/*
 * Copyright 2019 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.monitoring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.opensmartgridplatform.dlms.objectconfig.DlmsObjectType.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openmuc.jdlms.AttributeAddress;
import org.openmuc.jdlms.GetResult;
import org.openmuc.jdlms.datatypes.DataObject;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.testutil.GetResultImpl;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.testutil.ObjectConfigServiceHelper;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.utils.DlmsHelper;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.factories.DlmsConnectionManager;
import org.opensmartgridplatform.adapter.protocol.dlms.exceptions.ProtocolAdapterException;
import org.opensmartgridplatform.dlms.exceptions.ObjectConfigException;
import org.opensmartgridplatform.dlms.objectconfig.*;
import org.opensmartgridplatform.dlms.services.ObjectConfigService;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.CaptureObjectDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.GetPowerQualityProfileRequestDataDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.GetPowerQualityProfileResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PowerQualityProfileDataDto;

@ExtendWith(MockitoExtension.class)
class GetPowerQualityProfileNoSelectiveAccessHandlerTest extends ObjectConfigServiceHelper {

  private static final String PROTOCOL_NAME = "SMR";
  private static final String PROTOCOL_VERSION = "5.0.0";
  private static final int CLASS_ID_DATA = 1;
  private static final int CLASS_ID_REGISTER = 3;
  private static final String OBIS_PRIVATE = "0.1.94.31.6.255";
  private static final String OBIS_PROFILE = "0.1.99.1.1.255";
  private static final String OBIS_CLOCK = "0.0.1.0.0.255";
  private static final String OBIS_INSTANTANEOUS_VOLTAGE_L1 = "1.0.32.7.0.255";
  private static final String UNIT_UNDEFINED = "UNDEFINED";
  private static final String UNIT_VOLT = "V";

  @Mock private DlmsHelper dlmsHelper;
  @Mock private DlmsConnectionManager conn;
  @Mock private DlmsDevice dlmsDevice;
  @Mock private ObjectConfigService objectConfigService;

  @ParameterizedTest
  @EnumSource(PowerQualityProfile.class)
  void testHandlePublicOrPrivateProfileWithoutSelectiveAccessWithPartialNonAllowedObjects(
      final PowerQualityProfile profile) throws ProtocolAdapterException, ObjectConfigException {

    final boolean polyPhase = true;
    final List<CosemObject> allPqObjectsForThisMeter = this.getObjects(polyPhase, profile.name());
    final GetPowerQualityProfileRequestDataDto requestDto =
        new GetPowerQualityProfileRequestDataDto(
            profile.name(),
            Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),
            new Date(),
            new ArrayList<>());
    when(this.dlmsDevice.getProtocolName()).thenReturn(PROTOCOL_NAME);
    when(this.dlmsDevice.getProtocolVersion()).thenReturn(PROTOCOL_VERSION);
    when(this.dlmsHelper.readLogicalName(any(DataObject.class), any(String.class)))
        .thenCallRealMethod();
    when(this.dlmsHelper.readObjectDefinition(any(DataObject.class), any(String.class)))
        .thenCallRealMethod();
    when(this.dlmsHelper.readLongNotNull(any(DataObject.class), any(String.class)))
        .thenCallRealMethod();
    when(this.dlmsHelper.readLong(any(DataObject.class), any(String.class))).thenCallRealMethod();
    when(this.objectConfigService.getCosemObject(
            PROTOCOL_NAME, PROTOCOL_VERSION, POWER_QUALITY_PROFILE_2))
        .thenReturn(
            this.createObjectWithProperties(
                7,
                "0.1.99.1.2.255",
                "POWER_QUALITY_PROFILE_2",
                null,
                polyPhase,
                profile.name(),
                true));
    when(this.objectConfigService.getCosemObject(
            PROTOCOL_NAME, PROTOCOL_VERSION, POWER_QUALITY_PROFILE_1))
        .thenReturn(
            this.createObjectWithProperties(
                7, OBIS_PRIVATE, "POWER_QUALITY_PROFILE_1", null, polyPhase, profile.name(), true));
    when(this.objectConfigService.getCosemObject(
            PROTOCOL_NAME, PROTOCOL_VERSION, DEFINABLE_LOAD_PROFILE))
        .thenReturn(
            this.createObjectWithProperties(
                7, OBIS_PROFILE, "DEFINABLE_LOAD_PROFILE", null, polyPhase, profile.name(), true));
    when(this.objectConfigService.getCosemObjects(
            PROTOCOL_NAME, PROTOCOL_VERSION, this.getPropertyObjects()))
        .thenReturn(allPqObjectsForThisMeter);

    when(this.dlmsHelper.getAndCheck(
            any(DlmsConnectionManager.class),
            any(DlmsDevice.class),
            any(String.class),
            any(AttributeAddress.class)))
        .thenReturn(
            this.createPartialNotAllowedCaptureObjects(),
            this.createProfileEntries(),
            this.createPartialNotAllowedCaptureObjects(),
            this.createProfileEntries());

    // EXECUTE
    final GetPowerQualityProfileNoSelectiveAccessHandler handler =
        new GetPowerQualityProfileNoSelectiveAccessHandler(
            this.dlmsHelper, this.objectConfigService);
    final GetPowerQualityProfileResponseDto responseDto =
        handler.handle(this.conn, this.dlmsDevice, requestDto);

    assertThat(responseDto.getPowerQualityProfileResponseDatas()).hasSize(3);

    Optional<PowerQualityProfileDataDto> profileData =
        responseDto.getPowerQualityProfileResponseDatas().stream()
            .filter(data -> data.getLogicalName().toString().equals(OBIS_PROFILE))
            .findFirst();

    assertTrue(profileData.isPresent());
    assertThat(profileData.get().getCaptureObjects()).hasSize(1);

    CaptureObjectDto captureObject = profileData.get().getCaptureObjects().get(0);
    assertThat(captureObject.getLogicalName()).isEqualTo(OBIS_INSTANTANEOUS_VOLTAGE_L1);
    assertThat(captureObject.getUnit()).isEqualTo(UNIT_VOLT);
  }

  @ParameterizedTest
  @EnumSource(PowerQualityProfile.class)
  void testHandlePublicOrPrivateProfileWithInvalidConfigThrowsException(
      final PowerQualityProfile profile) throws ObjectConfigException, ProtocolAdapterException {

    final boolean polyPhase = true;
    final List<CosemObject> allPqObjectsForThisMeter = this.getObjects(polyPhase, profile.name());
    final GetPowerQualityProfileRequestDataDto requestDto =
        new GetPowerQualityProfileRequestDataDto(
            profile.name(),
            Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),
            new Date(),
            new ArrayList<>());
    when(this.dlmsDevice.getProtocolName()).thenReturn(PROTOCOL_NAME);
    when(this.dlmsDevice.getProtocolVersion()).thenReturn(PROTOCOL_VERSION);
    when(this.dlmsHelper.readObjectDefinition(any(DataObject.class), any(String.class)))
        .thenCallRealMethod();
    when(this.objectConfigService.getCosemObject(
            PROTOCOL_NAME, PROTOCOL_VERSION, POWER_QUALITY_PROFILE_2))
        .thenReturn(
            this.createObjectWithProperties(
                7,
                "0.1.99.1.2.255",
                "POWER_QUALITY_PROFILE_2",
                null,
                polyPhase,
                profile.name(),
                true));
    when(this.objectConfigService.getCosemObject(
            PROTOCOL_NAME, PROTOCOL_VERSION, POWER_QUALITY_PROFILE_1))
        .thenReturn(
            this.createObjectWithProperties(
                7, OBIS_PRIVATE, "POWER_QUALITY_PROFILE_1", null, polyPhase, profile.name(), true));
    when(this.objectConfigService.getCosemObject(
            PROTOCOL_NAME, PROTOCOL_VERSION, DEFINABLE_LOAD_PROFILE))
        .thenReturn(
            this.createObjectWithProperties(
                7, OBIS_PROFILE, "DEFINABLE_LOAD_PROFILE", null, polyPhase, profile.name(), true));
    when(this.objectConfigService.getCosemObjects(
            PROTOCOL_NAME, PROTOCOL_VERSION, this.getPropertyObjects()))
        .thenReturn(allPqObjectsForThisMeter);

    when(this.dlmsHelper.getAndCheck(
            any(DlmsConnectionManager.class),
            any(DlmsDevice.class),
            any(String.class),
            any(AttributeAddress.class)))
        .thenReturn(
            this.createInvalidCaptureObjects(),
            this.createProfileEntries(),
            this.createInvalidCaptureObjects(),
            this.createProfileEntries());
    final GetPowerQualityProfileNoSelectiveAccessHandler handler =
        new GetPowerQualityProfileNoSelectiveAccessHandler(
            this.dlmsHelper, this.objectConfigService);

    assertThrows(
        ProtocolAdapterException.class,
        () -> handler.handle(this.conn, this.dlmsDevice, requestDto));
  }

  @ParameterizedTest
  @EnumSource(PowerQualityProfile.class)
  void testInvalidProfileThrowsIllegalArgumentException(PowerQualityProfile profile)
      throws ObjectConfigException {
    final GetPowerQualityProfileRequestDataDto requestDto =
        new GetPowerQualityProfileRequestDataDto(
            profile.name(),
            Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),
            new Date(),
            new ArrayList<>());
    when(this.dlmsDevice.getProtocolName()).thenReturn(PROTOCOL_NAME);
    when(this.dlmsDevice.getProtocolVersion()).thenReturn(PROTOCOL_VERSION);
    when(this.objectConfigService.getCosemObject(
            PROTOCOL_NAME, PROTOCOL_VERSION, DEFINABLE_LOAD_PROFILE))
        .thenThrow(new IllegalArgumentException("IllegalArgumentException"));

    final GetPowerQualityProfileNoSelectiveAccessHandler handler =
        new GetPowerQualityProfileNoSelectiveAccessHandler(
            this.dlmsHelper, this.objectConfigService);

    assertThrows(
        IllegalArgumentException.class,
        () -> handler.handle(this.conn, this.dlmsDevice, requestDto));
  }

  @ParameterizedTest
  @EnumSource(PowerQualityProfile.class)
  void testInvalidProfileThrowsObjectConfigException(PowerQualityProfile profile)
      throws ObjectConfigException {
    final GetPowerQualityProfileRequestDataDto requestDto =
        new GetPowerQualityProfileRequestDataDto(
            profile.name(),
            Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),
            new Date(),
            new ArrayList<>());
    when(this.dlmsDevice.getProtocolName()).thenReturn(PROTOCOL_NAME);
    when(this.dlmsDevice.getProtocolVersion()).thenReturn(PROTOCOL_VERSION);
    when(this.objectConfigService.getCosemObject(
            PROTOCOL_NAME, PROTOCOL_VERSION, DEFINABLE_LOAD_PROFILE))
        .thenThrow(new ObjectConfigException("exception"));

    final GetPowerQualityProfileNoSelectiveAccessHandler handler =
        new GetPowerQualityProfileNoSelectiveAccessHandler(
            this.dlmsHelper, this.objectConfigService);

    assertThrows(
        ProtocolAdapterException.class,
        () -> handler.handle(this.conn, this.dlmsDevice, requestDto));
  }

  private List<CosemObject> getObjects(final boolean polyphase, final String publicOrPrivate) {
    final CosemObject dataObject =
        this.createObjectWithProperties(
            CLASS_ID_DATA,
            "1.0.0.0.0.1",
            NUMBER_OF_VOLTAGE_SAGS_FOR_L2.name(),
            null,
            polyphase,
            publicOrPrivate,
            false);

    final CosemObject registerVoltObject =
        this.createObjectWithProperties(
            CLASS_ID_REGISTER,
            OBIS_INSTANTANEOUS_VOLTAGE_L1,
            INSTANTANEOUS_VOLTAGE_L1.name(),
            "0, " + UNIT_VOLT,
            polyphase,
            publicOrPrivate,
            false);

    final CosemObject registerAmpereObject =
        this.createObjectWithProperties(
            CLASS_ID_REGISTER,
            "3.0.0.0.0.2",
            AVERAGE_CURRENT_L1.name(),
            "-1, A",
            polyphase,
            publicOrPrivate,
            false);

    final CosemObject registerVarObject =
        this.createObjectWithProperties(
            CLASS_ID_REGISTER,
            "3.0.0.0.0.3",
            AVERAGE_REACTIVE_POWER_IMPORT_L1.name(),
            "2, VAR",
            polyphase,
            publicOrPrivate,
            false);

    return new ArrayList<>(
        Arrays.asList(dataObject, registerVoltObject, registerAmpereObject, registerVarObject));
  }

  private List<GetResult> createProfileEntries() {
    final DataObject allowedCaptureObjectClock =
        DataObject.newStructureData(
            DataObject.newUInteger32Data(8),
            DataObject.newOctetStringData(new byte[] {0, 0, 1, 0, 0, (byte) 255}),
            DataObject.newInteger32Data(2),
            DataObject.newUInteger32Data(0));

    final DataObject allowedCaptureObjectINSTANTANEOUS_VOLTAGE_L1 =
        DataObject.newStructureData(
            DataObject.newUInteger32Data(1),
            DataObject.newOctetStringData(new byte[] {1, 0, 32, 7, 0, (byte) 255}),
            DataObject.newInteger32Data(2),
            DataObject.newUInteger32Data(0));

    final DataObject nonAllowedCaptureObject1 =
        DataObject.newStructureData(
            DataObject.newUInteger32Data(1),
            DataObject.newOctetStringData(new byte[] {80, 0, 32, 32, 0, (byte) 255}),
            DataObject.newInteger32Data(2),
            DataObject.newUInteger32Data(0));
    final DataObject nonAllowedCaptureObject2 =
        DataObject.newStructureData(
            DataObject.newUInteger32Data(1),
            DataObject.newOctetStringData(new byte[] {1, 0, 52, 32, 0, (byte) 255}),
            DataObject.newInteger32Data(2),
            DataObject.newUInteger32Data(0));

    final GetResult getResult =
        new GetResultImpl(
            DataObject.newArrayData(
                Arrays.asList(
                    allowedCaptureObjectClock,
                    allowedCaptureObjectINSTANTANEOUS_VOLTAGE_L1,
                    nonAllowedCaptureObject1,
                    nonAllowedCaptureObject2)));

    return Collections.singletonList(getResult);
  }

  private List<GetResult> createPartialNotAllowedCaptureObjects() {
    final DataObject allowedCaptureObjectClock =
        DataObject.newStructureData(
            DataObject.newUInteger32Data(8),
            DataObject.newOctetStringData(new byte[] {0, 0, 1, 0, 0, (byte) 255}),
            DataObject.newInteger32Data(2),
            DataObject.newUInteger32Data(0));

    final DataObject allowedCaptureObjectINSTANTANEOUS_VOLTAGE_L1 =
        DataObject.newStructureData(
            DataObject.newUInteger32Data(1),
            DataObject.newOctetStringData(new byte[] {1, 0, 32, 7, 0, (byte) 255}),
            DataObject.newInteger32Data(2),
            DataObject.newUInteger32Data(0));

    final DataObject nonAllowedCaptureObject1 =
        DataObject.newStructureData(
            DataObject.newUInteger32Data(1),
            DataObject.newOctetStringData(new byte[] {80, 0, 32, 32, 0, (byte) 255}),
            DataObject.newInteger32Data(2),
            DataObject.newUInteger32Data(0));
    final DataObject nonAllowedCaptureObject2 =
        DataObject.newStructureData(
            DataObject.newUInteger32Data(1),
            DataObject.newOctetStringData(new byte[] {1, 0, 52, 32, 0, (byte) 255}),
            DataObject.newInteger32Data(2),
            DataObject.newUInteger32Data(0));

    final GetResult getResult =
        new GetResultImpl(
            DataObject.newArrayData(
                Arrays.asList(
                    allowedCaptureObjectClock,
                    allowedCaptureObjectINSTANTANEOUS_VOLTAGE_L1,
                    nonAllowedCaptureObject1,
                    nonAllowedCaptureObject2)));

    return Collections.singletonList(getResult);
  }

  private CosemObject createObjectWithProperties(
      final int classId,
      final String obis,
      final String tag,
      final String scalerUnitValue,
      final boolean polyphase,
      final String publicOrPrivate,
      final boolean addAttributes) {

    final CosemObject object = this.createObject(classId, obis, tag, scalerUnitValue, polyphase);

    Map<ObjectProperty, Object> properties = new HashMap<>();
    List<String> stringProperties =
        this.getPropertyObjects().stream()
            .map(DlmsObjectType::toString)
            .collect(Collectors.toList());
    properties.put(ObjectProperty.SELECTABLE_OBJECTS, stringProperties);
    properties.put(ObjectProperty.PQ_PROFILE, publicOrPrivate);
    object.setProperties(properties);

    if (addAttributes) {
      List<Attribute> attributeList = new ArrayList<>();
      Attribute attribute = new Attribute();
      attribute.setId(3);
      attribute.setValue("1");
      attributeList.add(attribute);
      Attribute attributeInterval = new Attribute();
      attributeInterval.setId(4);
      attributeInterval.setValue("15");
      attributeList.add(attributeInterval);
      object.setAttributes(attributeList);
    }

    return object;
  }

  private List<DlmsObjectType> getPropertyObjects() {
    List<DlmsObjectType> objects = new ArrayList<>();
    objects.add(CLOCK);
    objects.add(INSTANTANEOUS_VOLTAGE_L1);
    objects.add(NUMBER_OF_VOLTAGE_SAGS_FOR_L1);
    objects.add(NUMBER_OF_VOLTAGE_SAGS_FOR_L2);
    objects.add(AVERAGE_CURRENT_L1);
    objects.add(AVERAGE_REACTIVE_POWER_IMPORT_L1);
    return objects;
  }

  private List<GetResult> createInvalidCaptureObjects() {
    final DataObject invalidObject =
        DataObject.newStructureData(
            DataObject.newNullData(),
            DataObject.newInteger32Data(2),
            DataObject.newUInteger32Data(0));

    final GetResult getResult =
        new GetResultImpl(DataObject.newArrayData(Arrays.asList(invalidObject)));

    return Collections.singletonList(getResult);
  }
}
