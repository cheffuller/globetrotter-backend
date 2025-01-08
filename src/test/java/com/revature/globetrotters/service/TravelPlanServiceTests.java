package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import com.revature.globetrotters.utils.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TravelPlanServiceTest {
    @Mock
    private TokenService tokenService;
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
    void testGetTravelPlanById() throws NotFoundException {
        TravelPlan travelPlan = new TravelPlan(1, 101, true, false);
        when(travelPlanRepository.findById(1)).thenReturn(Optional.of(travelPlan));

        TravelPlan fetchedTravelPlan = travelPlanService.getTravelPlanById(1);

        assertNotNull(fetchedTravelPlan);
        assertEquals(1, fetchedTravelPlan.getId());
        verify(travelPlanRepository, times(1)).findById(1);
    }
}
