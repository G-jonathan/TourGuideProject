package tourGuide.beans;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationBean {
    public final UUID userId;
    public final LocationBean LocationBean;
    public final Date timeVisited;

    public VisitedLocationBean(UUID userId, LocationBean LocationBean, Date timeVisited) {
        this.userId = userId;
        this.LocationBean = LocationBean;
        this.timeVisited = timeVisited;
    }
}