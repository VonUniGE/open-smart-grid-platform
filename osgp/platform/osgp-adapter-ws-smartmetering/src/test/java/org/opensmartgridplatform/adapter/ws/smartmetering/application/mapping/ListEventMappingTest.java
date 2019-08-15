/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package org.opensmartgridplatform.adapter.ws.smartmetering.application.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.Event;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.EventType;

public class ListEventMappingTest {

    private static final String NUMBER_OF_EVENTS = "number of events";
    private static final String EVENT_CODE_WITH_MAPPING_OF = "eventCode with mapping of ";
    private static final String EVENT_COUNTER_WITH_MAPPING_OF = "eventCounter with mapping of ";
    private static final String TIMESTAMP_WITH_MAPPING_OF = "timestamp with mapping of ";

    private ManagementMapper managementMapper = new ManagementMapper();
    private static final Integer EVENT_CODE = 10;
    private static final Integer EVENT_COUNTER = 1;

    private static final List<Event> STANDARD_EVENTS = Arrays
            .asList(new Event(DateTime.now(), EventType.EVENTLOG_CLEARED.getEventCode(), null),
                    new Event(DateTime.now(), EventType.POWER_FAILURE.getEventCode(), null),
                    new Event(DateTime.now(), EventType.POWER_RETURNED.getEventCode(), null),
                    new Event(DateTime.now(), EventType.CLOCK_UPDATE.getEventCode(), null),
                    new Event(DateTime.now(), EventType.CLOCK_ADJUSTED_OLD_TIME.getEventCode(), null),
                    new Event(DateTime.now(), EventType.CLOCK_ADJUSTED_NEW_TIME.getEventCode(), null),
                    new Event(DateTime.now(), EventType.CLOCK_INVALID.getEventCode(), null),
                    new Event(DateTime.now(), EventType.REPLACE_BATTERY.getEventCode(), null),
                    new Event(DateTime.now(), EventType.BATTERY_VOLTAGE_LOW.getEventCode(), null),
                    new Event(DateTime.now(), EventType.TARIFF_ACTIVATED.getEventCode(), null),
                    new Event(DateTime.now(), EventType.ERROR_REGISTER_CLEARED.getEventCode(), null),
                    new Event(DateTime.now(), EventType.ALARM_REGISTER_CLEARED.getEventCode(), null),
                    new Event(DateTime.now(), EventType.HARDWARE_ERROR_PROGRAM_MEMORY.getEventCode(), null),
                    new Event(DateTime.now(), EventType.HARDWARE_ERROR_RAM.getEventCode(), null),
                    new Event(DateTime.now(), EventType.HARDWARE_ERROR_NV_MEMORY.getEventCode(), null),
                    new Event(DateTime.now(), EventType.WATCHDOG_ERROR.getEventCode(), null),
                    new Event(DateTime.now(), EventType.HARDWARE_ERROR_MEASUREMENT_SYSTEM.getEventCode(), null),
                    new Event(DateTime.now(), EventType.FIRMWARE_READY_FOR_ACTIVATION.getEventCode(), null),
                    new Event(DateTime.now(), EventType.FIRMWARE_ACTIVATED.getEventCode(), null),
                    new Event(DateTime.now(), EventType.PASSIVE_TARIFF_UPDATED.getEventCode(), null),
                    new Event(DateTime.now(), EventType.SUCCESSFUL_SELFCHECK_AFTER_FIRMWARE_UPDATE.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_231.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_232.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_233.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_234.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_235.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_236.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_237.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_238.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_239.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_240.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_241.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_242.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_243.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_244.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_245.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_246.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_247.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_248.getEventCode(), null),
                    new Event(DateTime.now(), EventType.MANUFACTURER_SPECIFIC_249.getEventCode(), null));

