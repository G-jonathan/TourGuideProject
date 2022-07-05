package tourGuide.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;
import tourGuide.service.impl.UserServiceImpl;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@Component
public class InternalTestData {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    public final Map<String, User> internalUserMap = new HashMap<>();

    public InternalTestData() {
        LOGGER.info("Initializing users");
        InitializingUsers();
        LOGGER.info("Finished initializing users");
    }

    /**
     *
     */
    public void InitializingUsers() {
        LOGGER.debug("Call InternalTestData method: InitializingUsers()");
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUserTest" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);
            internalUserMap.put(userName, user);
            LOGGER.debug(userName);
        });
        LOGGER.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
    }

    /**
     *
     * @param user
     */
    private void generateUserLocationHistory(User user) {
        //LOGGER.debug("Call InternalTestData method: generateUserLocationHistory(User user)");
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(new VisitedLocationBean(user.getUserId(), new LocationBean(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    /**
     *
     * @return
     */
    private double generateRandomLongitude() {
        //LOGGER.debug("Call InternalTestData method: generateRandomLongitude()");
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     *
     * @return
     */
    private double generateRandomLatitude() {
        //LOGGER.debug("Call InternalTestData method: generateRandomLatitude()");
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     *
     * @return
     */
    private Date getRandomTime() {
        //LOGGER.debug("Call InternalTestData method: getRandomTime()");
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    /**
     * Getter
     *
     * @return
     */
    public Map<String, User> getInternalUserMap() {
        return internalUserMap;
    }
}