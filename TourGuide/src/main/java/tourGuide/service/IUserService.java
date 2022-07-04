package tourGuide.service;

import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import java.util.List;

public interface IUserService {

    List<UserReward> getUserRewards(User user);

    VisitedLocationBean getUserLocation(User user);

    User getUser(String userName);

    List<User> getAllUsers();

    void addUser(User user);

    VisitedLocationBean trackUserLocation(User user);
}