package tourGuide.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tourGuide.beans.LocationBean;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@Component
public class DistanceCalculations {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private final Logger LOGGER = LoggerFactory.getLogger(DistanceCalculations.class);

    /**
     * Calculates the distance between two gps coordinate points
     * Statute mile * 1.15077945 = nautical mile
     * dλ = λ B – λ A
     * S A-B = arc cos (sin ϕ A sin ϕ B + cos ϕ A cos ϕ B cos dλ)
     * A degree of latitude is 60 miles.
     *
     * @param locationBean1 First object Location containing gps coordinates
     * @param locationBean2 Second object Location containing gps coordinates
     * @return The distance between the two coordinates
     */
    public double getDistance(LocationBean locationBean1, LocationBean locationBean2) {
        double latitude1 = Math.toRadians(locationBean1.latitude);
        double longitude1 = Math.toRadians(locationBean1.longitude);
        double latitude2 = Math.toRadians(locationBean2.latitude);
        double longitude2 = Math.toRadians(locationBean2.longitude);
        double angle = Math.acos(Math.sin(latitude1) * Math.sin(latitude2) + Math.cos(latitude1) * Math.cos(latitude2) * Math.cos(longitude1 - longitude2));
        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }
}