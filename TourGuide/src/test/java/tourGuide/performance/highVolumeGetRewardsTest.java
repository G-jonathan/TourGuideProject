package tourGuide.performance;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 *
 * This class offers tests to measure the performance of our program.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class highVolumeGetRewardsTest {
    private final Logger LOGGER = LoggerFactory.getLogger(highVolumeGetRewardsTest.class);

    //TODO
    /*
    highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));

	@Disabled
	@Test
	public void highVolumeGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsCentralServiceImpl rewardsService = new RewardsCentralServiceImpl(gpsUtil, new RewardCentral());
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		GpsUtilServiceImpl tourGuideService = new GpsUtilServiceImpl(gpsUtil, rewardsService);
		Attraction attraction = gpsUtil.getAttractions().get(0);
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));
		allUsers.forEach(u -> rewardsService.calculateRewards(u));
		for (User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	 */
}