package tourGuide.beans;

import java.util.UUID;

public class ProviderBean {
    public final String name;
    public final double price;
    public final UUID tripId;

    public ProviderBean(String name, double price, UUID tripId) {
        this.name = name;
        this.price = price;
        this.tripId = tripId;
    }
}