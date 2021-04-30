/*
 * Copyright 2021 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.domain.core.valueobjects.smartmetering;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GetGsmDiagnosticResponseData extends ActionResponse implements Serializable {

  private static final long serialVersionUID = 4966055518516878043L;

  private final String operator;

  private final ModemRegistrationStatusType modemRegistrationStatus;

  private final CircuitSwitchedStatusType circuitSwitchedStatus;

  private final PacketSwitchedStatusType packetSwitchedStatus;

  private final CellInfo cellInfo;

  private final List<AdjacentCellInfo> adjacentCells;

  private final Date captureTime;

  public GetGsmDiagnosticResponseData(
      final String operator,
      final ModemRegistrationStatusType modemRegistrationStatus,
      final CircuitSwitchedStatusType circuitSwitchedStatus,
      final PacketSwitchedStatusType packetSwitchedStatus,
      final CellInfo cellInfo,
      final List<AdjacentCellInfo> adjacentCells,
      final Date captureTime) {
    super();
    this.operator = operator;
    this.modemRegistrationStatus = modemRegistrationStatus;
    this.circuitSwitchedStatus = circuitSwitchedStatus;
    this.packetSwitchedStatus = packetSwitchedStatus;
    this.cellInfo = cellInfo;
    this.adjacentCells = adjacentCells;
    this.captureTime = captureTime;
  }

  public String getOperator() {
    return this.operator;
  }

  public ModemRegistrationStatusType getModemRegistrationStatus() {
    return this.modemRegistrationStatus;
  }

  public CircuitSwitchedStatusType getCircuitSwitchedStatus() {
    return this.circuitSwitchedStatus;
  }

  public PacketSwitchedStatusType getPacketSwitchedStatus() {
    return this.packetSwitchedStatus;
  }

  public CellInfo getCellInfo() {
    return this.cellInfo;
  }

  public List<AdjacentCellInfo> getAdjacentCells() {
    return this.adjacentCells;
  }

  public Date getCaptureTime() {
    return this.captureTime;
  }
}
