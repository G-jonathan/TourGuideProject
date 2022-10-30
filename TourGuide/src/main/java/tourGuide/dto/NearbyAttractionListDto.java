package tourGuide.dto;

import tourGuide.beans.LocationBean;
import java.util.List;

public class NearbyAttractionListDto {
    private LocationBean userLocation;
    private List<NearbyAttractionDto> nearbyAttractionDtoList;

    public NearbyAttractionListDto(LocationBean userLocation, List<NearbyAttractionDto> nearbyAttractionDtoList) {
        this.userLocation = userLocation;
        this.nearbyAttractionDtoList = nearbyAttractionDtoList;
    }

    public LocationBean getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LocationBean userLocation) {
        this.userLocation = userLocation;
    }

    public List<NearbyAttractionDto> getNearbyAttractionDtoList() {
        return nearbyAttractionDtoList;
    }

    public void setNearbyAttractionDtoList(List<NearbyAttractionDto> nearbyAttractionDtoList) {
        this.nearbyAttractionDtoList = nearbyAttractionDtoList;
    }
}