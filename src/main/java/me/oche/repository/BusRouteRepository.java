package me.oche.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Repository
public class BusRouteRepository {
    private static final Logger logger = LoggerFactory.getLogger(BusRouteRepository.class);

    private File data;

    private Map<Integer, Set<Integer>> stationToRoutes;

    @Autowired
    public BusRouteRepository(@Value("${data}") File data) {
        this.data = data;
        try {
            reloadBusRoutes();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Data file could not be parsed!");
        }
    }

    /**
     * Get all stations to their bus route ids as a {@link Map} where a key is station id and
     * a value is a {@link Set} of all routes this station appear in.
     *
     * @return a completable future object with bus routes
     * @throws IOException
     */
    @Async
    public CompletableFuture<Map<Integer, Set<Integer>>> getStationToRoutes() throws IOException {
        if (Objects.isNull(stationToRoutes) || stationToRoutes.isEmpty()) {
            reloadBusRoutes();
        }

        return CompletableFuture.completedFuture(this.stationToRoutes);
    }

    /**
     * Reload all bus routes from the data file.
     *
     * Data file will be automatically reloaded once a week on Monday at 6 a.m. CET.
     *
     * @throws IOException
     */
    @Scheduled(cron = "0 0 6 * * MON", zone = "CET")
    void reloadBusRoutes() throws IOException {
        logger.info("Loading data file ...");

        final List<List<Integer>> idPlusRouteInfo = Files.lines(data.toPath())
                .skip(1)
                .map(line -> line.split("\\s+"))
                .filter(num -> num.length > 2)
                .map(this::mapArrayOfStringToListOfInteger)
                .collect(Collectors.toList());
        stationToRoutes = splitRawRouteInfoToStationToRoutes(idPlusRouteInfo);

        logger.info("Data file reloading complete. Num of stations processed: " + stationToRoutes.keySet().size());
    }

    private List<Integer> mapArrayOfStringToListOfInteger(String[] arr) {
        return Arrays.stream(arr)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private Map<Integer, Set<Integer>> splitRawRouteInfoToStationToRoutes(List<List<Integer>> idPlusRoutes) {
        Map<Integer, Set<Integer>> result = new HashMap<>();

        idPlusRoutes.forEach(idPlusRoute -> {
            int routeId = idPlusRoute.get(0);
            for(int i=1; i<idPlusRoute.size(); i++) {
                int station = idPlusRoute.get(i);
                if(result.containsKey(station)) {
                    result.get(station).add(routeId);
                } else {
                    Set<Integer> routeIds = new HashSet<>();
                    routeIds.add(routeId);
                    result.put(station, routeIds);
                }
            }
        });
        return result;
    }

}
