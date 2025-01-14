@SmartMetering @Platform @NightlyBuildOnly
Feature: SmartMetering Installation - Decouple M-Bus Device
  As a grid operator
  I want to be able to decouple an M-Bus device from a smart meter

  Scenario: Decouple G-meter from E-meter
    Given a dlms device
      | DeviceIdentification | TEST1024000000001 |
      | DeviceType           | SMART_METER_E     |
    And a dlms device
      | DeviceIdentification        | TESTG102400000001 |
      | DeviceType                  | SMART_METER_G     |
      | GatewayDeviceIdentification | TEST1024000000001 |
      | Channel                     |                 1 |
    When the Decouple G-meter "TESTG102400000001" from E-meter "TEST1024000000001" request is received
    Then the Decouple response is "OK"
    And the G-meter "TESTG102400000001" is Decoupled from device "TEST1024000000001"
    And the channel of device "TESTG102400000001" is cleared

  Scenario: Decouple unknown G-meter from E-meter
    Given a dlms device
      | DeviceIdentification | TEST1024000000001 |
      | DeviceType           | SMART_METER_E     |
    When the Decouple G-meter "TESTunknownDevice" from E-meter "TEST1024000000001" request is received
    Then retrieving the Decouple response results in an exception
    And a SOAP fault should have been returned
      | Code    |            201 |
      | Message | UNKNOWN_DEVICE |

  Scenario: Decouple G-meter from unknown E-meter
    Given a dlms device
      | DeviceIdentification | TESTG102400000001 |
      | DeviceType           | SMART_METER_G     |
    When the Decouple G-meter "TESTG102400000001" from E-meter "TEST102400unknown" request is received for an unknown gateway
    Then a SOAP fault should have been returned
      | Code    |            201 |
      | Message | UNKNOWN_DEVICE |

  Scenario: Decouple inactive G-meter from E-meter
    Given a dlms device
      | DeviceIdentification | TEST1024000000001 |
      | DeviceType           | SMART_METER_E     |
    And a dlms device
      | DeviceIdentification        | TESTG102400000001 |
      | DeviceType                  | SMART_METER_G     |
      | GatewayDeviceIdentification | TEST1024000000001 |
      | Channel                     |                 1 |
      | DeviceLifecycleStatus       | NEW_IN_INVENTORY  |
    When the Decouple G-meter "TESTG102400000001" from E-meter "TEST1024000000001" request is received
    Then retrieving the Decouple response results in an exception
    And a SOAP fault should have been returned
      | Code    |             207 |
      | Message | INACTIVE_DEVICE |

  Scenario: Decouple coupled G-meter "TESTG101205673117" from E-meter "TEST1024000000001"
    Given a dlms device
      | DeviceIdentification | TEST1024000000001 |
      | DeviceType           | SMART_METER_E     |
    And a dlms device
      | DeviceIdentification           | TESTG101205673117 |
      | DeviceType                     | SMART_METER_G     |
      | GatewayDeviceIdentification    | TEST1024000000001 |
      | Channel                        |                 1 |
      | MbusIdentificationNumber       |          12056731 |
      | MbusPrimaryAddress             |                 9 |
      | MbusManufacturerIdentification | LGB               |
      | MbusVersion                    |                66 |
      | MbusDeviceTypeIdentification   |                 3 |
    And device simulation of "TEST1024000000001" with M-Bus client version 0 values for channel 1
      | MbusPrimaryAddress             | 3        |
      | MbusIdentificationNumber       | 12056731 |
      | MbusManufacturerIdentification | LGB      |
      | MbusVersion                    | 66       |
      | MbusDeviceTypeIdentification   | 3        |
    When the Decouple G-meter "TESTG101205673117" from E-meter "TEST1024000000001" request is received
    Then the Decouple response is "OK"
    And the mbus device "TESTG101205673117" is not coupled to the device "TEST1024000000001"
    And the values for the M-Bus client for channel 1 on device simulator "TEST1024000000001" are
      | MbusPrimaryAddress             | 0 |
      | MbusIdentificationNumber       | 0 |
      | MbusManufacturerIdentification | 0 |
      | MbusVersion                    | 0 |
      | MbusDeviceTypeIdentification   | 0 |

  Scenario: Decouple decoupled G-meter "TESTG101205673117" from E-meter "TEST1024000000001"
    Given a dlms device
      | DeviceIdentification | TEST1024000000001 |
      | DeviceType           | SMART_METER_E     |
    And a dlms device
      | DeviceIdentification           | TESTG101205673117 |
      | DeviceType                     | SMART_METER_G     |
      | MbusIdentificationNumber       |          12056731 |
      | MbusPrimaryAddress             |                 9 |
      | MbusManufacturerIdentification | LGB               |
      | MbusVersion                    |                66 |
      | MbusDeviceTypeIdentification   |                 3 |
    And device simulation of "TEST1024000000001" with M-Bus client version 0 values for channel 1
      | MbusPrimaryAddress             | 0 |
      | MbusIdentificationNumber       | 0 |
      | MbusManufacturerIdentification | 0 |
      | MbusVersion                    | 0 |
      | MbusDeviceTypeIdentification   | 0 |
    When the Decouple G-meter "TESTG101205673117" from E-meter "TEST1024000000001" request is received
    Then the Decouple response is "OK"
    And the mbus device "TESTG101205673117" is not coupled to the device "TEST1024000000001"
    And the values for the M-Bus client for channel 1 on device simulator "TEST1024000000001" are
      | MbusPrimaryAddress             | 0 |
      | MbusIdentificationNumber       | 0 |
      | MbusManufacturerIdentification | 0 |
      | MbusVersion                    | 0 |
      | MbusDeviceTypeIdentification   | 0 |