    private static final List<Event> FRAUD_DETECTION_EVENTS = Arrays.asList(
            new Event(DateTime.now(), EventType.TERMINAL_COVER_REMOVED.getEventCode(), null),
            new Event(DateTime.now(), EventType.TERMINAL_COVER_CLOSED.getEventCode(), null),
            new Event(DateTime.now(), EventType.STRONG_DC_FIELD_DETECTED.getEventCode(), null),
            new Event(DateTime.now(), EventType.NO_STRONG_DC_FIELD_ANYMORE.getEventCode(), null),
            new Event(DateTime.now(), EventType.METER_COVER_REMOVED.getEventCode(), null),
            new Event(DateTime.now(), EventType.METER_COVER_CLOSED.getEventCode(), null),
            new Event(DateTime.now(), EventType.FAILED_LOGIN_ATTEMPT.getEventCode(), null),
            new Event(DateTime.now(), EventType.CONFIGURATION_CHANGE.getEventCode(), null));

    private static final List<Event> COMMUNICATION_SESSIONS_EVENTS = Arrays.asList(
            new Event(DateTime.now(), EventType.METROLOGICAL_MAINTENANCE.getEventCode(), 0),
            new Event(DateTime.now(), EventType.TECHNICAL_MAINTENANCE.getEventCode(), 0),
            new Event(DateTime.now(), EventType.RETRIEVE_METER_READINGS_E.getEventCode(), 0),
            new Event(DateTime.now(), EventType.RETRIEVE_METER_READINGS_G.getEventCode(), 1),
            new Event(DateTime.now(), EventType.RETRIEVE_INTERVAL_DATA_E.getEventCode(), 3754),
            new Event(DateTime.now(), EventType.RETRIEVE_INTERVAL_DATA_G.getEventCode(), 65535));

    private static final List<Event> M_BUS_EVENTS = Arrays.asList(
            new Event(DateTime.now(), EventType.COMMUNICATION_ERROR_M_BUS_CHANNEL_1.getEventCode(), null),
            new Event(DateTime.now(), EventType.COMMUNICATION_OK_M_BUS_CHANNEL_1.getEventCode(), null),
            new Event(DateTime.now(), EventType.REPLACE_BATTERY_M_BUS_CHANNEL_1.getEventCode(), null),
            new Event(DateTime.now(), EventType.FRAUD_ATTEMPT_M_BUS_CHANNEL_1.getEventCode(), null),
            new Event(DateTime.now(), EventType.CLOCK_ADJUSTED_M_BUS_CHANNEL_1.getEventCode(), null),
            new Event(DateTime.now(), EventType.NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_1.getEventCode(), null),
            new Event(DateTime.now(), EventType.PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_1.getEventCode(), null),
            new Event(DateTime.now(), EventType.COMMUNICATION_ERROR_M_BUS_CHANNEL_2.getEventCode(), null),
            new Event(DateTime.now(), EventType.COMMUNICATION_OK_M_BUS_CHANNEL_2.getEventCode(), null),
            new Event(DateTime.now(), EventType.REPLACE_BATTERY_M_BUS_CHANNEL_2.getEventCode(), null),
            new Event(DateTime.now(), EventType.FRAUD_ATTEMPT_M_BUS_CHANNEL_2.getEventCode(), null),
            new Event(DateTime.now(), EventType.CLOCK_ADJUSTED_M_BUS_CHANNEL_2.getEventCode(), null),
            new Event(DateTime.now(), EventType.NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_2.getEventCode(), null),
            new Event(DateTime.now(), EventType.PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_2.getEventCode(), null),
            new Event(DateTime.now(), EventType.COMMUNICATION_ERROR_M_BUS_CHANNEL_3.getEventCode(), null),
            new Event(DateTime.now(), EventType.COMMUNICATION_OK_M_BUS_CHANNEL_3.getEventCode(), null),
            new Event(DateTime.now(), EventType.REPLACE_BATTERY_M_BUS_CHANNEL_3.getEventCode(), null),
            new Event(DateTime.now(), EventType.FRAUD_ATTEMPT_M_BUS_CHANNEL_3.getEventCode(), null),
            new Event(DateTime.now(), EventType.CLOCK_ADJUSTED_M_BUS_CHANNEL_3.getEventCode(), null),
            new Event(DateTime.now(), EventType.NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_3.getEventCode(), null),
            new Event(DateTime.now(), EventType.PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_3.getEventCode(), null),
            new Event(DateTime.now(), EventType.COMMUNICATION_ERROR_M_BUS_CHANNEL_4.getEventCode(), null),
            new Event(DateTime.now(), EventType.COMMUNICATION_OK_M_BUS_CHANNEL_4.getEventCode(), null),
            new Event(DateTime.now(), EventType.REPLACE_BATTERY_M_BUS_CHANNEL_4.getEventCode(), null),
            new Event(DateTime.now(), EventType.FRAUD_ATTEMPT_M_BUS_CHANNEL_4.getEventCode(), null),
            new Event(DateTime.now(), EventType.CLOCK_ADJUSTED_M_BUS_CHANNEL_4.getEventCode(), null),
            new Event(DateTime.now(), EventType.NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_4.getEventCode(), null),
            new Event(DateTime.now(), EventType.PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_4.getEventCode(), null));

