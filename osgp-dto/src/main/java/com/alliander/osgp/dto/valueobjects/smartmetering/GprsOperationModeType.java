/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.dto.valueobjects.smartmetering;

public enum GprsOperationModeType {

    ALWAYS_ON,
    TRIGGERED;

    public String value() {
        return name();
    }

    public static GprsOperationModeType fromValue(String v) {
        return valueOf(v);
    }
}
