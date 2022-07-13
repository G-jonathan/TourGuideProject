package tourGuide.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.exceptions.UserAlreadyExistException;
import tourGuide.exceptions.UserNotFoundException;
import tourGuide.helper.InternalTestData;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IRewardCentralService;
import tourGuide.service.IUserService;
import tourGuide.tracker.Tracker;
import java.util.*;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@Service
public class UserServiceImpl implements IUserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final IGpsUtilService gpsUtilService;
    private final IRewardCentralService rewardsCentralService;
    public final Tracker tracker;

    @Autowired
    private InternalTestData internalTestData;

    public UserServiceImpl(IGpsUtilService gpsUtilService, IRewardCentralService rewardsCentralService) {
        this.gpsUtilService = gpsUtilService;
        this.rewardsCentralService = rewardsCentralService;
        tracker = new Tracker(this);
        addShutDownHook();
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(tracker::stopTracking));
    }

    /**
     * Determines if the user exists in this application
     *
     * @param userName HashMap key of internalUserMap
     * @return True if user exist, false if user does not exist
     */
    @Override
    public boolean isUserExist(String userName) {
        LOGGER.info("[SERVICE] Call UserServiceImpl method: isUserExist(" + userName + ")");
        boolean response = internalTestData.getInternalUserMap().containsKey(userName);
        LOGGER.debug("[RETURN]:  " + response);
        return response;
    }

    /**
     * Search a user by userName in the list of internal users
     *
     * @param userName HashMap key of internalUserMap
     * @return The user corresponding to the hashmap key
     * @throws UserNotFoundException Custom exception when a user is not found
     */
    @Override
    public User getInternalUser(String userName) throws UserNotFoundException {
        LOGGER.info("[SERVICE] Call UserServiceImpl method: getInternalUser(" + userName + ")");
        if (isUserExist(userName)) {
            return internalTestData.getInternalUserMap().get(userName);
        } else {
            throw new UserNotFoundException("User Not found with: userName= " + userName);
        }
    }

    /**
     * Allows to add a user to this application
     *
     * @param user object
     * @throws UserAlreadyExistException Custom exception when a user already exist on this application
     */
    @Override
    public User addUser(User user) throws UserAlreadyExistException, UserNotFoundException {
        LOGGER.info("[SERVICE] Call UserServiceImpl method: addUser(" + user + ")");
        String userName = user.getUserName();
        if (!isUserExist(userName)) {
            internalTestData.getInternalUserMap().put(userName, user);
            return getInternalUser(userName);
        } else {
            throw new UserAlreadyExistException("User: [" + user.getUserName() + "] already exist");
        }
    }

    /**
     * Get the rewards obtained by this user
     *
     * @param user object
     * @return A list of rewards obtained by this user
     */
    @Override
    public List<UserReward> getUserRewards(User user) {
        LOGGER.info("[SERVICE] Call UserServiceImpl method: getUserRewards(" + user + ")");
        return user.getUserRewards();
    }

    /**
     * Get the last visited location or ,if it's empty, the actual User location
     *
     * @param user object
     * @return A VisitedLocation Object who contain the date and user GPS position
     */
    @Override
    public VisitedLocationBean getUserLocation(User user) {
        LOGGER.info("[SERVICE] Call UserServiceImpl method: getUserLocation(" + user + ")");
        return (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation() : trackUserLocation(user);
    }

    /**
     * Get all users of our internal list
     *
     * @return A list of Users
     */
    @Override
    public List<User> getAllUsers() {
        LOGGER.info("[SERVICE] Call UserServiceImpl method: getAllUsers()");
        return new ArrayList<>(internalTestData.getInternalUserMap().values());
    }

    /**
     * Tracks the current position of the user and add it to its list of user.visitedLocations
     * then call rewardCentral method to calculates the corresponding rewards
     *
     * @param user object
     * @return  A VisitedLocation Object who contain the date and user GPS position
     */
    //TODO REFACTOR THIS METHOD ?
    @Override
    public VisitedLocationBean trackUserLocation(User user) {
        LOGGER.info("[SERVICE] Call UserServiceImpl method: trackUserLocation(" + user + ")");
        VisitedLocationBean visitedLocationBean = gpsUtilService.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocationBean);
        rewardsCentralService.calculateRewards(user);
        return visitedLocationBean;
    }
}