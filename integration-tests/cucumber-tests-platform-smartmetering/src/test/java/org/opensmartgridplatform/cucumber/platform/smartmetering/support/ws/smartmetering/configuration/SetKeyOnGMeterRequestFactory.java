/*
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.cucumber.platform.smartmetering.support.ws.smartmetering.configuration;

import java.util.Map;
import org.opensmartgridplatform.adapter.ws.schema.smartmetering.configuration.SecretType;
import org.opensmartgridplatform.adapter.ws.schema.smartmetering.configuration.SetKeyOnGMeterAsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.smartmetering.configuration.SetKeyOnGMeterRequest;
import org.opensmartgridplatform.adapter.ws.schema.smartmetering.configuration.SetKeyOnGMeterRequestData;
import org.opensmartgridplatform.cucumber.platform.smartmetering.PlatformSmartmeteringDefaults;
import org.opensmartgridplatform.cucumber.platform.smartmetering.PlatformSmartmeteringKeys;
import org.opensmartgridplatform.cucumber.platform.smartmetering.support.ws.smartmetering.RequestFactoryHelper;

public class SetKeyOnGMeterRequestFactory {
  private SetKeyOnGMeterRequestFactory() {
    // Private constructor for utility class
  }

  public static SetKeyOnGMeterRequest fromParameterMap(
      final Map<String, String> requestParameters) {
    final SetKeyOnGMeterRequest SetKeyOnGMeterRequest = new SetKeyOnGMeterRequest();
    SetKeyOnGMeterRequest.setDeviceIdentification(
        requestParameters.get(PlatformSmartmeteringKeys.KEY_DEVICE_IDENTIFICATION));

    final SetKeyOnGMeterRequestData data = new SetKeyOnGMeterRequestData();

    data.setMbusDeviceIdentification(
        requestParameters.get(PlatformSmartmeteringKeys.KEY_DEVICE_IDENTIFICATION));
    data.setSecretType(
        SecretType.valueOf(requestParameters.get(PlatformSmartmeteringKeys.SECRET_TYPE)));
    data.setCloseOpticalPort(
        Boolean.valueOf(
            requestParameters.getOrDefault(
                PlatformSmartmeteringKeys.CLOSE_OPTICAL_PORT,
                String.valueOf(PlatformSmartmeteringDefaults.CLOSE_OPTICAL_PORT))));

    SetKeyOnGMeterRequest.setSetKeyOnGMeterRequestData(data);

    return SetKeyOnGMeterRequest;
  }

  public static SetKeyOnGMeterAsyncRequest fromScenarioContext() {
    final SetKeyOnGMeterAsyncRequest SetKeyOnGMeterAsyncRequest = new SetKeyOnGMeterAsyncRequest();
    SetKeyOnGMeterAsyncRequest.setCorrelationUid(
        RequestFactoryHelper.getCorrelationUidFromScenarioContext());
    SetKeyOnGMeterAsyncRequest.setDeviceIdentification(
        RequestFactoryHelper.getDeviceIdentificationFromScenarioContext());
    return SetKeyOnGMeterAsyncRequest;
  }
}
