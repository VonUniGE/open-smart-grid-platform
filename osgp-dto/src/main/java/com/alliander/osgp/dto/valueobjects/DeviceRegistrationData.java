/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.dto.valueobjects;

import java.io.Serializable;

public class DeviceRegistrationData implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -5264884250167476931L;

    private String ipAddress;
    private String deviceType;
    private boolean hasSchedule;

    public DeviceRegistrationData(final String ipAddress, final String deviceType, final boolean hasSchedule) {
        this.ipAddress = ipAddress;
        this.deviceType = deviceType;
        this.hasSchedule = hasSchedule;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public boolean isHasSchedule() {
        return this.hasSchedule;
    }
}