    @Test
    public void testMappingForListOfStandardEvents() {

        final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event> mappedStandardEvents = this.managementMapper
                .mapAsList(STANDARD_EVENTS, org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

        this.checkEventsMappedToWsSchema(STANDARD_EVENTS, mappedStandardEvents);

        final List<Event> standardEvents = this.managementMapper.mapAsList(mappedStandardEvents, Event.class);

        this.checkEventsMappedFromWsSchema(mappedStandardEvents, standardEvents);
    }

    @Test
    public void testMappingForListOfFraudDetectionEvents() {

        final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event> mappedFraudDetectionEvents = this.managementMapper
                .mapAsList(FRAUD_DETECTION_EVENTS,
                        org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

        this.checkEventsMappedToWsSchema(FRAUD_DETECTION_EVENTS, mappedFraudDetectionEvents);

        final List<Event> fraudDetectionEvents = this.managementMapper.mapAsList(mappedFraudDetectionEvents,
                Event.class);

        this.checkEventsMappedFromWsSchema(mappedFraudDetectionEvents, fraudDetectionEvents);
    }

    @Test
    public void testMappingForListOfCommunicationSessionEvents() {

        final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event> mappedCommunicationSessionEvents = this.managementMapper
                .mapAsList(COMMUNICATION_SESSIONS_EVENTS,
                        org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

        this.checkEventsMappedToWsSchema(COMMUNICATION_SESSIONS_EVENTS, mappedCommunicationSessionEvents);

        final List<Event> communicationSessionEvents = this.managementMapper.mapAsList(mappedCommunicationSessionEvents,
                Event.class);

        this.checkEventsMappedFromWsSchema(mappedCommunicationSessionEvents, communicationSessionEvents);
    }

    @Test
    public void testMappingForListOfMBusEvents() {

        final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event> mappedMBusEvents = this.managementMapper
                .mapAsList(M_BUS_EVENTS, org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

        this.checkEventsMappedToWsSchema(M_BUS_EVENTS, mappedMBusEvents);

        final List<Event> mBusEvents = this.managementMapper.mapAsList(mappedMBusEvents, Event.class);

        this.checkEventsMappedFromWsSchema(mappedMBusEvents, mBusEvents);
    }

    private void checkEventsMappedToWsSchema(final List<Event> originalEvents,
            final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event> mappedEvents) {

        assertEquals(NUMBER_OF_EVENTS, originalEvents.size(), mappedEvents.size());

        for (int i = 0; i < originalEvents.size(); i++) {
            final Event originalEvent = originalEvents.get(i);
            final org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event mappedEvent = mappedEvents.get(i);

            assertEquals(EVENT_CODE_WITH_MAPPING_OF + originalEvent, originalEvent.getEventCode().intValue(),
                    EventType.valueOf(mappedEvent.getEventType().name()).getEventCode());

            assertEquals(EVENT_COUNTER_WITH_MAPPING_OF + originalEvent, originalEvent.getEventCounter(),
                    mappedEvent.getEventCounter());

            assertEquals(TIMESTAMP_WITH_MAPPING_OF + originalEvent, originalEvent.getTimestamp().toString(),
                    new DateTime(mappedEvent.getTimestamp().toGregorianCalendar()).toString());
        }
    }

    private void checkEventsMappedFromWsSchema(
            final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event> originalEvents,
            final List<Event> mappedEvents) {

        assertEquals(NUMBER_OF_EVENTS, originalEvents.size(), mappedEvents.size());

        for (int i = 0; i < originalEvents.size(); i++) {
            final org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event originalEvent = originalEvents
                    .get(i);
            final Event mappedEvent = mappedEvents.get(i);

            assertEquals(EVENT_CODE_WITH_MAPPING_OF + originalEvent.getEventType(),
                    EventType.valueOf(originalEvent.getEventType().name()).getEventCode(),
                    mappedEvent.getEventCode().intValue());

            assertEquals(EVENT_COUNTER_WITH_MAPPING_OF + originalEvent.getEventType(),
                    originalEvent.getEventCounter(), mappedEvent.getEventCounter());

            assertEquals(TIMESTAMP_WITH_MAPPING_OF + originalEvent.getEventType(),
                    new DateTime(originalEvent.getTimestamp().toGregorianCalendar()).toString(),
                    mappedEvent.getTimestamp().toString());
        }
    }

    /**
     * Tests if mapping a List, typed to Event, succeeds if the List is filled.
     */
    @Test
    public void testFilledListEventMapping() {

        // build test data
        final DateTime timestamp = new DateTime();
        final Event event = new Event(timestamp, EVENT_CODE, EVENT_COUNTER);
        final List<Event> listOriginal = new ArrayList<>();
        listOriginal.add(event);

        // actual mapping
        final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event> listMapped = this.managementMapper
                .mapAsList(listOriginal, org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

        // check mapping
        assertNotNull(listMapped);
        assertNotNull(listMapped.get(0));
        assertNotNull(listMapped.get(0).getEventCounter());
        assertNotNull(listMapped.get(0).getEventType());
        assertNotNull(listMapped.get(0).getTimestamp());

        assertEquals(timestamp.getYear(), listMapped.get(0).getTimestamp().getYear());
        assertEquals(timestamp.getMonthOfYear(), listMapped.get(0).getTimestamp().getMonth());
        assertEquals(timestamp.getDayOfMonth(), listMapped.get(0).getTimestamp().getDay());

        assertEquals(timestamp.getHourOfDay(), listMapped.get(0).getTimestamp().getHour());
        assertEquals(timestamp.getMinuteOfHour(), listMapped.get(0).getTimestamp().getMinute());
        assertEquals(timestamp.getSecondOfMinute(), listMapped.get(0).getTimestamp().getSecond());

        assertEquals((int) EVENT_CODE, listMapped.get(0).getEventType().ordinal());

        assertEquals(EVENT_COUNTER, listMapped.get(0).getEventCounter());
    }

    /**
     * Tests if mapping a List, typed to Event, succeeds if the List is empty.
     */
    @Test
    public void testEmptyListEventMapping() {

        // build test data
        final List<Event> listOriginal = new ArrayList<>();

        // actual mapping
        final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event> listMapped = this.managementMapper
                .mapAsList(listOriginal, org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

        // check mapping
        assertNotNull(listMapped);
        assertTrue(listMapped.isEmpty());
    }

    /**
     * Tests if mapping a List, typed to Event, succeeds if the List is null.
     */
    @Test(expected = NullPointerException.class)
    public void testNullListEventMapping() {

        // build test data
        final List<Event> listOriginal = null;

        // actual mapping
        this.managementMapper.mapAsList(listOriginal,
                org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

    }
}
