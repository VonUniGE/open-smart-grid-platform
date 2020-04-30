package util;

import java.io.Serializable;

import org.opensmartgridplatform.adapter.protocol.dlms.application.services.MonitoringService;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.factories.DlmsConnectionManager;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.GetPowerQualityProfileRequestDataDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.GetPowerQualityProfileResponseDto;

/**
 * Copyright 2019 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
public class MockMonitoringService extends MonitoringService {

    public MockMonitoringService(/*GetPeriodicMeterReadsCommandExecutor getPeriodicMeterReadsCommandExecutor,
            GetPeriodicMeterReadsGasCommandExecutor getPeriodicMeterReadsGasCommandExecutor,
            GetActualMeterReadsCommandExecutor actualMeterReadsCommandExecutor,
            GetActualMeterReadsGasCommandExecutor actualMeterReadsGasCommandExecutor,
            ReadAlarmRegisterCommandExecutor readAlarmRegisterCommandExecutor,
            GetPowerQualityProfileCommandExecutor getPowerQualityProfileCommandExecutor,
            ClearAlarmRegisterCommandExecutor clearAlarmRegisterCommandExecutor*/) {
        super(null, null, null, null, null, null, null);
    }

    @Override
    public Serializable requestPowerQualityProfile(final DlmsConnectionManager conn, final DlmsDevice device,
            final GetPowerQualityProfileRequestDataDto powerQualityProfileRequestDataDto) {

        return new GetPowerQualityProfileResponseDto();
    }
}
