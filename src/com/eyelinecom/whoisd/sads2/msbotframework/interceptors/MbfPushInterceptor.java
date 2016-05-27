package com.eyelinecom.whoisd.sads2.msbotframework.interceptors;

import com.eyelinecom.whoisd.sads2.RequestDispatcher;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.common.PageBuilder;
import com.eyelinecom.whoisd.sads2.common.SADSInitUtils;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.connector.SADSResponse;
import com.eyelinecom.whoisd.sads2.connector.Session;
import com.eyelinecom.whoisd.sads2.content.ContentRequestUtils;
import com.eyelinecom.whoisd.sads2.content.ContentResponse;
import com.eyelinecom.whoisd.sads2.exception.InterceptionException;
import com.eyelinecom.whoisd.sads2.executors.connector.SADSExecutor;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Action;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Attachment;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Message;
import com.eyelinecom.whoisd.sads2.msbotframework.connector.MbfMessageConnector;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfServiceRegistry;
import com.eyelinecom.whoisd.sads2.msbotframework.resource.MbfApi;
import com.eyelinecom.whoisd.sads2.session.ServiceSessionManager;
import com.eyelinecom.whoisd.sads2.session.SessionManager;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static com.eyelinecom.whoisd.sads2.content.attributes.AttributeReader.getAttributes;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@SuppressWarnings("unused")
public class MbfPushInterceptor extends MbfPushBase implements Initable {

  private static final Logger log = Logger.getLogger(MbfPushInterceptor.class);

  private MbfServiceRegistry serviceRegistry;
  private MbfApi client;
  private ServiceSessionManager sessionManager;

  @Override
  public void afterResponseRender(SADSRequest request,
                                  ContentResponse content,
                                  SADSResponse response,
                                  RequestDispatcher dispatcher) throws InterceptionException {

    if (log.isTraceEnabled()) {
      log.trace("MbfPushInterceptor.afterContentResponse" +
          " request = [" + request + "]," +
          " response = [" + response + "]," +
          " content = [" + content + "]," +
          " dispatcher = [" + dispatcher + "]");
    }

    try {

      if (isNotBlank(request.getParameters().get("sadsSmsMessage"))) {
        // TODO: rely on MessagesAdaptor, use concatenated message text & clear them after processing.
        sendMbfMessage(
            request,
            content,
            request.getParameters().get("sadsSmsMessage"),
            request.getParameters().get("keyboard"));

      } else {
        sendMbfMessage(request, content, response);
      }

    } catch (Exception e) {
      throw new InterceptionException(e);
    }
  }

  /**
   * Processes content-originated messages.
   */
  private void sendMbfMessage(SADSRequest request,
                              ContentResponse contentResponse,
                              SADSResponse response) throws Exception {

    final String serviceId = request.getServiceId();
    final Document doc = (Document) response.getAttributes().get(PageBuilder.VALUE_DOCUMENT);

    final List<Element> buttons = getKeyboard(doc);

    String text = getText(doc);

    final boolean isNothingToSend = StringUtils.isBlank(text) && buttons == null;
    if (!isNothingToSend) {
      // TODO: check if a message with no text is allowed and we don't need this hack.
      text = text.isEmpty() ? "." : text;
    }

    final boolean shouldCloseSession =
        buttons == null && doc.getRootElement().elements("input").isEmpty() &&
            !getAttributes(doc.getRootElement()).getBoolean("mbf.keep.session").or(false);

    final SessionManager sessionManager =
        this.sessionManager.getSessionManager(request.getProtocol(), serviceId);
    final Session session = request.getSession();

    if (!shouldCloseSession) {
      session.setAttribute(SADSExecutor.ATTR_SESSION_PREVIOUS_PAGE, doc);
      session.setAttribute(
          MbfMessageConnector.ATTR_SESSION_PREVIOUS_PAGE_URI,
          response.getAttributes().get(ContentRequestUtils.ATTR_REQUEST_URI));
    }

    if (!isNothingToSend) {
      final Message initialMessage =
          (Message) request.getAttributes().get("mbf.message");

      final Message reply = initialMessage.createReply();
      reply.setText(text);

      final List<Attachment> attachments = new ArrayList<Attachment>() {{
        // Keyboard.
        if (buttons != null) {
          final List<Action> actions = new ArrayList<>();
          for (Element button : buttons) {
            actions.add(new Action(button.getTextTrim(), button.getTextTrim()));
          }

          add(new Attachment(actions.toArray(new Action[0])));
        }

      }};

      reply.setAttachments(attachments.toArray(new Attachment[0]));

      client.send(sessionManager, serviceRegistry.getBot(serviceId), reply);
    }

    if (shouldCloseSession) {
      // No inputs mean that the dialog is over.
      session.close();
    }
  }

  /**
   * Processes PUSH messages.
   */
  private void sendMbfMessage(final SADSRequest request,
                              final ContentResponse content,
                              String message,
                              String keyboard) throws Exception {

    // TODO
  }

  public static String getText(final Document doc) throws DocumentException {
    final Collection<String> messages = new ArrayList<String>() {{
      //noinspection unchecked
      for (Element e : (List<Element>) doc.getRootElement().elements("message")) {
        add(getContent(e));
      }
    }};

    return StringUtils.join(messages, "\n").trim();
  }

  public static String getContent(Element element) throws DocumentException {
    final StringBuilder buf = new StringBuilder();

    final Element messageElement = new SAXReader()
        .read(new ByteArrayInputStream(element.asXML().getBytes(StandardCharsets.UTF_8)))
        .getRootElement();

    //noinspection unchecked
    for (Node e : (List<Node>) messageElement.selectNodes("//text()")) {
      if (!"pre".equals(e.getParent().getName())) {
        e.setText(e.getText().replaceAll("\\n\\s+", "\n"));
      }
    }

    //noinspection unchecked
    for (Node e : (Collection<Node>) IteratorUtils.toList(messageElement.nodeIterator())) {
      String text = e.asXML();
//      if (!AttributeReader.getAttributes(e).getBoolean("html.escape").or(true)) {
//        text = ESCAPE_SKYPE.translate(text);
//      }

      buf.append(text);
    }
    return buf.toString().trim();
  }

  @Override
  public void init(Properties config) throws Exception {
    serviceRegistry = SADSInitUtils.getResource("msbotframework-service-registry", config);
    client = SADSInitUtils.getResource("client", config);
    sessionManager = SADSInitUtils.getResource("session-manager", config);
  }

  @Override
  public void destroy() {
    // Nothing here.
  }
}