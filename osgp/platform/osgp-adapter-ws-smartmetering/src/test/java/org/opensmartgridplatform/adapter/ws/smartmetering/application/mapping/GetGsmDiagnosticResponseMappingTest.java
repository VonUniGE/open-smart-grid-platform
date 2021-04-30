/*
 * Copyright 2021 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.smartmetering.application.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.AdjacentCellInfo;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.BitErrorRateType;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.CellInfo;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.CircuitSwitchedStatusType;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.GetGsmDiagnosticResponseData;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.ModemRegistrationStatusType;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.PacketSwitchedStatusType;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.SignalQualityType;

class GetGsmDiagnosticResponseMappingTest {

  private final ManagementMapper managementMapper = new ManagementMapper();

  @Test
  void shouldConvertGetGsmDiagnosticResponse() {
    final GetGsmDiagnosticResponseData source = this.newGetGsmDiagnosticResponseData();

    final org.opensmartgridplatform.adapter.ws.schema.smartmetering.bundle.GetGsmDiagnosticResponse
        target =
            this.managementMapper.map(
                source,
                org.opensmartgridplatform.adapter.ws.schema.smartmetering.bundle
                    .GetGsmDiagnosticResponse.class);

    // Assert
    assertThat(target.getOperator()).isEqualTo(source.getOperator());
    assertThat(target.getModemRegistrationStatus().name())
        .isEqualTo(source.getModemRegistrationStatus().name());
    assertThat(target.getCircuitSwitchedStatus().name())
        .isEqualTo(source.getCircuitSwitchedStatus().name());
    assertThat(target.getPacketSwitchedStatus().name())
        .isEqualTo(source.getPacketSwitchedStatus().name());
    final org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.CellInfo
        targetCellInfo = target.getCellInfo();
    final CellInfo sourceCellInfo = source.getCellInfo();
    assertThat(targetCellInfo.getCellId()).isEqualTo(sourceCellInfo.getCellId());
    assertThat(targetCellInfo.getLocationId()).isEqualTo(sourceCellInfo.getLocationId());
    assertThat(targetCellInfo.getSignalQuality().name())
        .isEqualTo(sourceCellInfo.getSignalQuality().name());
    assertThat(targetCellInfo.getBitErrorRate().name())
        .isEqualTo(sourceCellInfo.getBitErrorRate().name());
    assertThat(targetCellInfo.getMobileCountryCode())
        .isEqualTo(sourceCellInfo.getMobileCountryCode());
    assertThat(targetCellInfo.getMobileNetworkCode())
        .isEqualTo(sourceCellInfo.getMobileNetworkCode());
    assertThat(targetCellInfo.getChannelNumber()).isEqualTo(sourceCellInfo.getChannelNumber());
    final List<
            org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.AdjacentCellInfo>
        targetAdjacentCellInfos = target.getAdjacentCells();
    final List<AdjacentCellInfo> sourceAdjacentCellInfos = source.getAdjacentCells();
    assertThat(targetAdjacentCellInfos.size()).isEqualTo(sourceAdjacentCellInfos.size());
    assertThat(targetAdjacentCellInfos.get(0).getCellId())
        .isEqualTo(sourceAdjacentCellInfos.get(0).getCellId());
    assertThat(targetAdjacentCellInfos.get(0).getSignalQuality().name())
        .isEqualTo(sourceAdjacentCellInfos.get(0).getSignalQuality().name());
    assertThat(target.getCaptureTime().toGregorianCalendar().getTime().getTime())
        .isEqualTo(source.getCaptureTime().getTime());
  }

  private GetGsmDiagnosticResponseData newGetGsmDiagnosticResponseData() {
    final CellInfo cellInfo =
        new CellInfo(
            "cellId".getBytes(),
            "locationId".getBytes(),
            SignalQualityType.MINUS_61_DBM,
            BitErrorRateType.RXQUAL_4,
            31L,
            42L,
            1L);

    final AdjacentCellInfo adjacentCellInfo =
        new AdjacentCellInfo("adjacentCellId".getBytes(), SignalQualityType.MINUS_61_DBM);

    return new GetGsmDiagnosticResponseData(
        "operator",
        ModemRegistrationStatusType.REGISTERED_ROAMING,
        CircuitSwitchedStatusType.ACTIVE,
        PacketSwitchedStatusType.GPRS,
        cellInfo,
        Collections.singletonList(adjacentCellInfo),
        new Date());
  }
}
