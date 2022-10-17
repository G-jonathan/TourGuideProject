package tourGuide.beans;

public class LocationBean {
    public final double longitude;
    public final double latitude;

    public LocationBean(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}