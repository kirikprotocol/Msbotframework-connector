package com.eyelinecom.whoisd.sads2.msbotframework.resource;

import com.eyelinecom.whoisd.sads2.Protocol;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Activity;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;
import com.eyelinecom.whoisd.sads2.profile.ProfileStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public interface MbfApi {

  /** MBF-specific system-wide profile. */
  String MBF_PROFILE = "mbf";

  Activity send(MbfBotDetails bot,
                Activity activity) throws MbfException;

  /**
   * Checks if the supplied credentials are valid.
   *
   * @throws MbfException in case validation failed for whatever reason.
   */
  void checkCredentials(MbfBotDetails bot) throws MbfException;


  //
  //
  //

  static Optional<String> getApiUrl(ProfileStorage profileStorage,

                                    String appId,
                                    Protocol protocol) {
    return Optional.ofNullable(
        profileStorage
            .findOrCreate(MBF_PROFILE)
            .property("api-url", appId, protocol.getProtocolName())
            .getValue()
    );
  }

  static void setApiUrl(ProfileStorage profileStorage,

                        String appId,
                        Protocol protocol,
                        String apiUrl) {

    profileStorage
        .findOrCreate(MBF_PROFILE)
        .property("api-url", appId, protocol.getProtocolName())
        .set(checkNotNull(apiUrl));
  }

  String locationLinkUrl(double latitude, double longitude);

  String locationImageUrl(double latitude, double longitude);

}
