package tourGuide.model;

/**
 * Data Transfer Object exposed by the controller
 */
public class UserLocation {

    private String userId;
    private Location location;

    public UserLocation(String userId, Location location) {
        this.userId = userId;
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}