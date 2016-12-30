package com.eyelinecom.whoisd.sads2.msbotframework.interceptors;

import com.eyelinecom.whoisd.sads2.RequestDispatcher;
import com.eyelinecom.whoisd.sads2.common.HttpDataLoader;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.common.PageBuilder;
import com.eyelinecom.whoisd.sads2.common.SADSInitUtils;
import com.eyelinecom.whoisd.sads2.common.StringUtils;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.connector.SADSResponse;
import com.eyelinecom.whoisd.sads2.connector.Session;
import com.eyelinecom.whoisd.sads2.content.ContentRequestUtils;
import com.eyelinecom.whoisd.sads2.content.ContentResponse;
import com.eyelinecom.whoisd.sads2.content.attachments.Attachment;
import com.eyelinecom.whoisd.sads2.content.attributes.AttributeSet;
import com.eyelinecom.whoisd.sads2.exception.InterceptionException;
import com.eyelinecom.whoisd.sads2.executors.connector.SADSExecutor;
import com.eyelinecom.whoisd.sads2.msbotframework.api.MbfAttachmentConverter;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Activity;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ActivityType;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ChannelAccount;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ConversationAccount;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.MbfAttachment;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments.HeroCard;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook.Bubble;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook.Button;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook.FacebookChannelData;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook.GenericTemplate;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook.TemplateAttachment;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfServiceRegistry;
import com.eyelinecom.whoisd.sads2.msbotframework.resource.MbfApi;
import com.eyelinecom.whoisd.sads2.profile.Profile;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.eyelinecom.whoisd.sads2.Protocol.FACEBOOK;
import static com.eyelinecom.whoisd.sads2.Protocol.SKYPE;
import static com.eyelinecom.whoisd.sads2.content.attributes.AttributeReader.getAttributes;
import static com.eyelinecom.whoisd.sads2.executors.connector.ProfileEnabledMessageConnector.ATTR_SESSION_PREVIOUS_PAGE_URI;
import static com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfServiceRegistry.CONF_TOKEN;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.partition;
import static java.util.Collections.singletonList;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.join;

@SuppressWarnings({"unused", "WeakerAccess"})
public class MbfPushInterceptor extends MbfPushBase implements Initable {

  private static final Logger log = Logger.getLogger(MbfPushInterceptor.class);

  private static final int FB_ACTIONS_PER_GROUP = 3;

  /**
   * Maximal allowed text message length, symbols. Applies to messages with no buttons or
   * button template (i.e. 3 buttons without a bubble).
   *
   * <p>Actually, this is declared to be 320 symbols by FB API, but in fact MBF still splits
   * messages at ~300 symbols boundary. And since it doesn't actually care about smart splitting
   * (e.g. taking newlines and other separators in the markup into account) that's better be done
   * on our side.
   */
  private static final int FB_MAX_MSG_SIZE = 300;

  /**
   * Maximal allowed text message length, symbols. Applies to generic template header
   * (i.e. message with more than 3 buttons).
   */
  private static final int FB_BUBBLE_HEADER_SIZE = 80;

  private MbfServiceRegistry serviceRegistry;
  private MbfApi client;
  private HttpDataLoader loader;
  private Cache<Long, byte[]> fileCache;

  private String facebookBubbleHeader = ".";

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
    final Collection<Attachment> attachments = Attachment.extract(log, doc);

    String text = getText(doc);

    final boolean isNothingToSend =
        isBlank(text) && buttons == null && CollectionUtils.isEmpty(attachments);
    if (!isNothingToSend) {
      // Messages with no text are not allowed.
      text = text.isEmpty() ? "." : text;
    }

    final boolean shouldCloseSession;
    {
      if (buttons != null || !doc.getRootElement().elements("input").isEmpty()) {
        shouldCloseSession = false;

      } else {
        final String protocolName = request.getProtocol().getProtocolName();
        final AttributeSet pageAttributes = getAttributes(doc.getRootElement());

        shouldCloseSession = !pageAttributes.getBoolean(protocolName + ".keep.session")
            .or(pageAttributes.getBoolean("mbf.keep.session"))
            .or(pageAttributes.getBoolean("keep.session"))
            .or(false);
      }
    }

    final Session session = request.getSession();

    if (!shouldCloseSession) {
      session.setAttribute(SADSExecutor.ATTR_SESSION_PREVIOUS_PAGE, doc);
      session.setAttribute(
          ATTR_SESSION_PREVIOUS_PAGE_URI,
          response.getAttributes().get(ContentRequestUtils.ATTR_REQUEST_URI));
    }

