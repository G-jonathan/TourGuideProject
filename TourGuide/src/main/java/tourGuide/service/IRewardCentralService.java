package tourGuide.service;

import tourGuide.beans.AttractionBean;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;

public interface IRewardCentralService {

    int getRewardPoints(AttractionBean attraction, User user);

    void calculateRewards(User user);

    boolean nearAttraction(VisitedLocationBean visitedLocation, AttractionBean attraction);

    double getDistance(LocationBean loc1, LocationBean loc2);
}