package com.revature.globetrotters.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

class TravelPlanServiceTest {

    @Mock
    private TravelPlanRepository travelPlanRepository;

    @Mock
    private TravelPlanLocationRepository travelPlanLocationRepository;

    @InjectMocks
    private TravelPlanService travelPlanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTravelPlanById() {
        TravelPlan travelPlan = new TravelPlan(1, 101, true, false);
        when(travelPlanRepository.getTravelPlanById(1)).thenReturn(travelPlan);

        TravelPlan fetchedTravelPlan = travelPlanService.getTravelPlanById(1);

        assertNotNull(fetchedTravelPlan);
        assertEquals(1, fetchedTravelPlan.getId());
        verify(travelPlanRepository, times(1)).getTravelPlanById(1);
    }

    @Test
    void testDeleteTravelPlan() {
        TravelPlan travelPlan = new TravelPlan(1, 101, true, false);
        when(travelPlanRepository.getTravelPlanById(1)).thenReturn(travelPlan);
        doNothing().when(travelPlanRepository).deleteById(1);

        travelPlanService.deleteTravelPlan(1);

        verify(travelPlanRepository, times(1)).getTravelPlanById(1);
        verify(travelPlanRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteTravelPlanThrowsExceptionForNonExistentPlan() {
        when(travelPlanRepository.getTravelPlanById(1)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> travelPlanService.deleteTravelPlan(1));
        assertEquals("Travel plan not found", exception.getMessage());
    }
}
