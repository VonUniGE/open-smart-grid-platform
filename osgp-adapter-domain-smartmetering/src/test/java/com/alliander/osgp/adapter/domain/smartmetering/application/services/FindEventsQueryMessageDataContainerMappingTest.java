/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alliander.osgp.adapter.domain.smartmetering.application.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;

import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.ManagementMapper;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.EventLogCategory;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.FindEventsQuery;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.FindEventsQueryMessageDataContainer;
import com.alliander.osgp.dto.valueobjects.smartmetering.FindEventsQueryMessageDataContainerDto;

public class FindEventsQueryMessageDataContainerMappingTest {

    private ManagementMapper managementMapper = new ManagementMapper();

    // MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Test
    public void testWithNullList() {

        final FindEventsQueryMessageDataContainer container = new FindEventsQueryMessageDataContainer(null);
        final FindEventsQueryMessageDataContainerDto containerDto = this.managementMapper.map(container,
                FindEventsQueryMessageDataContainerDto.class);

        assertNotNull(containerDto);
        assertNull(containerDto.getFindEventsQueryList());
    }

    @Test
    public void testWithEmptyList() {

        final FindEventsQueryMessageDataContainer container = new FindEventsQueryMessageDataContainer(
                new ArrayList<FindEventsQuery>());
        final FindEventsQueryMessageDataContainerDto containerDto = this.managementMapper.map(container,
                FindEventsQueryMessageDataContainerDto.class);

        assertNotNull(containerDto);
        assertNotNull(containerDto.getFindEventsQueryList());
        assertTrue(containerDto.getFindEventsQueryList().isEmpty());
    }

    @Test
    public void testWithFilledList() {

        final FindEventsQuery findEventsQuery = new FindEventsQuery(EventLogCategory.STANDARD_EVENT_LOG,
                new DateTime(), new DateTime());
        final ArrayList<FindEventsQuery> findEventsQueryList = new ArrayList<FindEventsQuery>();
        findEventsQueryList.add(findEventsQuery);
        final FindEventsQueryMessageDataContainer container = new FindEventsQueryMessageDataContainer(
                findEventsQueryList);
        final FindEventsQueryMessageDataContainerDto containerDto = this.managementMapper.map(container,
                FindEventsQueryMessageDataContainerDto.class);

        assertNotNull(containerDto);
        assertNotNull(containerDto.getFindEventsQueryList());
        assertEquals(container.getFindEventsQueryList().get(0).getEventLogCategory().name(), containerDto
                .getFindEventsQueryList().get(0).getEventLogCategory().name());
        assertEquals(container.getFindEventsQueryList().get(0).getFrom(), containerDto.getFindEventsQueryList().get(0)
                .getFrom());
        assertEquals(container.getFindEventsQueryList().get(0).getUntil(), containerDto.getFindEventsQueryList().get(0)
                .getUntil());
    }
}
