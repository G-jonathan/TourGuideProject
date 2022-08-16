package tourGuide.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.UUID;

public class VisitedLocationBean {

    public final UUID userId;

    @JsonProperty("location")
    public final LocationBean locationBean;
    public final Date timeVisited;

    public VisitedLocationBean(UUID userId, LocationBean LocationBean, Date timeVisited) {
        this.userId = userId;
        this.locationBean = LocationBean;
        this.timeVisited = timeVisited;
    }
}