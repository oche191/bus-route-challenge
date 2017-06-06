package me.oche.service;

import me.oche.BaseTest;
import me.oche.repository.BusRouteRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

public class BusRouteServiceTest extends BaseTest {

    @Mock
    private BusRouteRepository busRouteRepository;

    private BusRouteService busRouteService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.busRouteService = new BusRouteService(busRouteRepository);
    }

    @Test
    public void baseDirectBusRouteTest() throws IOException {
        int depSid = 1;
        int arrSid = 2;
        int station = 0;

        Map<Integer, Set<Integer>> mockedStationToRoute = new HashMap<>();
        mockedStationToRoute.put(1, new HashSet<>(Arrays.asList(station)));
        mockedStationToRoute.put(2, new HashSet<>(Arrays.asList(station)));
        when(busRouteRepository.getStationToRoutes()).thenReturn(CompletableFuture.completedFuture(mockedStationToRoute));

        CompletableFuture<Boolean> directBusRoute = busRouteService.isDirectBusRoute(depSid, arrSid);
        assertThat(directBusRoute.join(), equalTo(true));

        CompletableFuture<Boolean> invalidBusRoute = busRouteService.isDirectBusRoute(depSid, 0);
        assertThat(invalidBusRoute.join(), equalTo(false));
    }

}
