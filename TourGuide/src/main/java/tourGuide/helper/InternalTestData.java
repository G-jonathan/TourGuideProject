package tourGuide.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;
import tourGuide.service.impl.UserServiceImpl;
import tourGuide.tracker.Tracker;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

/**
 * For internal testing
 * Provide a user list stored in memory
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@Component
public class InternalTestData {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    public final Map<String, User> internalUserMap = new HashMap<>();
    public final Tracker tracker;

    public InternalTestData() {
        LOGGER.info("[HELPER] Initializing users");
        InitializingUsers();
        LOGGER.info("[HELPER] Finished initializing users");
        tracker = new Tracker();
        addShutDownHook();
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(tracker::stopTracking));
    }

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

    /**
     * Getter
     *
     * @return a list of Users
     */
    public Map<String, User> getInternalUserMap() {
        return internalUserMap;
    }
}