package me.oche.service;

import me.oche.repository.BusRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Filter direct connections for bus routes.
 */
@Service
public class BusRouteService {

    private BusRouteRepository busRouteRepository;

    @Autowired
    public BusRouteService(BusRouteRepository busRouteRepository) {
        this.busRouteRepository = busRouteRepository;
    }

    /**
     * Returns true iff. two given stations are connected by a single bus route.
     *
     * @param depSid department station id (sid)
     * @param arrSid arrival station id (sid)
     * @return a completable future object with the result
     */
    public CompletableFuture<Boolean> isDirectBusRoute(int depSid, int arrSid) throws IOException {
        CompletableFuture<Map<Integer, Set<Integer>>> stationToRoutes = busRouteRepository.getStationToRoutes();
        return stationToRoutes.thenApply(routes -> {
            if(!routes.containsKey(depSid) || !routes.containsKey(arrSid)) {
                return false;
            }
            final Set<Integer> routesContainingDep = routes.get(depSid);
            final Set<Integer> routesContainingSid = routes.get(arrSid);
            routesContainingDep.retainAll(routesContainingSid);

            if(routesContainingDep.size() > 0) {
                return true;
            }
            return false;
        });
    }

}