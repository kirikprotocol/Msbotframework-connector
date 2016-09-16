package com.eyelinecom.whoisd.sads2.msbotframework.connector;

import com.eyelinecom.whoisd.sads2.Protocol;
import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.SADSUrlUtils;
import com.eyelinecom.whoisd.sads2.common.UrlUtils;
import com.eyelinecom.whoisd.sads2.connector.ChatCommand;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.connector.SADSResponse;
import com.eyelinecom.whoisd.sads2.connector.Session;
import com.eyelinecom.whoisd.sads2.events.Event;
import com.eyelinecom.whoisd.sads2.events.LinkEvent;
import com.eyelinecom.whoisd.sads2.events.MessageEvent.TextMessageEvent;
import com.eyelinecom.whoisd.sads2.exception.NotFoundResourceException;
import com.eyelinecom.whoisd.sads2.exception.NotFoundServiceException;
import com.eyelinecom.whoisd.sads2.executors.connector.AbstractHTTPPushConnector;
import com.eyelinecom.whoisd.sads2.executors.connector.ProfileEnabledMessageConnector;
import com.eyelinecom.whoisd.sads2.executors.connector.SADSExecutor;
import com.eyelinecom.whoisd.sads2.input.AbstractInputType;
import com.eyelinecom.whoisd.sads2.input.InputFile;
import com.eyelinecom.whoisd.sads2.input.InputLocation;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Activity;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Entity;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.MbfAttachment;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfServiceRegistry;
import com.eyelinecom.whoisd.sads2.msbotframework.resource.MbfApi;
import com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils;
import com.eyelinecom.whoisd.sads2.profile.Profile;
import com.eyelinecom.whoisd.sads2.registry.ServiceConfig;
import com.eyelinecom.whoisd.sads2.session.SessionManager;
import com.eyelinecom.whoisd.sads2.utils.ConnectorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.eyelinecom.whoisd.sads2.connector.ChatCommand.CLEAR_PROFILE;
import static com.eyelinecom.whoisd.sads2.connector.ChatCommand.INVALIDATE_SESSION;
import static com.eyelinecom.whoisd.sads2.connector.ChatCommand.SHOW_PROFILE;
import static com.eyelinecom.whoisd.sads2.connector.ChatCommand.WHO_IS;
import static com.eyelinecom.whoisd.sads2.wstorage.profile.QueryRestrictions.property;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public class MbfMessageConnector extends HttpServlet {

  private final static Log log = new Log4JLogger(Logger.getLogger(MbfMessageConnector.class));

  private MbfMessageConnectorImpl connector;

  @Override
  public void destroy() {
    super.destroy();
    connector.destroy();
  }

  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    connector = new MbfMessageConnectorImpl();

    try {
      final Properties properties = AbstractHTTPPushConnector.buildProperties(servletConfig);
      connector.init(properties);

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  @Override
  protected void service(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {

    final MbfWebhookRequest request = new MbfWebhookRequest(req);

    try {
      final Activity activity = request.asMessage();

      switch (activity.getType()) {

        // Don't support this, so just acknowledge and omit any further processing:
        case CONVERSATION_UPDATE:
        case CONTACT_RELATION_UPDATE:
        case TYPING:
        case DELETE_USER_DATA:

        // Platform heartbeat, reply with HTTP-200.
        case PING:
        {
          final SADSResponse rc = new SADSResponse();
          rc.setStatus(200);
          rc.setHeaders(Collections.<String, String>emptyMap());

          ConnectorUtils.fillHttpResponse(resp, rc);

          return;
        }

        case MESSAGE:
          break;
      }

      final SADSResponse response = connector.process(request);
      ConnectorUtils.fillHttpResponse(resp, response);

    } catch (MbfException e) {
      throw new ServletException(e);
    }
  }


  //
  //
  //

  private class MbfMessageConnectorImpl
      extends ProfileEnabledMessageConnector<MbfWebhookRequest> {

    /** Response to a WebHook update. */
    @Override
    protected SADSResponse buildQueuedResponse(MbfWebhookRequest req,
                                               SADSRequest sadsRequest) {

      return buildWebhookResponse(200);
    }

    @Override
    protected SADSResponse buildQueueErrorResponse(Exception e,
                                                   MbfWebhookRequest httpServletRequest,
                                                   SADSRequest sadsRequest) {
      return buildWebhookResponse(500);
    }

    @Override
    protected Log getLogger() {
      return MbfMessageConnector.log;
    }

    @Override
    protected String getSubscriberId(MbfWebhookRequest req) throws Exception {
      if (req.getProfile() != null) {
        return req.getProfile().getWnumber();
      }

      final Activity msg = req.asMessage();
      final String channelId = msg.getChannelId();

      final String incoming = req.getMessageText();
      if (ChatCommand.match(getServiceId(req), incoming, msg.getProtocol()) == CLEAR_PROFILE) {
        // Reset profile of the current user.

        final String from = msg.getFrom().getId();
        final Profile profile = getProfileStorage()
            .query()
            .where(property("mbf-" + channelId, "id").eq(from))
            .get();
        if (profile != null) {
          profile.delete();
        }
      }

      final Profile profile;

      final String userId = msg.getFrom().getId();
      if (userId != null) {
        profile = getProfileStorage()
            .query()
            .where(property("mbf-" + channelId, "id").eq(userId))
            .getOrCreate();

        // Conversation ID.
        profile.property("mbf-" + channelId, "chats", req.getAppId()).set(msg.getConversation().getId());

        // Bot user ID.
        profile.property("mbf-" + channelId, "bots", req.getAppId()).set(msg.getRecipient().getId());

      } else {
        profile = null;
      }

      req.setProfile(profile);

      //noinspection ConstantConditions
      return profile.getWnumber();
    }

    @Override
    protected String getServiceId(MbfWebhookRequest req) {
      try {
        return getServiceRegistry().findService(req.getAppId());

      } catch (NotFoundServiceException | NotFoundResourceException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    protected String getGateway() {
      return "MsBotFramework";
    }

    @Override
    protected String getGatewayRequestDescription(MbfWebhookRequest req) {
      String channelId = null;
      try {
        channelId = req.asMessage().getChannelId();

      } catch (IOException | MbfException e) {
        getLogger().warn("Cannot determine ChannelId from message [" + req + "]");
      }

      return "MsBotFramework" + (channelId == null ? "" : "/" + channelId);
    }

    @Override
    protected void fillSADSRequest(SADSRequest sadsRequest, MbfWebhookRequest req) {
      try {
        // We might need an original message for constructing reply.
        final Activity msg = req.asMessage();
        sadsRequest.getAttributes().put("mbf.message", msg);

        handleFileUpload(sadsRequest, req);

      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

      super.fillSADSRequest(sadsRequest, req);
    }

    private void handleFileUpload(SADSRequest sadsRequest,
                                  MbfWebhookRequest req) throws Exception {

      // Submit uploaded files to the content service via request parameter.

      final List<? extends AbstractInputType> mediaList = extractMedia(req);
      if (isEmpty(mediaList)) {
        return;
      }

      req.setEvent(mediaList.iterator().next().asEvent());

      final String inputName;
      {
        final Session session = sadsRequest.getSession();
        final Document prevPage =
            (Document) session.getAttribute(SADSExecutor.ATTR_SESSION_PREVIOUS_PAGE);

        final Element input =
            prevPage == null ? null : prevPage.getRootElement().element("input");
        if (input != null) {
          inputName = input.attributeValue("name");

        } else {
          inputName = "bad_command";
        }
      }

      final String mediaParameter = MarshalUtils.marshal(mediaList);
      sadsRequest.getParameters().put(inputName, mediaParameter);
      sadsRequest.getParameters().put("input_type", "json");
    }

    private List<? extends AbstractInputType> extractMedia(MbfWebhookRequest req)
        throws Exception {

      final Activity msg = req.asMessage();

      final List<AbstractInputType> rc = new ArrayList<>();

      if (msg.getAttachments() != null && msg.getAttachments().length != 0) {
        for (MbfAttachment attachment : msg.getAttachments()) {
          final String url = trimToNull(attachment.getContentUrl());
          if (url == null) {
            // No content URL means this is not an attachment.
            continue;
          }

          final String contentType = trimToNull(attachment.getContentType());
          if (contentType.startsWith("image")) {
            final InputFile file = new InputFile();
            file.setMediaType("photo");
            file.setUrl(url);
            rc.add(file);

          } else {
            // TODO: correctly determine MediaType.
            final InputFile file = new InputFile();
            file.setMediaType("document");
            file.setUrl(url);
            rc.add(file);
          }
        }
      }

      if (msg.getEntities() != null && msg.getEntities().length != 0) {
        for (Entity entity : msg.getEntities()) {
          if (entity instanceof Entity.Place && ((Entity.Place) entity).getGeo() != null) {
            final Entity.GeoCoordinates geo = ((Entity.Place) entity).getGeo();
            rc.add(new InputLocation(geo.getLongitude(), geo.getLatitude()));
          }
        }

      }

      return Collections.unmodifiableList(rc);
    }

    @Override
    protected Protocol getRequestProtocol(ServiceConfig config,
                                          String subscriberId,
                                          MbfWebhookRequest req) {
      try {
        return req.asMessage().getProtocol();

      } catch (IOException | MbfException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    protected String getRequestUri(ServiceConfig config,
                                   String wnumber,
                                   MbfWebhookRequest req) throws Exception {

      final String serviceId = config.getId();
      final Activity msg = req.asMessage();
      final String incoming = req.getMessageText();
      final MbfBotDetails bot = getServiceRegistry().getBot(serviceId);
      final Protocol protocol = getRequestProtocol(config, wnumber, req);
      final SessionManager sessionManager = getSessionManager(protocol, serviceId);

      Session session = sessionManager.getSession(wnumber);

      final ChatCommand cmd = ChatCommand.match(serviceId, incoming, protocol);

      if (cmd == INVALIDATE_SESSION) {
        // Invalidate the current session.
        session.close();
        session = sessionManager.getSession(wnumber);

      } else if (cmd == WHO_IS) {
        final Activity reply = msg.createReply(
            new Date(),
            StringUtils.join(
                new String[] {
                    "Application ID: " + bot.getAppId() + ".",
                    "Secret: " + bot.getAppSecret() + ".",
                    "Service: " + serviceId + ".",
                    "Mobilizer instance: " + getRootUri()
                },
                "\n\n")
        );
        getClient().send(sessionManager, bot, reply);

      } else if (cmd == SHOW_PROFILE) {
        final Profile profile = getProfileStorage().find(wnumber);

        final Activity reply = msg.createReply(new Date(), profile.dump().replace("\n", "\n\n"));

        getClient().send(sessionManager, bot, reply);
      }

      final String prevUri = (String) session.getAttribute(ATTR_SESSION_PREVIOUS_PAGE_URI);
      if (prevUri == null) {
        // No previous page means this is an initial request, thus serve the start page.
        req.setEvent(new TextMessageEvent(incoming));
        return super.getRequestUri(config, wnumber, req);

      } else {
        final Document prevPage =
            (Document) session.getAttribute(SADSExecutor.ATTR_SESSION_PREVIOUS_PAGE);

        String href = null;
        String inputName = null;

        // Look for a button with a corresponding label.
        //noinspection unchecked
        for (Element e : (List<Element>) prevPage.getRootElement().elements("button")) {
          final String btnLabel = e.getTextTrim();
          final String btnIndex = e.attributeValue("index");

          if (equalsIgnoreCase(btnLabel, incoming) || equalsIgnoreCase(btnIndex, incoming)) {
            final String btnHref = e.attributeValue("href");
            href = btnHref != null ? btnHref : e.attributeValue("target");

            req.setEvent(new LinkEvent(btnLabel, prevUri));
          }
        }

        // Look for input field if any.
        if (href == null) {
          final Element input = prevPage.getRootElement().element("input");
          if (input != null) {
            href = input.attributeValue("href");
            inputName = input.attributeValue("name");
          }
        }

        // Nothing suitable to handle user input found, consider it a bad command.
        if (href == null) {
          final String badCommandPage =
              InitUtils.getString("bad-command-page", "", config.getAttributes());
          href = UrlUtils.merge(prevUri, badCommandPage);
          href = UrlUtils.addParameter(href, "bad_command", incoming);
        }

        if (req.getEvent() == null) {
          req.setEvent(new TextMessageEvent(incoming));
        }

        href = SADSUrlUtils.processUssdForm(href, StringUtils.trim(incoming));
        if (inputName != null) {
          href = UrlUtils.addParameter(href, inputName, incoming);
        }

        return UrlUtils.merge(prevUri, href);
      }
    }

    private SADSResponse buildWebhookResponse(int statusCode) {
      final SADSResponse rc = new SADSResponse();
      rc.setStatus(statusCode);
      rc.setHeaders(Collections.<String, String>emptyMap());
      return rc;
    }

    /**
     * @param request   Request to the content provider
     * @param response  Response from content provider
     */
    @Override
    protected SADSResponse getOuterResponse(MbfWebhookRequest req,
                                            SADSRequest request,
                                            SADSResponse response) {
      return buildWebhookResponse(200);
    }

    @Override
    protected Event getEvent(MbfWebhookRequest req) {
      return req.getEvent();
    }

    @Override
    protected Profile getCachedProfile(MbfWebhookRequest req) {
      return req.getProfile();
    }

    private MbfServiceRegistry getServiceRegistry() throws NotFoundResourceException {
      return getResource("msbotframework-service-registry");
    }

    private MbfApi getClient() throws NotFoundResourceException {
      return getResource("msbotframework-api");
    }

  }
}
