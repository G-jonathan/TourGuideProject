package tourGuide.service;

import tourGuide.beans.VisitedLocationBean;
import tourGuide.exceptions.UserAlreadyExistException;
import tourGuide.exceptions.UserNotFoundException;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserReward> getUserRewards(User user);

    VisitedLocationBean getUserLocation(User user);

    User getInternalUser(String userName) throws UserNotFoundException;

    List<User> getAllUsers();

    User addUser(User user) throws UserAlreadyExistException, UserNotFoundException;

    VisitedLocationBean trackUserLocation(User user);

    boolean isUserExist(String userName);
}