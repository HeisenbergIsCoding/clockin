package com.clockin.admin.service;

import com.clockin.admin.dto.HolidayDTO;
import com.clockin.admin.entity.Holiday;
import com.clockin.admin.repository.HolidayRepository;
import com.clockin.admin.service.impl.HolidayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 假日服務測試
 */
public class HolidayServiceTest {

    @InjectMocks
    private HolidayServiceImpl holidayService;

    @Mock
    private HolidayRepository holidayRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateHoliday() {
        Holiday holiday = new Holiday();
        holiday.setHolidayDate(LocalDate.of(2025, 1, 1));
        holiday.setHolidayName("元旦");
        holiday.setHolidayType(1);
        holiday.setStatus(1);
        
        Holiday savedHoliday = new Holiday();
        savedHoliday.setId(1L);
        savedHoliday.setHolidayDate(holiday.getHolidayDate());
        savedHoliday.setHolidayName(holiday.getHolidayName());
        savedHoliday.setHolidayType(holiday.getHolidayType());
        savedHoliday.setYear(2025);
        savedHoliday.setMonth(1);
        savedHoliday.setDay(1);
        savedHoliday.setStatus(holiday.getStatus());
        
        when(holidayRepository.save(any(Holiday.class))).thenReturn(savedHoliday);
        
        Holiday result = holidayService.createHoliday(holiday);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(holiday.getHolidayDate(), result.getHolidayDate());
        assertEquals(holiday.getHolidayName(), result.getHolidayName());
        assertEquals(holiday.getHolidayType(), result.getHolidayType());
        assertEquals(2025, result.getYear());
        
        verify(holidayRepository, times(1)).save(any(Holiday.class));
    }
    
    @Test
    public void testGetHolidayById() {
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
        
        when(holidayRepository.findById(id)).thenReturn(Optional.of(holiday));
        
        Optional<Holiday> result = holidayService.getHolidayById(id);
        
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals(holiday.getHolidayDate(), result.get().getHolidayDate());
        assertEquals(holiday.getHolidayName(), result.get().getHolidayName());
        
        verify(holidayRepository, times(1)).findById(id);
    }
    
    @Test
    public void testGetHolidaysByYear() {
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
        
        when(holidayRepository.findByYearOrderByHolidayDateAsc(eq(year))).thenReturn(Collections.singletonList(holiday));
        
        List<Holiday> results = holidayService.getHolidaysByYear(year);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(holiday.getId(), results.get(0).getId());
        assertEquals(holiday.getHolidayDate(), results.get(0).getHolidayDate());
        
        verify(holidayRepository, times(1)).findByYearOrderByHolidayDateAsc(eq(year));
    }
    
    @Test
    public void testGetHolidaysPage() {
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
        Pageable pageable = PageRequest.of(0, 10);
        
        when(holidayRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        
        Page<Holiday> results = holidayService.getHolidaysPage(null, null, null, pageable);
        
        assertNotNull(results);
        assertEquals(1, results.getTotalElements());
        assertEquals(holiday.getId(), results.getContent().get(0).getId());
        
        verify(holidayRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }
}
