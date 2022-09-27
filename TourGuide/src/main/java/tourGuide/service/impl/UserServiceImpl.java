package tourGuide.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.exceptions.UserAlreadyExistException;
import tourGuide.exceptions.UserNotFoundException;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IUserService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@Service
public class UserServiceImpl implements IUserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final IGpsUtilService gpsUtilService;
    public final Map<String, User> internalUserMap = new HashMap<>();
    public boolean testMode = true;

    public UserServiceImpl(IGpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
        if (testMode) {
            LOGGER.info("[SERVICE] Test mode enabled");
            LOGGER.info("[SERVICE] Initializing users");
            InitializingUsers();
            LOGGER.info("[SERVICE] Finished initializing users");
        }
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
        boolean response = internalUserMap.containsKey(userName);
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
            return internalUserMap.get(userName);
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
            internalUserMap.put(userName, user);
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
    //TODO GPSUTILSERVICE REFACTOR ?
    @Override
    public VisitedLocationBean getUserLocation(User user) {
        LOGGER.info("[SERVICE] Call UserServiceImpl method: getUserLocation(" + user + ")");
        if (user.getVisitedLocations().size() == 0) {
            gpsUtilService.getAndAddUserLocation(user);
        }
        return user.getLastVisitedLocation();
    }

    /**
     * Get all users of our internal list
     *
     * @return A list of Users
     */
    @Override
    public List<User> getAllUsers() {
        LOGGER.info("[SERVICE] Call UserServiceImpl method: getAllUsers()");
        return new ArrayList<>(internalUserMap.values());
    }


    /* *********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     * *********************************************************************************/


    /**
     * Populates the userMap with a number of users defined in internalTestHelper.internalUserNumber
     *
     * @see tourGuide/helper/InternalTestHelper.java
     */
    public void InitializingUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUserTest" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);
            internalUserMap.put(userName, user);
        });
        LOGGER.info("[HELPER] Number of users created = " + InternalTestHelper.getInternalUserNumber());
    }

    /**
     * Generates 3 VisitedLocationBean objects which contain random data
     * and which are added to the User object
     *
     * @param user The user to whom we will add the generated Visited location history
     */
    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(
                    new VisitedLocationBean(
                            user.getUserId(),
                            new LocationBean(
                                    generateRandomLatitude(),
                                    generateRandomLongitude()),
                            getRandomTime()));
        });
    }

    /**
     * Generates a random longitude between two limits
     *
     * @return a double that corresponds to the generated longitude
     */
    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * Generates a random latitude between two limits
     *
     * @return a double that corresponds to the generated latitude
     */
    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * Generates a random past date
     *
     * @return a random LocalDateTime Object before the current date
     */
    private Date getRandomTime() {
        //LOGGER.debug("Call InternalTestData method: getRandomTime()");
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}