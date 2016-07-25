package com.eyelinecom.whoisd.sads2.msbotframework.interceptors;

import com.eyelinecom.whoisd.sads2.RequestDispatcher;
import com.eyelinecom.whoisd.sads2.common.HttpDataLoader;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.common.PageBuilder;
import com.eyelinecom.whoisd.sads2.common.SADSInitUtils;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.connector.SADSResponse;
import com.eyelinecom.whoisd.sads2.connector.Session;
import com.eyelinecom.whoisd.sads2.content.ContentRequestUtils;
import com.eyelinecom.whoisd.sads2.content.ContentResponse;
import com.eyelinecom.whoisd.sads2.content.attachments.Attachment;
import com.eyelinecom.whoisd.sads2.exception.InterceptionException;
import com.eyelinecom.whoisd.sads2.executors.connector.SADSExecutor;
import com.eyelinecom.whoisd.sads2.msbotframework.api.MbfAttachmentConverter;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Activity;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ActivityType;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ChannelAccount;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ConversationAccount;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.MbfAttachment;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments.HeroCard;
import com.eyelinecom.whoisd.sads2.msbotframework.connector.MbfMessageConnector;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfServiceRegistry;
import com.eyelinecom.whoisd.sads2.msbotframework.resource.MbfApi;
import com.eyelinecom.whoisd.sads2.profile.Profile;
import com.eyelinecom.whoisd.sads2.session.ServiceSessionManager;
import com.eyelinecom.whoisd.sads2.session.SessionManager;
import com.google.common.cache.Cache;
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
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.eyelinecom.whoisd.sads2.content.attributes.AttributeReader.getAttributes;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@SuppressWarnings({"unused", "WeakerAccess"})
public class MbfPushInterceptor extends MbfPushBase implements Initable {

  private static final Logger log = Logger.getLogger(MbfPushInterceptor.class);

  private MbfServiceRegistry serviceRegistry;
  private MbfApi client;
  private ServiceSessionManager sessionManager;
  private HttpDataLoader loader;
  private Cache<Long, byte[]> fileCache;

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
  private void sendMbfMessage(final SADSRequest request,
                              ContentResponse contentResponse,
                              SADSResponse response) throws Exception {

    final String serviceId = request.getServiceId();
    final Document doc = (Document) response.getAttributes().get(PageBuilder.VALUE_DOCUMENT);

    final List<Element> buttons = getKeyboard(doc);

    String text = getText(doc);

    final boolean isNothingToSend = StringUtils.isBlank(text) && buttons == null;
    if (!isNothingToSend) {
      // Messages with no text are not allowed.
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

      final MbfBotDetails bot = serviceRegistry.getBot(serviceId);

      final Activity initialActivity =
          (Activity) request.getAttributes().get("mbf.message");

      if (initialActivity == null) {
        // PUSH request, so rely on Profile to be initialized w/ all the necessary stuff.
        final String protocolName = request.getProtocol().getProtocolName();

        final Profile profile = request.getProfile();

        final Activity push = new Activity() {{
          setType(ActivityType.MESSAGE);
          setFrom(
              new ChannelAccount(
                  profile.property("mbf-" + protocolName, "bots", bot.getAppId()).getValue(),
                  null
              )
          );
          setRecipient(
              new ChannelAccount(
                  profile.property("mbf-" + protocolName, "id").getValue(),
                  null
              )
          );

          setConversation(
              new ConversationAccount(
                  profile.property("mbf-" + protocolName, "chats").getValue()
              )
          );
        }};

        fillContent(push, text, request, doc);

        client.send(sessionManager, bot, push);

      } else {
        final Activity reply = initialActivity.createReply(new Date());
        fillContent(reply, text, request, doc);

        client.send(sessionManager, bot, reply);
      }
    }

    if (shouldCloseSession) {
      // No inputs mean that the dialog is over.
      session.close();
    }
  }

  private void fillContent(Activity msg,

                           final String text,
                           SADSRequest request,
                           Document doc) {

    final int ACTIONS_PER_GROUP = 3;

    msg.setText(text);

    final List<MbfAttachment> attachments =
        extractAttachments(msg, request, doc);

    msg.setAttachments(attachments);
  }

  private List<MbfAttachment> extractAttachments(
      Activity msg, final SADSRequest request, final Document doc) {

    final List<MbfAttachment> target =
        new ArrayList<>();

    final List<Element> buttons = getKeyboard(doc);
    if (isNotEmpty(buttons)) {
      final List<String> actions = new ArrayList<>();
      for (Element button : buttons) {
        actions.add(button.getTextTrim());
      }

      target.add(HeroCard.fromOptions(msg.getText(), actions));
      msg.setText(null);
    }

    // File attachments.
    final Collection<Attachment> fileAttachments = Attachment.extract(log, doc);
    if (isNotEmpty(fileAttachments)) {
      final MbfAttachmentConverter converter =
          new MbfAttachmentConverter(log, loader, fileCache, request.getResourceURI());

      target.addAll(filter(transform(fileAttachments, converter), notNull()));
    }

    return target;
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

    return StringUtils.join(messages, "\n\n").trim();
  }

  public static String getContent(Element element) throws DocumentException {
    final StringBuilder buf = new StringBuilder();

    final Element messageElement = new SAXReader()
        .read(new ByteArrayInputStream(element.asXML().getBytes(StandardCharsets.UTF_8)))
        .getRootElement();

    //noinspection unchecked
    for (Node e : (List<Node>) messageElement.selectNodes("//text()")) {
//      if (!"pre".equals(e.getParent().getName())) {
//        e.setText(e.getText().replaceAll("\\n\\s+", "\n"));
//      }
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
    loader = SADSInitUtils.getResource("loader", config);
    fileCache = SADSInitUtils.getResource("file-cache", config);
  }

  @Override
  public void destroy() {
    // Nothing here.
  }
}
