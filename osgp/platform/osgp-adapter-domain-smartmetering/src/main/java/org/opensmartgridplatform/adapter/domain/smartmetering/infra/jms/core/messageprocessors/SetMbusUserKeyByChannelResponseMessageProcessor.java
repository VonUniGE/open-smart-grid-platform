/*
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.messageprocessors;

import org.opensmartgridplatform.adapter.domain.smartmetering.application.services.ConfigurationService;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.OsgpCoreResponseMessageProcessor;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.ws.WebServiceResponseMessageSender;
import org.opensmartgridplatform.shared.exceptionhandling.ComponentType;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.infra.jms.MessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.MessageProcessorMap;
import org.opensmartgridplatform.shared.infra.jms.MessageType;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/** Class for processing smart metering Set M-Bus User Key By Channel response messages */
@Component
public class SetMbusUserKeyByChannelResponseMessageProcessor
    extends OsgpCoreResponseMessageProcessor {

  @Autowired private ConfigurationService configurationService;

  @Autowired
  protected SetMbusUserKeyByChannelResponseMessageProcessor(
      final WebServiceResponseMessageSender responseMessageSender,
      @Qualifier("domainSmartMeteringInboundOsgpCoreResponsesMessageProcessorMap")
          final MessageProcessorMap messageProcessorMap) {
    super(
        responseMessageSender,
        messageProcessorMap,
        MessageType.SET_MBUS_USER_KEY_BY_CHANNEL,
        ComponentType.DOMAIN_SMART_METERING);
  }

  @Override
  protected boolean hasRegularResponseObject(final ResponseMessage responseMessage) {
    // Only the Result (OK/NOK/Exception) is returned, no need to check the (contents of the
    // dataObject).
    return true;
  }

  @Override
  protected void handleMessage(
      final MessageMetadata deviceMessageMetadata,
      final ResponseMessage responseMessage,
      final OsgpException osgpException) {

    this.configurationService.handleSetKeyOnGMeterResponse(
        deviceMessageMetadata, responseMessage.getResult(), osgpException);
  }
}
