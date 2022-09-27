package tourGuide.service;

import tourGuide.beans.AttractionBean;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;
import tourGuide.model.UserLocation;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IGpsUtilService {

    List<UserLocation> getAllCurrentLocations(List<User> userList);

    VisitedLocationBean getUserLocation(UUID userId);

    List<AttractionBean> getNearByAttractions(VisitedLocationBean visitedLocation);

    boolean isWithinAttractionProximity(AttractionBean attraction, LocationBean location);

    List<AttractionBean> getAttractionsList();

    CompletableFuture<VisitedLocationBean> getAndAddUserLocation(User user);
}