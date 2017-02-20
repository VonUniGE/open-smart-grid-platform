/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgp.adapter.protocol.dlms.domain.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.openmuc.jdlms.AttributeAddress;
import org.openmuc.jdlms.GetResult;
import org.openmuc.jdlms.ObisCode;
import org.openmuc.jdlms.SelectiveAccessDescription;
import org.openmuc.jdlms.datatypes.CosemDateTime;
import org.openmuc.jdlms.datatypes.DataObject;
import org.openmuc.jdlms.interfaceclass.attribute.ProfileGenericAttribute;
import org.osgp.adapter.protocol.dlms.application.valueobjects.DlmsUnitType;
import org.osgp.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.osgp.adapter.protocol.dlms.domain.factories.DlmsConnectionHolder;
import org.osgp.adapter.protocol.dlms.exceptions.ProtocolAdapterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alliander.osgp.dto.valueobjects.smartmetering.CaptureObjectDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.DlmsMeterValueDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ObisCodeValuesDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ProfileEntryDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ProfileEntryValueDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ProfileGenericDataRequestDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ProfileGenericDataResponseDto;

@Component()
public class GetProfileGenericDataCommandExecutor extends
        AbstractCommandExecutor<ProfileGenericDataRequestDto, ProfileGenericDataResponseDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetProfileGenericDataCommandExecutor.class);

    private static final int ACCESS_SELECTOR_RANGE_DESCRIPTOR = 1;
    private static final int CLASS_ID_PROFILE_GENERIC = 7;
    private static final byte ATTRIBUTE_ID_SCALER_UNIT = 3;

    private static final int[] HAS_SCALER_UNITS = new int[] { CosemInterfaceClass.REGISTER.id(),
        CosemInterfaceClass.EXTENDED_REGISTER.id(), CosemInterfaceClass.DEMAND_REGISTER.id() };

    @Autowired
    private DlmsHelperService dlmsHelperService;

    public GetProfileGenericDataCommandExecutor() {
        super(ProfileGenericDataRequestDto.class);
    }

    @Override
    public ProfileGenericDataResponseDto execute(final DlmsConnectionHolder conn, final DlmsDevice device,
            final ProfileGenericDataRequestDto profileGenericDataRequestDto) throws ProtocolAdapterException {

        final DateTime beginDateTime = new DateTime(profileGenericDataRequestDto.getBeginDate());
        final DateTime endDateTime = new DateTime(profileGenericDataRequestDto.getEndDate());
        final ObisCode obisCode = this.makeObisCode(profileGenericDataRequestDto.getObisCode());
        final ObisCodeValuesDto inputObisCodes = profileGenericDataRequestDto.getObisCode();

        LOGGER.debug("Retrieving profile generic data for, from: {}, to: {}", beginDateTime, endDateTime);

        List<GetResult> captureObjects = this.retrieveCaptureObjects(conn, device, obisCode);
        List<GetResult> bufferList = this.retrieveBuffer(conn, device, beginDateTime, endDateTime, obisCode);
        List<ScalerUnitInfo> scalerUnitInfos = this.retrieveScalerUnits(conn, device, captureObjects);
        return this.processData(inputObisCodes, captureObjects, bufferList, scalerUnitInfos);
    }

    private List<GetResult> retrieveCaptureObjects(DlmsConnectionHolder conn, DlmsDevice device, final ObisCode obisCode)
            throws ProtocolAdapterException {
        AttributeAddress captureObjectsAttributeAddress = new AttributeAddress(CLASS_ID_PROFILE_GENERIC, obisCode,
                ProfileGenericAttribute.CAPTURE_OBJECTS.attributeId());

        return this.dlmsHelperService.getAndCheck(conn, device, "retrieve profile generic capture objects",
                captureObjectsAttributeAddress);
    }

    private List<GetResult> retrieveBuffer(final DlmsConnectionHolder conn, final DlmsDevice device,
            final DateTime beginDateTime, final DateTime endDateTime, final ObisCode obisCode)
                    throws ProtocolAdapterException {
        final SelectiveAccessDescription access = this.getSelectiveAccessDescription(beginDateTime, endDateTime);
        AttributeAddress bufferAttributeAddress = new AttributeAddress(CLASS_ID_PROFILE_GENERIC, obisCode,
                ProfileGenericAttribute.BUFFER.attributeId(), access);
        return this.dlmsHelperService.getAndCheck(conn, device, "retrieve profile generic buffer",
                bufferAttributeAddress);
    }

    /*
     * Process data Add units to capture objects Calculate the proper values in
     * the buffer using the scaler
     */
    private ProfileGenericDataResponseDto processData(final ObisCodeValuesDto obisCode,
            final List<GetResult> captureObjects, final List<GetResult> bufferList, List<ScalerUnitInfo> scalerUnitInfos)
                    throws ProtocolAdapterException {

        List<CaptureObjectDto> captureObjectDtos = this.makeCaptureObjects(captureObjects, scalerUnitInfos);
        List<ProfileEntryDto> profileEntryDtos = this.makeProfileEntries(bufferList, scalerUnitInfos);
        return new ProfileGenericDataResponseDto(obisCode, captureObjectDtos, profileEntryDtos);
    }

    private List<ProfileEntryDto> makeProfileEntries(final List<GetResult> bufferList,
            final List<ScalerUnitInfo> scalerUnitInfos) throws ProtocolAdapterException {

        List<ProfileEntryDto> profileEntryDtos = new ArrayList<>();
        for (GetResult buffer : bufferList) {
            DataObject dataObject = buffer.getResultData();
            final List<DataObject> dataObjectList1 = dataObject.getValue();
            for (DataObject profEntryDataObject : dataObjectList1) {
                profileEntryDtos.add(new ProfileEntryDto(this.makeProfileEntryValueDto(profEntryDataObject,
                        scalerUnitInfos)));
            }
        }
        return profileEntryDtos;
    }

    private List<CaptureObjectDto> makeCaptureObjects(final List<GetResult> captureObjects,
            List<ScalerUnitInfo> scalerUnitInfos) throws ProtocolAdapterException {

        List<CaptureObjectDto> captureObjectDtos = new ArrayList<>();
        for (GetResult captureObjectResult : captureObjects) {
            DataObject dataObject = captureObjectResult.getResultData();
            final List<DataObject> dataObjectList1 = dataObject.getValue();
            for (int i = 0; i < dataObjectList1.size(); i++) {
                captureObjectDtos.add(this.makeCaptureObjectDto(dataObjectList1.get(i), scalerUnitInfos.get(i)));
            }
        }
        return captureObjectDtos;
    }

    private SelectiveAccessDescription getSelectiveAccessDescription(final DateTime beginDateTime,
            final DateTime endDateTime) {

        final int accessSelector = ACCESS_SELECTOR_RANGE_DESCRIPTOR;

        /*
         * Define the clock object {8,0-0:1.0.0.255,2,0} to be used as
         * restricting object in a range descriptor with a from value and to
         * value to determine which elements from the buffered array should be
         * retrieved.
         */
        final DataObject clockDefinition = this.dlmsHelperService.getClockDefinition();

        final DataObject fromValue = this.dlmsHelperService.asDataObject(beginDateTime);
        final DataObject toValue = this.dlmsHelperService.asDataObject(endDateTime);

        /*
         * List of object definitions to determine which of the capture objects
         * to retrieve from the buffer.
         */
        final List<DataObject> objectDefinitions = new ArrayList<>();
        final DataObject selectedValues = DataObject.newArrayData(objectDefinitions);

        final DataObject accessParameter = DataObject.newStructureData(Arrays.asList(clockDefinition, fromValue,
                toValue, selectedValues));

        return new SelectiveAccessDescription(accessSelector, accessParameter);
    }

    private ObisCode makeObisCode(final ObisCodeValuesDto obisCodeValues) {
        final byte[] obisCodeBytes = { obisCodeValues.getA(), obisCodeValues.getB(), obisCodeValues.getC(),
                obisCodeValues.getD(), obisCodeValues.getE(), obisCodeValues.getF() };
        return new ObisCode(obisCodeBytes);
    }

    private CaptureObjectDto makeCaptureObjectDto(final DataObject captureObjectDataObject,
            final ScalerUnitInfo scalerUnitInfo) throws ProtocolAdapterException {
        final List<DataObject> dataObjects = captureObjectDataObject.getValue();

        final int classId = this.getInt(dataObjects.get(CaptureObjectDefinition.CLASS_ID.index()));
        final String logicalName = this.dlmsHelperService.readString(
                dataObjects.get(CaptureObjectDefinition.LOGICAL_NAME.index()), "obis-code");
        final int attribute = this.getInt(dataObjects.get(CaptureObjectDefinition.ATTRIBUTE_INDEX.index()));
        final int version = this.getInt(dataObjects.get(CaptureObjectDefinition.DATA_INDEX.index()));
        return new CaptureObjectDto(classId, logicalName, attribute, version, this.getUnit(scalerUnitInfo));
    }

    private String getUnit(final ScalerUnitInfo scalerUnitInfo) {
        if (scalerUnitInfo.getScalerUnit() != null) {
            final List<DataObject> dataObjects = scalerUnitInfo.getScalerUnit().getValue();
            final int unit = Integer.parseInt(dataObjects.get(1).getValue().toString());
            return DlmsUnitType.getUnit(unit);
        } else {
            return DlmsUnitType.UNDEFINED.getUnit();
        }
    }

    private List<ProfileEntryValueDto> makeProfileEntryValueDto(final DataObject profEntryDataObjects,
            List<ScalerUnitInfo> scalerUnitInfos) throws ProtocolAdapterException {

        final List<ProfileEntryValueDto> result = new ArrayList<>();
        final List<DataObject> dataObjects = profEntryDataObjects.getValue();
        for (int i = 0; i < dataObjects.size(); i++) {
            result.add(this.makeProfileEntryValueDto(dataObjects.get(i), scalerUnitInfos.get(i)));
        }
        return result;
    }

    private ProfileEntryValueDto makeProfileEntryValueDto(final DataObject dataObject,
            final ScalerUnitInfo scalerUnitInfo) {
        if (CosemInterfaceClass.CLOCK.id() == scalerUnitInfo.getClassId()) {
            return this.makeDateProfileEntryValueDto(dataObject);
        } else if (dataObject.isNumber()) {
            return this.makeNumericProfileEntryValueDto(dataObject, scalerUnitInfo);
        } else {
            final String dbgInfo = this.dlmsHelperService.getDebugInfo(dataObject);
            LOGGER.debug("creating ProfileEntryDto from " + dbgInfo + " " + scalerUnitInfo);
            return new ProfileEntryValueDto(dbgInfo);
        }
    }

    private ProfileEntryValueDto makeDateProfileEntryValueDto(final DataObject dataObject) {
        CosemDateTime cosemDateTime = CosemDateTime.decode((byte[]) dataObject.getValue());
        return new ProfileEntryValueDto(cosemDateTime.toCalendar().getTime());
    }

    private ProfileEntryValueDto makeNumericProfileEntryValueDto(final DataObject dataObject,
            final ScalerUnitInfo scalerUnitInfo) {
        try {
            if (scalerUnitInfo.getScalerUnit() != null) {
                DlmsMeterValueDto meterValue = this.dlmsHelperService.getScaledMeterValue(dataObject,
                        scalerUnitInfo.getScalerUnit(), "getScaledMeterValue");
                return new ProfileEntryValueDto(meterValue.getValue());
            } else {
                long value = this.dlmsHelperService.readLong(dataObject, "read long");
                return new ProfileEntryValueDto(value);
            }
        } catch (ProtocolAdapterException e) {
            LOGGER.error("Error creating ProfileEntryDto from " + dataObject + " :" + e);
            final String dbgInfo = this.dlmsHelperService.getDebugInfo(dataObject);
            return new ProfileEntryValueDto(dbgInfo);
        }
    }

    private int getInt(final DataObject dataObject) throws ProtocolAdapterException {
        return this.dlmsHelperService.readLong(dataObject, "read-long").intValue();
    }

    private List<ScalerUnitInfo> retrieveScalerUnits(DlmsConnectionHolder conn, DlmsDevice device,
            List<GetResult> captureObjects) throws ProtocolAdapterException {

        final List<ScalerUnitInfo> result = new ArrayList<>();

        for (GetResult captureObjectResult : captureObjects) {
            DataObject dataObject = captureObjectResult.getResultData();
            final List<DataObject> dataObjectList1 = dataObject.getValue();
            for (DataObject captureObjectDataObject : dataObjectList1) {
                final List<DataObject> dataObjects = captureObjectDataObject.getValue();
                final int classId = this.getInt(dataObjects.get(CaptureObjectDefinition.CLASS_ID.index()));
                final String logicalName = this.dlmsHelperService.readString(
                        dataObjects.get(CaptureObjectDefinition.LOGICAL_NAME.index()), "obis-code");
                if (this.hasScalerUnit(classId)) {
                    AttributeAddress addr = new AttributeAddress(classId, logicalName, ATTRIBUTE_ID_SCALER_UNIT);
                    final List<GetResult> scalerUnitResult = this.dlmsHelperService.getAndCheck(conn, device,
                            "retrieve scaler unit for capture object", addr);
                    DataObject scalerUnitDataObject = scalerUnitResult.get(0).getResultData();
                    result.add(new ScalerUnitInfo(logicalName, classId, scalerUnitDataObject));
                } else {
                    result.add(new ScalerUnitInfo(logicalName, classId, null));
                }
            }
        }

        return result;
    }

    private boolean hasScalerUnit(final int classId) {
        for (int classIdWithScalar : HAS_SCALER_UNITS) {
            if (classId == classIdWithScalar) {
                return true;
            }
        }
        return false;
    }

}
