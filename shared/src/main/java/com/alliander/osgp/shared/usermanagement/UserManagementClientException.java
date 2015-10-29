/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.shared.usermanagement;

public class UserManagementClientException extends Exception {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4869922322193170605L;

    public UserManagementClientException(final String message) {
        super(message);
    }

    public UserManagementClientException(final String message, final Throwable t) {
        super(message, t);
    }
}
