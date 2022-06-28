package tourGuide.beans;

import java.util.UUID;

public class AttractionBean extends LocationBean {

    public final String name;
    public final String city;
    public final String state;
    public final UUID id;

    public AttractionBean(String name, String city, String state, UUID id, double latitude, double longitude) {
        super(latitude, longitude);
        this.name = name;
        this.city = city;
        this.state = state;
        this.id = id;
    }
}