package tourGuide.service;

import tourGuide.beans.VisitedLocationBean;
import tourGuide.exceptions.UserAlreadyExistException;
import tourGuide.exceptions.UserNotFoundException;
import tourGuide.model.User;
import tourGuide.model.UserPreferences;
import tourGuide.model.UserReward;
import java.util.List;

public interface IUserService {

    List<UserReward> getUserRewards(User user);

    VisitedLocationBean getUserLocation(User user);

    User getInternalUser(String userName) throws UserNotFoundException;

    List<User> getAllUsers();

    User addUser(User user) throws UserAlreadyExistException, UserNotFoundException;

    boolean isUserExist(String userName);

    void updateUserPreferences(String userName, UserPreferences userPreferences) throws UserNotFoundException;
}