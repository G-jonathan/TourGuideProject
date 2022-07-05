package tourGuide.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.service.IUserService;
import tourGuide.tracker.Tracker;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class UserServiceImpl implements IUserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final MicroserviceGpsUtilProxy gpsUtilProxy;
    private final RewardsCentralService rewardsCentralService;
    public boolean testMode = true;
    public final Tracker tracker;


    public UserServiceImpl(MicroserviceGpsUtilProxy gpsUtilProxy, RewardsCentralService rewardsCentralService) {
        this.gpsUtilProxy = gpsUtilProxy;
        this.rewardsCentralService = rewardsCentralService;
        tracker = new Tracker(this);
        addShutDownHook();
        if (testMode) {
            LOGGER.info("TestMode enabled");
            LOGGER.debug("Initializing users");
            initializeInternalUsers();
            LOGGER.debug("Finished initializing users");
        }
    }

    @Override
    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    @Override
    public VisitedLocationBean getUserLocation(User user) {
        return (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation() : trackUserLocation(user);
    }

    @Override
    public User getUser(String userName) {
        return internalUserMap.get(userName);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(internalUserMap.values());
    }

    @Override
    public void addUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    @Override
    public VisitedLocationBean trackUserLocation(User user) {
        VisitedLocationBean visitedLocation = gpsUtilProxy.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        rewardsCentralService.calculateRewards(user);
        return visitedLocation;
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(tracker::stopTracking));
    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/

    private final Map<String, User> internalUserMap = new HashMap<>();
    private void initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            internalUserMap.put(userName, user);
        });
        LOGGER.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
    }

    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i-> {
            user.addToVisitedLocations(new VisitedLocationBean(user.getUserId(), new LocationBean(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}