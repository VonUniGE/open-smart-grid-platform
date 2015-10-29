/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.dto.valueobjects;

import java.io.Serializable;
import java.util.List;

public class EventNotificationMessageDataContainer implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4707772459625804068L;

    private List<EventNotificationType> eventNotifications;

    public EventNotificationMessageDataContainer(final List<EventNotificationType> eventNotifications) {
        this.eventNotifications = eventNotifications;
    }

    public List<EventNotificationType> getEventNotifications() {
        return this.eventNotifications;
    }
}
