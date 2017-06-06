package me.oche.controllers.route.direct;


import com.fasterxml.jackson.annotation.JsonProperty;
public class ConfirmationResponse {
    @JsonProperty("dep_sid")
    private int depSid;

    @JsonProperty("arr_sid")
    private int arrSid;

    @JsonProperty("direct_bus_route")
    private boolean directBusRoute;

    public int getDepSid() {
        return depSid;
    }

    public void setDepSid(int depSid) {
        this.depSid = depSid;
    }

    public int getArrSid() {
        return arrSid;
    }

    public void setArrSid(int arrSid) {
        this.arrSid = arrSid;
    }

    public boolean isDirectBusRoute() {
        return directBusRoute;
    }

    public void setDirectBusRoute(boolean directBusRoute) {
        this.directBusRoute = directBusRoute;
    }
}
