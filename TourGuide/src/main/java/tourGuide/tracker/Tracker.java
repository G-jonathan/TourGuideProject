package tourGuide.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IRewardCentralService;
import tourGuide.service.IUserService;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@Component
@Profile("!test")
public class Tracker extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(1);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private boolean stop = false;
	private final IUserService userServiceImpl;
	private final IGpsUtilService gpsUtilService;
	private final IRewardCentralService rewardCentralService;

	public Tracker(IUserService userServiceImpl, IGpsUtilService gpsUtilService, IRewardCentralService rewardCentralService1) {
		LOGGER.debug("[TRACKER] Tracker initialisation");
		this.userServiceImpl = userServiceImpl;
		this.gpsUtilService = gpsUtilService;
		this.rewardCentralService = rewardCentralService1;
		addShutDownHook();
		executorService.submit(this);
	}

	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		LOGGER.debug("[TRACKER] Stop tracking");
		stop = true;
		executorService.shutdownNow();
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::stopTracking));
	}

	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				LOGGER.debug("[TRACKER] Tracker stopping");
				break;
			}
			List<User> users = userServiceImpl.getAllUsers();
			LOGGER.debug("[TRACKER] Begin Tracker. Tracking " + users.size() + " users.");
			List<CompletableFuture<?>> allFuturesGetAndAddUserLocation = new ArrayList<>();
			List<CompletableFuture<?>> allFuturesCalculateRewards = new ArrayList<>();
			stopWatch.start();
			for (User user : users) {
				allFuturesGetAndAddUserLocation.add(gpsUtilService.getAndAddUserLocation(user));
			}
			allFuturesGetAndAddUserLocation.forEach(CompletableFuture::join);
			for (User user : users) {
				allFuturesCalculateRewards.add(rewardCentralService.calculateRewards(user));
			}
			allFuturesCalculateRewards.forEach(CompletableFuture::join);
			stopWatch.stop();
			LOGGER.debug("[TRACKER] Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				LOGGER.debug("[TRACKER] Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}