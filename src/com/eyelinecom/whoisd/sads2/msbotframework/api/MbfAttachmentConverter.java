package com.eyelinecom.whoisd.sads2.msbotframework.api;

import com.eyelinecom.whoisd.sads2.common.HttpDataLoader;
import com.eyelinecom.whoisd.sads2.content.attachments.Attachment;
import com.eyelinecom.whoisd.sads2.executors.connector.SADSInitializer;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.MbfAttachment;
import com.eyelinecom.whoisd.sads2.multipart.FileUpload.ByteFileUpload;
import com.google.common.base.Function;
import com.google.common.cache.Cache;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Properties;

import static com.eyelinecom.whoisd.sads2.content.attachments.Attachment.Type;
import static com.eyelinecom.whoisd.sads2.content.attachments.Attachment.Type.fromString;

public class MbfAttachmentConverter
    implements Function<Attachment, MbfAttachment> {

  private final Logger log;
  private final HttpDataLoader loader;
  private final Cache<Long, byte[]> fileCache;
  private final String resourceBaseUrl;

  public MbfAttachmentConverter(Logger log,
                                HttpDataLoader loader,
                                Cache<Long, byte[]> fileCache,
                                String resourceBaseUrl) {

    this.log = log;
    this.loader = loader;
    this.fileCache = fileCache;
    this.resourceBaseUrl = resourceBaseUrl;
  }

  @Override
  public MbfAttachment apply(final Attachment _) {

    final Type type = fromString(_.getType());
    if (type == null) {
      return null;
    }

    final ByteFileUpload upload = _.asFileUpload(log, loader, resourceBaseUrl);
    if (upload == null) {
      return null;
    }

    // MS Bot Framework requires content type for attachments.
    final String contentType;
    try {
      if (upload.getMime() != null) {
        contentType = upload.getMime();

      } else {
       String guessedMime =
            URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(upload.getBytes()));
       if (guessedMime == null) {
         // URLConnection supports a very limited subset of content types, so
         // try guessing by extension.
         if (upload.getFileName() != null &&
             upload.getFileName().toLowerCase().endsWith(".bmp")) {
           guessedMime = "image/bmp";
         }
       }
       contentType = guessedMime;
      }

    } catch (IOException e) {
      log.warn("Failed matching content type", e);
      return null;
    }

    if (upload.getUrl() != null) {
      return new MbfAttachment(contentType, upload.getUrl());

    } else {
      // In case an attachment is declared using inline BASE64 content, we should provide
      // an externally accessible URL to pass into MBF.

      // XXX: Temporary, need to re-implement ID generation.
      final long fileId = Arrays.hashCode(upload.getBytes());
      fileCache.put(fileId, upload.getBytes());

      return new MbfAttachment(contentType, getAttachmentUrl(String.valueOf(fileId)));
    }
  }

  private String getAttachmentUrl(String fileId) {
    final Properties mainProperties;
    try {
      mainProperties = SADSInitializer.getMainProperties();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return mainProperties.getProperty("root.uri") + "/files/" + fileId;
  }
}
