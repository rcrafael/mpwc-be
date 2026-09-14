package com.mahabang_parang.api_user.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerMeterDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testDeserializeDateUpdated_dateOnly() throws Exception {
        String json = "{\"meterNo\":\"MTR-001\",\"dateUpdated\":\"2026-08-04\"}";
        CustomerMeterDto dto = objectMapper.readValue(json, CustomerMeterDto.class);

        assertNotNull(dto);
        assertEquals("MTR-001", dto.getMeterNo());
        assertEquals(LocalDateTime.of(2026, 8, 4, 0, 0, 0), dto.getDateUpdated());
    }

    @Test
    void testDeserializeDateUpdated_isoDateTime() throws Exception {
        String json = "{\"meterNo\":\"MTR-001\",\"dateUpdated\":\"2026-08-04T14:30:00\"}";
        CustomerMeterDto dto = objectMapper.readValue(json, CustomerMeterDto.class);

        assertNotNull(dto);
        assertEquals(LocalDateTime.of(2026, 8, 4, 14, 30, 0), dto.getDateUpdated());
    }

    @Test
    void testDeserializeDateUpdated_spaceDateTime() throws Exception {
        String json = "{\"meterNo\":\"MTR-001\",\"dateUpdated\":\"2026-08-04 14:30:00\"}";
        CustomerMeterDto dto = objectMapper.readValue(json, CustomerMeterDto.class);

        assertNotNull(dto);
        assertEquals(LocalDateTime.of(2026, 8, 4, 14, 30, 0), dto.getDateUpdated());
    }

    @Test
    void testDeserializeDateUpdated_nullOrEmpty() throws Exception {
        String json = "{\"meterNo\":\"MTR-001\",\"dateUpdated\":null}";
        CustomerMeterDto dto = objectMapper.readValue(json, CustomerMeterDto.class);

        assertNotNull(dto);
        assertNull(dto.getDateUpdated());
    }
}
