/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.smartmetering.glue.steps.ws.smartmetering.smartmeteringinstallation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgp.adapter.protocol.dlms.simulator.trigger.SimulatorTriggerClient;
import org.osgp.adapter.protocol.dlms.simulator.trigger.SimulatorTriggerClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.cucumber.platform.smartmetering.glue.steps.ws.smartmetering.AbstractSmartMeteringSteps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class DeviceSimulatorSteps extends AbstractSmartMeteringSteps {

    @Autowired
    private SimulatorTriggerClient simulatorTriggerClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceSimulatorSteps.class);

    private static final String XPATH_MATCHER_RESULT_DECODED_MESSAGE = "\\d";

    public void clearDlmsAttributeValues() {
        try {
            this.simulatorTriggerClient.clearDlmsAttributeValues();
        } catch (final SimulatorTriggerClientException stce) {
            LOGGER.error("Error calling simulatorTriggerClient.clearDlmsAttributeValues()", stce);
            fail("Error clearing DLMS attribute values for simulator");
        }
    }

    @Given("^device simulation of \"([^\"]*)\" with classid (\\d+) obiscode \"([^\"]*)\" and attributes$")
    public void deviceSimulateWithClassidObiscodeAndAttributes(final String deviceIdentification, final int classId,
            final String obisCode, final Map<String, String> settings) throws Throwable {

        /*
         * Currently the first argument: deviceIdentification, is not used yet,
         * because in all scenarios created so far that make use of dynamic
         * device simulator properties, only one meter was read. In future
         * scenarios it may be possible that within a single scenario two (or
         * more) meters should be read, and that both meters should read their
         * own set of dynamic properties. In that case the deviceIdentification
         * parameter can be used to make this distinction.
         */

        try {
            this.simulatorTriggerClient.setDlmsAttributeValues(classId, obisCode, settings);
        } catch (final SimulatorTriggerClientException stce) {
            LOGGER.error("Error while setting DLMS attribute values for classId: " + classId + ", obisCode: " + obisCode
                    + " and settings: " + settings + " with SimulatorTriggerClient", stce);
            fail("Error setting DLMS attribute values for simulator");
        }
    }

    @Then("^device simulation of 'TEST(\\d+)' with classid (\\d+) obiscode \"([^\"]*)\" retrieves the attributes$")
    public void deviceSimulationOfTESTWithClassidObiscodeRetrievesTheAttributes(final String deviceIdentification,
            final int classId, final String obisCode, final Map<String, String> settings) throws Throwable {
        String attribute = null;
        try {
            attribute = this.simulatorTriggerClient.getDlmsAttributeValues(classId, obisCode);

        } catch (final SimulatorTriggerClientException stce) {
            LOGGER.error("Error while getting DLMS attribute values");
            fail("Error setting DLMS attribute values for simulator");
        }

        for (final Map.Entry<String, String> setting : settings.entrySet()) {
            final Pattern pattern = Pattern.compile("(" + setting.getKey() + "=" + setting.getValue() + ")");
            final Matcher matcher = pattern.matcher(attribute);
            assertTrue("Pattern not found", matcher.find());
        }
    }
}
