package tourGuide.dto;

import tourGuide.beans.LocationBean;
import java.util.UUID;

public class NearbyAttractionDto {
    private UUID attractionId;
    private String attractionName;
    private LocationBean attractionLocation;
    private double distance;
    private int rewardPoints;

    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
    }
    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public LocationBean getAttractionLocation() {
        return attractionLocation;
    }

    public void setAttractionLocation(LocationBean attractionLocation) {
        this.attractionLocation = attractionLocation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}