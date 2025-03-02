package com.clockin.admin.controller;

import com.clockin.admin.dto.HolidayDTO;
import com.clockin.admin.entity.Holiday;
import com.clockin.admin.service.HolidayService;
import com.clockin.common.response.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 假日控制器測試
 */
public class HolidayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HolidayService holidayService;

    @InjectMocks
    private HolidayController holidayController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // 支持 Java 8 日期時間
    }

    @Test
    public void testCreateHoliday() throws Exception {
        // 準備測試數據
        Holiday holiday = new Holiday();
        holiday.setHolidayName("元旦");
        holiday.setHolidayDate(LocalDate.of(2025, 1, 1));
        holiday.setHolidayType(1);
        holiday.setStatus(1);

        Holiday createdHoliday = new Holiday();
        createdHoliday.setId(1L);
        createdHoliday.setHolidayName(holiday.getHolidayName());
        createdHoliday.setHolidayDate(holiday.getHolidayDate());
        createdHoliday.setHolidayType(holiday.getHolidayType());
        createdHoliday.setYear(2025);
        createdHoliday.setMonth(1);
        createdHoliday.setDay(1);
        createdHoliday.setStatus(holiday.getStatus());

        when(holidayService.createHoliday(any(Holiday.class))).thenReturn(createdHoliday);

        mockMvc.perform(post("/holiday")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(holiday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(holidayService, times(1)).createHoliday(any(Holiday.class));
    }

    @Test
    public void testGetHolidayById() throws Exception {
        Long id = 1L;

        Holiday holiday = new Holiday();
        holiday.setId(id);
        holiday.setHolidayDate(LocalDate.of(2025, 1, 1));
        holiday.setHolidayName("元旦");
        holiday.setHolidayType(1);
        holiday.setYear(2025);
        holiday.setMonth(1);
        holiday.setDay(1);
        holiday.setStatus(1);

        when(holidayService.getHolidayById(id)).thenReturn(Optional.of(holiday));

        mockMvc.perform(get("/holiday/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.holidayName").value("元旦"));

        verify(holidayService, times(1)).getHolidayById(id);
    }

    @Test
    public void testListHolidays() throws Exception {
        Holiday holiday = new Holiday();
        holiday.setId(1L);
        holiday.setHolidayDate(LocalDate.of(2025, 1, 1));
        holiday.setHolidayName("元旦");
        holiday.setHolidayType(1);
        holiday.setYear(2025);
        holiday.setMonth(1);
        holiday.setDay(1);
        holiday.setStatus(1);

        Page<Holiday> page = new PageImpl<>(Collections.singletonList(holiday));

        when(holidayService.getHolidaysPage(any(), any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/holiday/list")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content[0].id").value(1));

        verify(holidayService, times(1)).getHolidaysPage(any(), any(), any(), any(Pageable.class));
    }

    @Test
    public void testGetHolidaysByYear() throws Exception {
        Integer year = 2025;

        Holiday holiday = new Holiday();
        holiday.setId(1L);
        holiday.setHolidayDate(LocalDate.of(2025, 1, 1));
        holiday.setHolidayName("元旦");
        holiday.setHolidayType(1);
        holiday.setYear(2025);
        holiday.setMonth(1);
        holiday.setDay(1);
        holiday.setStatus(1);

        when(holidayService.getHolidaysByYear(year)).thenReturn(Collections.singletonList(holiday));

        mockMvc.perform(get("/holiday/year/{year}", year))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].year").value(2025));

        verify(holidayService, times(1)).getHolidaysByYear(year);
    }
}
