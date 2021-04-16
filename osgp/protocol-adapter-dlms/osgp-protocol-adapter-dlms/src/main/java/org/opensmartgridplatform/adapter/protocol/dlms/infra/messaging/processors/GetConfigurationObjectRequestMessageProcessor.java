/*
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.infra.messaging.processors;

import java.io.Serializable;
import org.opensmartgridplatform.adapter.protocol.dlms.application.services.ConfigurationService;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.factories.DlmsConnectionManager;
import org.opensmartgridplatform.adapter.protocol.dlms.infra.messaging.DeviceRequestMessageProcessor;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.infra.jms.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Class for processing Get Configuration Object Request messages */
@Component
public class GetConfigurationObjectRequestMessageProcessor extends DeviceRequestMessageProcessor {

  @Autowired private ConfigurationService configurationService;

  public GetConfigurationObjectRequestMessageProcessor() {
    super(MessageType.GET_CONFIGURATION_OBJECT);
  }

  @Override
  protected Serializable handleMessage(
      final DlmsConnectionManager conn, final DlmsDevice device, final Serializable requestObject)
      throws OsgpException {

    return this.configurationService.requestGetConfigurationObject(conn, device);
  }
}
