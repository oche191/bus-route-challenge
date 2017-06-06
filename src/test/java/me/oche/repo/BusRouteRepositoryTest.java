package me.oche.repo;


import me.oche.BaseTest;
import me.oche.repository.BusRouteRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class BusRouteRepositoryTest extends BaseTest {
    @Autowired
    private BusRouteRepository busRouteRepository;

    @Test
    public void getBusRoutesTest() throws IOException {
        Map<Integer, Set<Integer>> stationToRoute = busRouteRepository.getStationToRoutes().join();

        assertThat(stationToRoute.size(), equalTo(24));

        int sid = 148;
        Set<Integer> routesForSid = stationToRoute.get(sid);
        assertThat(routesForSid.size(), equalTo(6));
        assertThat(routesForSid.contains(7), is(true));
    }
}
