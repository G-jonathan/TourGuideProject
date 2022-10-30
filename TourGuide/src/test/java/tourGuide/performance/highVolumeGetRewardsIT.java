package tourGuide.performance;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IRewardCentralService;
import tourGuide.service.impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 *
 * This class offers tests to measure the performance of our program.
 */
@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class highVolumeGetRewardsIT {
    private final Logger LOGGER = LoggerFactory.getLogger(highVolumeGetRewardsIT.class);

    @Autowired
    private IGpsUtilService gpsUtilService;

    @Autowired
    private IRewardCentralService rewardCentralService;

    /**
     * The number of users generated for the high volume tests is adjusted via the InternalTestHelper.setInternalUserNumber() method.
     * In the BeforeAll method of Junit, we ensure that it is correctly set before the spring automatic dependency injection.
     */
    @BeforeAll
    static void beforeAll() {
        InternalTestHelper.setInternalUserNumber(100000);
    }

    /**
     * With 100 000 users, test should finish within 20 minutes.
     */
    @Test
    public void highVolumeGetRewards(){
        UserServiceImpl userService = new UserServiceImpl(gpsUtilService);
        AttractionBean attractionBean = gpsUtilService.getAttractionsList().get(0);
        List<User> allUsers = userService.getAllUsers();
        allUsers.forEach(user -> {
            user.clearVisitedLocations();
            user.addToVisitedLocations(new VisitedLocationBean(user.getUserId(), attractionBean, new Date()));
        });
        List<CompletableFuture<?>> allFutures = new ArrayList<>();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (User user : allUsers) {
            allFutures.add(rewardCentralService.calculateRewards(user));
        }
        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();
        stopWatch.stop();
        for (User user : allUsers) {
            assertEquals(1, user.getUserRewards().size());
            assertEquals(user.getLastVisitedLocation().locationBean, user.getUserRewards().get(0).visitedLocation.locationBean);
        }
        LOGGER.info("[TEST] highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
}