/*
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.smartmetering.application.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.Event;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.EventLogCategory;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.EventType;

public class ListEventMappingTest {

  private static final String NUMBER_OF_EVENTS = "number of events";
  private static final String EVENT_CODE_WITH_MAPPING_OF = "eventCode with mapping of ";
  private static final String EVENT_COUNTER_WITH_MAPPING_OF = "eventCounter with mapping of ";
  private static final String TIMESTAMP_WITH_MAPPING_OF = "timestamp with mapping of ";

  private static final EventType EVENT_TYPE = EventType.ERROR_REGISTER_CLEARED;
  private static final Integer EVENT_COUNTER = 1;
  private static final List<Event> STANDARD_EVENTS =
      Arrays.asList(
          new Event(
              DateTime.now(),
              EventType.EVENTLOG_CLEARED,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(), EventType.POWER_FAILURE, null, EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(), EventType.POWER_RETURNED, null, EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(), EventType.CLOCK_UPDATE, null, EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.CLOCK_ADJUSTED_OLD_TIME,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.CLOCK_ADJUSTED_NEW_TIME,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(), EventType.CLOCK_INVALID, null, EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(), EventType.REPLACE_BATTERY, null, EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.BATTERY_VOLTAGE_LOW,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.TARIFF_ACTIVATED,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.ERROR_REGISTER_CLEARED,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.ALARM_REGISTER_CLEARED,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.HARDWARE_ERROR_PROGRAM_MEMORY,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.HARDWARE_ERROR_RAM,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.HARDWARE_ERROR_NV_MEMORY,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(), EventType.WATCHDOG_ERROR, null, EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.HARDWARE_ERROR_MEASUREMENT_SYSTEM,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.FIRMWARE_READY_FOR_ACTIVATION,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.FIRMWARE_ACTIVATED,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.PASSIVE_TARIFF_UPDATED,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.SUCCESSFUL_SELFCHECK_AFTER_FIRMWARE_UPDATE,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_231,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_232,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_233,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_234,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_235,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_236,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_237,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_238,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_239,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_240,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_241,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_242,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_243,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_244,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_245,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_246,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_247,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_248,
              null,
              EventLogCategory.STANDARD_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.MANUFACTURER_SPECIFIC_249,
              null,
              EventLogCategory.STANDARD_EVENT_LOG));

  private static final List<Event> FRAUD_DETECTION_EVENTS =
      Arrays.asList(
          new Event(
              DateTime.now(),
              EventType.TERMINAL_COVER_REMOVED,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.TERMINAL_COVER_CLOSED,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.STRONG_DC_FIELD_DETECTED,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.NO_STRONG_DC_FIELD_ANYMORE,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.METER_COVER_REMOVED,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.METER_COVER_CLOSED,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.FAILED_LOGIN_ATTEMPT,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.CONFIGURATION_CHANGE,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.MODULE_COVER_OPENED,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.MODULE_COVER_CLOSED,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.MODULE_COVER_OPENED_ISKR,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG),
          new Event(
              DateTime.now(),
              EventType.MODULE_COVER_CLOSED_ISKR,
              null,
              EventLogCategory.FRAUD_DETECTION_LOG));

  private static final List<Event> COMMUNICATION_SESSIONS_EVENTS =
      Arrays.asList(
          new Event(
              DateTime.now(),
              EventType.METROLOGICAL_MAINTENANCE,
              0,
              EventLogCategory.COMMUNICATION_SESSION_LOG),
          new Event(
              DateTime.now(),
              EventType.TECHNICAL_MAINTENANCE,
              0,
              EventLogCategory.COMMUNICATION_SESSION_LOG),
          new Event(
              DateTime.now(),
              EventType.RETRIEVE_METER_READINGS_E,
              0,
              EventLogCategory.COMMUNICATION_SESSION_LOG),
          new Event(
              DateTime.now(),
              EventType.RETRIEVE_METER_READINGS_G,
              1,
              EventLogCategory.COMMUNICATION_SESSION_LOG),
          new Event(
              DateTime.now(),
              EventType.RETRIEVE_INTERVAL_DATA_E,
              3754,
              EventLogCategory.COMMUNICATION_SESSION_LOG),
          new Event(
              DateTime.now(),
              EventType.RETRIEVE_INTERVAL_DATA_G,
              65535,
              EventLogCategory.COMMUNICATION_SESSION_LOG));

  private static final List<Event> M_BUS_EVENTS =
      Arrays.asList(
          new Event(
              DateTime.now(),
              EventType.COMMUNICATION_ERROR_M_BUS_CHANNEL_1,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.COMMUNICATION_OK_M_BUS_CHANNEL_1,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.REPLACE_BATTERY_M_BUS_CHANNEL_1,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.FRAUD_ATTEMPT_M_BUS_CHANNEL_1,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.CLOCK_ADJUSTED_M_BUS_CHANNEL_1,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_1,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_1,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.COMMUNICATION_ERROR_M_BUS_CHANNEL_2,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.COMMUNICATION_OK_M_BUS_CHANNEL_2,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.REPLACE_BATTERY_M_BUS_CHANNEL_2,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.FRAUD_ATTEMPT_M_BUS_CHANNEL_2,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.CLOCK_ADJUSTED_M_BUS_CHANNEL_2,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_2,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_2,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.COMMUNICATION_ERROR_M_BUS_CHANNEL_3,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.COMMUNICATION_OK_M_BUS_CHANNEL_3,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.REPLACE_BATTERY_M_BUS_CHANNEL_3,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.FRAUD_ATTEMPT_M_BUS_CHANNEL_3,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.CLOCK_ADJUSTED_M_BUS_CHANNEL_3,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_3,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_3,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.COMMUNICATION_ERROR_M_BUS_CHANNEL_4,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.COMMUNICATION_OK_M_BUS_CHANNEL_4,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.REPLACE_BATTERY_M_BUS_CHANNEL_4,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.FRAUD_ATTEMPT_M_BUS_CHANNEL_4,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.CLOCK_ADJUSTED_M_BUS_CHANNEL_4,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_4,
              null,
              EventLogCategory.M_BUS_EVENT_LOG),
          new Event(
              DateTime.now(),
              EventType.PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_4,
              null,
              EventLogCategory.M_BUS_EVENT_LOG));

  private final ManagementMapper managementMapper = new ManagementMapper();

  private void checkEventsMappedFromWsSchema(
      final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event>
          originalEvents,
      final List<Event> mappedEvents) {

    assertThat(mappedEvents.size()).as(NUMBER_OF_EVENTS).isEqualTo(originalEvents.size());

    for (int i = 0; i < originalEvents.size(); i++) {
      final org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event
          originalEvent = originalEvents.get(i);
      final Event mappedEvent = mappedEvents.get(i);

      assertThat(mappedEvent.getEventCode().intValue())
          .as(EVENT_CODE_WITH_MAPPING_OF + originalEvent.getEventType())
          .isEqualTo(EventType.valueOf(originalEvent.getEventType().name()).getEventCode());

      assertThat(mappedEvent.getEventCounter())
          .as(EVENT_COUNTER_WITH_MAPPING_OF + originalEvent.getEventType())
          .isEqualTo(originalEvent.getEventCounter());

      assertThat(mappedEvent.getTimestamp().toString())
          .as(TIMESTAMP_WITH_MAPPING_OF + originalEvent.getEventType())
          .isEqualTo(new DateTime(originalEvent.getTimestamp().toGregorianCalendar()).toString());
    }
  }

  private void checkEventsMappedToWsSchema(
      final List<Event> originalEvents,
      final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event>
          mappedEvents) {

    assertThat(mappedEvents.size()).as(NUMBER_OF_EVENTS).isEqualTo(originalEvents.size());

    for (int i = 0; i < originalEvents.size(); i++) {
      final Event originalEvent = originalEvents.get(i);
      final org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event mappedEvent =
          mappedEvents.get(i);

      assertThat(EventType.valueOf(mappedEvent.getEventType().name()).getEventCode())
          .as(EVENT_CODE_WITH_MAPPING_OF + originalEvent)
          .isEqualTo(originalEvent.getEventCode().intValue());

      assertThat(mappedEvent.getEventCounter())
          .as(EVENT_COUNTER_WITH_MAPPING_OF + originalEvent)
          .isEqualTo(originalEvent.getEventCounter());

      assertThat(new DateTime(mappedEvent.getTimestamp().toGregorianCalendar()).toString())
          .as(TIMESTAMP_WITH_MAPPING_OF + originalEvent)
          .isEqualTo(originalEvent.getTimestamp().toString());
    }
  }

  /** Tests if mapping a List, typed to Event, succeeds if the List is empty. */
  @Test
  public void testEmptyListEventMapping() {

    // build test data
    final List<Event> listOriginal = new ArrayList<>();

    // actual mapping
    final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event>
        listMapped =
            this.managementMapper.mapAsList(
                listOriginal,
                org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

    // check mapping
    assertThat(listMapped).isNotNull();
    assertThat(listMapped.isEmpty()).isTrue();
  }

  /** Tests if mapping a List, typed to Event, succeeds if the List is filled. */
  @Test
  public void testFilledListEventMapping() {

    // build test data
    final DateTime timestamp = new DateTime();
    final Event event =
        new Event(timestamp, EVENT_TYPE, EVENT_COUNTER, EventLogCategory.STANDARD_EVENT_LOG);
    final List<Event> listOriginal = new ArrayList<>();
    listOriginal.add(event);

    // actual mapping
    final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event>
        listMapped =
            this.managementMapper.mapAsList(
                listOriginal,
                org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

    // check mapping
    assertThat(listMapped).isNotNull();
    assertThat(listMapped.get(0)).isNotNull();
    assertThat(listMapped.get(0).getEventCounter()).isNotNull();
    assertThat(listMapped.get(0).getEventType()).isNotNull();
    assertThat(listMapped.get(0).getTimestamp()).isNotNull();
    assertThat(listMapped.get(0).getEventLogCategory()).isNotNull();

    assertThat(listMapped.get(0).getTimestamp().getYear()).isEqualTo(timestamp.getYear());
    assertThat(listMapped.get(0).getTimestamp().getMonth()).isEqualTo(timestamp.getMonthOfYear());
    assertThat(listMapped.get(0).getTimestamp().getDay()).isEqualTo(timestamp.getDayOfMonth());

    assertThat(listMapped.get(0).getTimestamp().getHour()).isEqualTo(timestamp.getHourOfDay());
    assertThat(listMapped.get(0).getTimestamp().getMinute()).isEqualTo(timestamp.getMinuteOfHour());
    assertThat(listMapped.get(0).getTimestamp().getSecond())
        .isEqualTo(timestamp.getSecondOfMinute());

    assertThat(listMapped.get(0).getEventType().name()).isEqualTo(EVENT_TYPE.name());
    assertThat(listMapped.get(0).getEventCode()).isEqualTo(EVENT_TYPE.getEventCode());
    assertThat(listMapped.get(0).getEventCounter()).isEqualTo(EVENT_COUNTER);
    assertThat(listMapped.get(0).getEventLogCategory())
        .isEqualTo(
            org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.EventLogCategory
                .STANDARD_EVENT_LOG);
  }

  @Test
  public void testMappingForListOfCommunicationSessionEvents() {

    final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event>
        mappedCommunicationSessionEvents =
            this.managementMapper.mapAsList(
                COMMUNICATION_SESSIONS_EVENTS,
                org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

    this.checkEventsMappedToWsSchema(
        COMMUNICATION_SESSIONS_EVENTS, mappedCommunicationSessionEvents);

    final List<Event> communicationSessionEvents =
        this.managementMapper.mapAsList(mappedCommunicationSessionEvents, Event.class);

    this.checkEventsMappedFromWsSchema(
        mappedCommunicationSessionEvents, communicationSessionEvents);
  }

  @Test
  public void testMappingForListOfFraudDetectionEvents() {

    final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event>
        mappedFraudDetectionEvents =
            this.managementMapper.mapAsList(
                FRAUD_DETECTION_EVENTS,
                org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

    this.checkEventsMappedToWsSchema(FRAUD_DETECTION_EVENTS, mappedFraudDetectionEvents);

    final List<Event> fraudDetectionEvents =
        this.managementMapper.mapAsList(mappedFraudDetectionEvents, Event.class);

    this.checkEventsMappedFromWsSchema(mappedFraudDetectionEvents, fraudDetectionEvents);
  }

  @Test
  public void testMappingForListOfMBusEvents() {

    final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event>
        mappedMBusEvents =
            this.managementMapper.mapAsList(
                M_BUS_EVENTS,
                org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

    this.checkEventsMappedToWsSchema(M_BUS_EVENTS, mappedMBusEvents);

    final List<Event> mBusEvents = this.managementMapper.mapAsList(mappedMBusEvents, Event.class);

    this.checkEventsMappedFromWsSchema(mappedMBusEvents, mBusEvents);
  }

  @Test
  public void testMappingForListOfStandardEvents() {

    final List<org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event>
        mappedStandardEvents =
            this.managementMapper.mapAsList(
                STANDARD_EVENTS,
                org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);

    this.checkEventsMappedToWsSchema(STANDARD_EVENTS, mappedStandardEvents);

    final List<Event> standardEvents =
        this.managementMapper.mapAsList(mappedStandardEvents, Event.class);

    this.checkEventsMappedFromWsSchema(mappedStandardEvents, standardEvents);
  }

  /** Tests if mapping a List, typed to Event, succeeds if the List is null. */
  @Test
  public void testNullListEventMapping() {
    // build test data
    final List<Event> listOriginal = null;

    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(
            () -> {
              // actual mapping
              this.managementMapper.mapAsList(
                  listOriginal,
                  org.opensmartgridplatform.adapter.ws.schema.smartmetering.management.Event.class);
            });
  }
}