    if (!isNothingToSend) {

      final MbfBotDetails bot = Optional
          .fromNullable(serviceRegistry.getBot(serviceId))
          .or(serviceRegistry.findBotByMbfToken(
              request.getServiceScenario().getAttributes().getProperty(CONF_TOKEN)
          ));


      final List<Activity> activities = createResponse(request, bot, doc, text, attachments);
      for (Activity activity : activities) {
        client.send(bot, activity);
      }

    }

    if (shouldCloseSession) {
      // No inputs mean that the dialog is over.
      session.close();
    }
  }

  List<Activity> createResponse(final SADSRequest request,
                                MbfBotDetails bot,
                                Document doc,
                                final String text,
                                Collection<Attachment> rawFileAttachments) {

    final List<Element> buttons = getKeyboard(doc);

    final List<String> labels = isNotEmpty(buttons) ?
        new ArrayList<String>() {{ for (Element _ : buttons) add(_.getTextTrim()); }} :
        Collections.<String>emptyList();

    // File attachments.
    final List<MbfAttachment> fileAttachments = new ArrayList<>();
    if (isNotEmpty(rawFileAttachments)) {
      final MbfAttachmentConverter converter =
          new MbfAttachmentConverter(log, loader, fileCache, request.getResourceURI());
      fileAttachments.addAll(filter(transform(rawFileAttachments, converter), notNull()));
    }

    if (request.getProtocol() == SKYPE) {
      final Activity msg = createActivityTemplate(request, bot);

      if (isEmpty(buttons)) {
        msg.setText(text);

      } else {
        final String textKeyboard = getTextKeyboardMd(buttons);
        msg.setText(text + "\n\n" + textKeyboard);
      }

      if (isNotEmpty(fileAttachments)) {
        msg.setAttachments(fileAttachments);
      }

      return singletonList(msg);

    } else if (request.getProtocol() == FACEBOOK) {

      if (isEmpty(buttons)) {
        // Simple text message. Don't care about text size limits, MBF will handle that.
        final Activity msg = createActivityTemplate(request, bot);
        msg.setText(text);

        if (isNotEmpty(fileAttachments)) {
          final ArrayList<MbfAttachment> attachments = new ArrayList<>();
          attachments.addAll(fileAttachments);
          msg.setAttachments(attachments);
        }

        return Collections.singletonList(msg);

      } else if (buttons.size() <= FB_ACTIONS_PER_GROUP) {
        // Message with links, use FB's ButtonTemplate.
        return createFbButtonTemplate(request, bot, text, labels, fileAttachments);

      } else {
        // Use FB's Generic template via ChannelData.
        return createFbGenericTemplate(request, bot, text, labels, fileAttachments);
      }

    } else {
      // Unsupported protocol.
      return Collections.emptyList();
    }
  }

  private List<Activity> createFbButtonTemplate(final SADSRequest request,
                                                MbfBotDetails bot,
                                                final String text,
                                                final List<String> labels,
                                                final List<MbfAttachment> fileAttachments) {

    final List<String> textParts = StringUtils.splitText(text, '\n', FB_MAX_MSG_SIZE);

    if (textParts.size() == 1) {
      // Text fits into a single ButtonTemplate, send via HeroCard.
      final Activity msg = createActivityTemplate(request, bot);

      final ArrayList<MbfAttachment> attachments = new ArrayList<MbfAttachment>() {{
        add(HeroCard.fromOptions(text, labels));
        addAll(fileAttachments);
      }};
      msg.setAttachments(attachments);

      return Collections.singletonList(msg);

    } else {
      final List<Activity> messages = new ArrayList<>();

      // First, send all the text split into several messages
      for (String textPart : textParts) {
        messages.add(
            createActivityTemplate(request, bot)
                .setText(textPart)
        );
      }

      // Then the last text part as a bubble w/ all remaining links & attachments.
      final Activity msg = createActivityTemplate(request, bot);
      final ArrayList<MbfAttachment> attachments = new ArrayList<MbfAttachment>() {{
        add(HeroCard.fromOptions(getFbSingleBubbleHeader(request), labels));
        addAll(fileAttachments);
      }};
      msg.setAttachments(attachments);

      messages.add(msg);

      return Collections.unmodifiableList(messages);
    }
  }

  private List<Activity> createFbGenericTemplate(SADSRequest request,
                                                 MbfBotDetails bot,
                                                 String text,
                                                 List<String> labels,
                                                 List<MbfAttachment> fileAttachments) {

    if (text.length() <= FB_BUBBLE_HEADER_SIZE) {
      // Text fits into a single GenericTemplate.
      final Activity msg = createActivityTemplate(request, bot);
      msg.setChannelData(asFbGenericTemplate(text, labels));

      if (isNotEmpty(fileAttachments)) {
        final ArrayList<MbfAttachment> attachments = new ArrayList<>();
        attachments.addAll(fileAttachments);
        msg.setAttachments(attachments);
      }

      return Collections.singletonList(msg);

    } else {
      final List<String> textParts = StringUtils.splitText(text, '\n', FB_MAX_MSG_SIZE);

      final List<Activity> messages = new ArrayList<>();

      for (String textPart : textParts) {
        messages.add(
            createActivityTemplate(request, bot)
                .setText(textPart)
        );
      }

      messages.add(
          createActivityTemplate(request, bot)
              .setChannelData(asFbGenericTemplate(getFbSingleBubbleHeader(request), labels))
      );

      if (isNotEmpty(fileAttachments)) {
        final ArrayList<MbfAttachment> attachments = new ArrayList<>();
        attachments.addAll(fileAttachments);
        getLast(messages).setAttachments(attachments);
      }

      return Collections.unmodifiableList(messages);
    }
  }

  Activity createActivityTemplate(final SADSRequest request, final MbfBotDetails bot) {
    final Activity initialActivity =
        (Activity) request.getAttributes().get("mbf.message");

    if (initialActivity != null) {
      return initialActivity.createReply(new Date());

    } else {
      // PUSH request, so rely on Profile to be initialized w/ all the necessary stuff.
      final String protocolName = request.getProtocol().getProtocolName();

      final Profile profile = request.getProfile();

      return new Activity() {{
        setType(ActivityType.MESSAGE);
        setFrom(
            new ChannelAccount(
                checkNotNull(
                    profile.property("mbf-" + protocolName, "bots", bot.getAppId()).getValue()
                )
            )
        );
        setRecipient(
            new ChannelAccount(
                checkNotNull(
                  profile.property("mbf-" + protocolName, "id").getValue()
                )
            )
        );
        setConversation(
            new ConversationAccount(
                checkNotNull(
                  profile.property("mbf-" + protocolName, "chats", bot.getAppId()).getValue()
                )
            )
        );

        setChannelId(Activity.asChannelId(request.getProtocol()));
      }};
    }
  }

  private FacebookChannelData asFbGenericTemplate(final String text, final List<String> labels) {
    final List<Bubble> bubbles = new ArrayList<Bubble>() {{
      final List<List<String>> parts = partition(labels, FB_ACTIONS_PER_GROUP);
      for (int i = 0; i < parts.size(); i++) {
        final boolean firstBubble = i == 0;
        final List<String> actions = parts.get(i);

        add(new Bubble() {{
          setTitle(firstBubble ? text : facebookBubbleHeader);
          setButtons(newArrayList(transform(actions,
              new Function<String, Button>() {
                @Override public Button apply(String _) { return Button.postback(_); }
              }))
          );
        }});
      }
    }};

    final GenericTemplate template = new GenericTemplate(bubbles);
    return new FacebookChannelData() {{
      setAttachment(new TemplateAttachment(template));
    }};
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

    return join(messages, "\n\n").trim();
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

  protected String getFbSingleBubbleHeader(SADSRequest request) {
    return getLocalizedProperty(request, "facebook.single.bubble.header", ".");
  }

  private String getLocalizedProperty(SADSRequest request, String name, String defaultValue) {
    final String lang = Optional
        .fromNullable((String) request.getAttributes().get("lang"))
        .or(Optional.fromNullable(request.getParameters().get("lang")))
        .orNull();

    final Properties attrs = request.getServiceScenario().getAttributes();

    final String localized = lang != null ? attrs.getProperty(name + "." + lang) : null;
    return Optional.fromNullable(localized).or(attrs.getProperty(name, defaultValue));
  }

  @Override
  public void init(Properties config) throws Exception {
    serviceRegistry = SADSInitUtils.getResource("msbotframework-service-registry", config);
    client = SADSInitUtils.getResource("client", config);
    loader = SADSInitUtils.getResource("loader", config);
    fileCache = SADSInitUtils.getResource("file-cache", config);
    facebookBubbleHeader = SADSInitUtils.getString("facebook.bubble.header", ".", config);
  }

  @Override
  public void destroy() {
    // Nothing here.
  }
}
