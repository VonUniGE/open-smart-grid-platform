/*
 * Copyright 2021 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.application.mapping;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.openmuc.jdlms.datatypes.DataObject;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.SendDestinationAndMethodDto;

public class SendDestinationAndMethodConverter
    extends CustomConverter<SendDestinationAndMethodDto, DataObject> {

  @Override
  public DataObject convert(
      final SendDestinationAndMethodDto source,
      final Type<? extends DataObject> destinationType,
      final MappingContext context) {
    if (source == null) {
      return null;
    }

    final List<DataObject> sendDestinationAndMethodElements = new ArrayList<>();

    // add service
    final int enumValueTransportServiceType = source.getTransportService().getDlmsEnumValue();
    sendDestinationAndMethodElements.add(
        DataObject.newEnumerateData(enumValueTransportServiceType));

    // add destination
    sendDestinationAndMethodElements.add(
        DataObject.newOctetStringData(source.getDestination().getBytes(StandardCharsets.US_ASCII)));

    // add message
    final int enumValueMessageType = source.getMessage().getDlmsEnumValue();
    sendDestinationAndMethodElements.add(DataObject.newEnumerateData(enumValueMessageType));

    return DataObject.newStructureData(sendDestinationAndMethodElements);
  }
}
