package tourGuide.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.Location;
import tourGuide.model.User;
import tourGuide.model.UserLocation;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IRewardCentralService;

@Service
public class GpsUtilServiceImpl implements IGpsUtilService {
	private final Logger LOGGER = LoggerFactory.getLogger(GpsUtilServiceImpl.class);
	private final MicroserviceGpsUtilProxy gpsUtilProxy;
	private static final int attractionProximityRange = 200;
	private final ExecutorService executorService = Executors.newFixedThreadPool(10000);

	@Autowired
	IRewardCentralService rewardCentralService;

	public GpsUtilServiceImpl(MicroserviceGpsUtilProxy gpsUtilProxy) {
		this.gpsUtilProxy = gpsUtilProxy;
	}


	/**
	 *
	 * @param userList
	 * @return
	 */
	@Override
	public List<UserLocation> getAllCurrentLocations(List<User> userList) {
		LOGGER.info("[SERVICE] Call GpsUtilServiceImpl method: getAllCurrentLocations(" + userList + ")");
		List<UserLocation> userLocationList = new ArrayList<>();
		userList.forEach(user -> userLocationList.add(
				new UserLocation(
						user.getUserId().toString(),
						new Location(
								user.getLastVisitedLocation().locationBean.longitude,
								user.getLastVisitedLocation().locationBean.latitude
						)
				)
		));
		return userLocationList;
	}

	/**
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public VisitedLocationBean getUserLocation(UUID userId) {
		LOGGER.info("[SERVICE] Call GpsUtilServiceImpl method: getNearByAttractions(" + userId + ")");
		return gpsUtilProxy.getUserLocation(userId);
	}

	/**
	 *
	 * @param visitedLocation
	 * @return
	 */
	@Override
	public List<AttractionBean> getNearByAttractions(VisitedLocationBean visitedLocation) {
		LOGGER.info("[SERVICE] Call GpsUtilServiceImpl method: getNearByAttractions(" + visitedLocation + ")");
		List<AttractionBean> nearbyAttractions = new ArrayList<>();
		for (AttractionBean attraction : gpsUtilProxy.getAttractionList()) {
			if (isWithinAttractionProximity(attraction, visitedLocation.locationBean)) {
				nearbyAttractions.add(attraction);
			}
		}
		return nearbyAttractions;
	}


	/**
	 *
	 * @param attraction
	 * @param location
	 * @return
	 */
	@Override
	public boolean isWithinAttractionProximity(AttractionBean attraction, LocationBean location) {
		LOGGER.info("[SERVICE] Call GpsUtilServiceImpl method: isWithinAttractionProximity(" + attraction + ", " + location + ")");
		return !(rewardCentralService.getDistance(attraction, location) > attractionProximityRange);
	}

	/**
	 * Tracks the current position of the user and add it to its list of user.visitedLocations
	 * then call rewardCentral method to calculates the corresponding rewards
	 *
	 * @param user object
	 */
	@Override
	public void trackUserLocation(User user) {
		LOGGER.info("[SERVICE] Call GpsUtilServiceImpl method: trackUserLocation(" + user + ")");
		CompletableFuture.supplyAsync(() -> getUserLocation(user.getUserId()), executorService)
				.thenAccept(visitedLocationBean -> {
					user.addToVisitedLocations(visitedLocationBean);
					rewardCentralService.calculateRewards(user);
				});
	}
}