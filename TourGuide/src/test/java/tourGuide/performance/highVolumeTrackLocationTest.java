package tourGuide.performance;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IUserService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 *
 * This class offers tests to measure the performance of our program.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class highVolumeTrackLocationTest {
    private final Logger LOGGER = LoggerFactory.getLogger(highVolumeTrackLocationTest.class);

    /**
     * The number of users generated for the high volume tests is adjusted via the InternalTestHelper.setInternalUserNumber() method.
     * In the BeforeAll method of Junit, we ensure that it is correctly set before the spring automatic dependency injection.
     */
    @BeforeAll
    static void beforeAll() {
        InternalTestHelper.setInternalUserNumber(100);
    }

    @Autowired
    private IUserService userService;

    @Autowired
    private IGpsUtilService gpsUtilService;

    /**
     * With 100 000 users, test should finish within 15 minutes.
     */
    @Test
    public void highVolumeTrackLocation() {
        List<User> allUsers = userService.getAllUsers();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (User user : allUsers) {
            gpsUtilService.trackUserLocation(user);
        }
        stopWatch.stop();
        userService.tracker.stopTracking();
        LOGGER.info("[TEST] highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
}