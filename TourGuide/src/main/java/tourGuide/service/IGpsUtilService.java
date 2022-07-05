package tourGuide.service;

import tourGuide.beans.AttractionBean;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import java.util.List;

public interface IGpsUtilService {

    List<AttractionBean> getNearByAttractions(VisitedLocationBean visitedLocation);

    boolean isWithinAttractionProximity(AttractionBean attraction, LocationBean location);

    boolean nearAttraction(VisitedLocationBean visitedLocation, AttractionBean attraction);

    double getDistance(LocationBean loc1, LocationBean loc2);
}