/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands;

import org.openmuc.openiec61850.Fc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alliander.osgp.adapter.protocol.iec61850.exceptions.NodeWriteException;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.helper.DataAttribute;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.helper.DeviceConnection;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.helper.LogicalDevice;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.helper.LogicalNode;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.helper.NodeContainer;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.helper.SubDataAttribute;

public class Iec61850ClearReportCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iec61850ClearReportCommand.class);

    public void clearReportOnDevice(final DeviceConnection deviceConnection) throws NodeWriteException {
        final NodeContainer reporting = deviceConnection.getFcModelNode(LogicalDevice.LIGHTING,
                LogicalNode.LOGICAL_NODE_ZERO, DataAttribute.REPORTING, Fc.BR);

        reporting.writeBoolean(SubDataAttribute.ENABLE_REPORTING, false);
        reporting.writeBoolean(SubDataAttribute.PURGE_BUF, true);
        LOGGER.info("Cleared event buffer for device: {}", deviceConnection.getDeviceIdentification());
    }
}
