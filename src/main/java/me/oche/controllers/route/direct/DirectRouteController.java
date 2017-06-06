package me.oche.controllers.route.direct;

import me.oche.service.BusRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class DirectRouteController {
    private static final Logger logger = LoggerFactory.getLogger(DirectRouteController.class);

    private BusRouteService busRouteService;

    @Autowired
    public DirectRouteController(BusRouteService busRouteService) {
        this.busRouteService = busRouteService;
    }

    /**
     * Check whether two stations are connected by a single bus route.
     *
     * @param depSid department station id (sid)
     * @param arrSid arrival station id (sid)
     * @return a completable future object with the response entity
     */
    @RequestMapping(value = "/direct")
    public CompletableFuture hasDirectBusRoute(@RequestParam(name = "dep_sid") int depSid,
                                               @RequestParam(name = "arr_sid") int arrSid) {
        try {
            ConfirmationResponse response = new ConfirmationResponse();
            response.setDepSid(depSid);
            response.setArrSid(arrSid);

            return busRouteService.isDirectBusRoute(depSid, arrSid).thenApply(result -> {
                response.setDirectBusRoute(result);
                return CompletableFuture.completedFuture(ResponseEntity.ok().body(response));
            });
        } catch (IOException e) {
            logger.error(e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }
}
