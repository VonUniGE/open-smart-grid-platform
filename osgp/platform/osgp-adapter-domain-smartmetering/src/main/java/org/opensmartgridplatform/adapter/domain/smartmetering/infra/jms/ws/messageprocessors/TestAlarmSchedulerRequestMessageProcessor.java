/*
 * Copyright 2022 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.ws.messageprocessors;

import org.opensmartgridplatform.adapter.domain.smartmetering.application.services.AdhocService;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.BaseRequestMessageProcessor;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.TestAlarmSchedulerRequestData;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.opensmartgridplatform.shared.infra.jms.MessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.MessageProcessorMap;
import org.opensmartgridplatform.shared.infra.jms.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TestAlarmSchedulerRequestMessageProcessor extends BaseRequestMessageProcessor {

  @Autowired
  @Qualifier("domainSmartMeteringAdhocService")
  private AdhocService adhocService;

  @Autowired
  protected TestAlarmSchedulerRequestMessageProcessor(
      @Qualifier("domainSmartMeteringInboundWebServiceRequestsMessageProcessorMap")
          final MessageProcessorMap messageProcessorMap) {
    super(messageProcessorMap, MessageType.SCHEDULE_TEST_ALARM);
  }

  @Override
  protected void handleMessage(final MessageMetadata deviceMessageMetadata, final Object dataObject)
      throws FunctionalException {

    final TestAlarmSchedulerRequestData testAlarmSchedulerRequestData =
        (TestAlarmSchedulerRequestData) dataObject;

    this.adhocService.scheduleTestAlarm(deviceMessageMetadata, testAlarmSchedulerRequestData);
  }
}
