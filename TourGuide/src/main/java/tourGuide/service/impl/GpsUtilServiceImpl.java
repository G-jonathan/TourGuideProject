package tourGuide.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.Location;
import tourGuide.model.User;
import tourGuide.model.UserLocation;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.service.IGpsUtilService;

@Service
public class GpsUtilServiceImpl implements IGpsUtilService {
	private final Logger LOGGER = LoggerFactory.getLogger(GpsUtilServiceImpl.class);
	private final MicroserviceGpsUtilProxy gpsUtilProxy;
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	// proximity in miles
	private final int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private static final int attractionProximityRange = 200;

	public GpsUtilServiceImpl(MicroserviceGpsUtilProxy gpsUtilProxy) {
		this.gpsUtilProxy = gpsUtilProxy;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
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
								user.getLastVisitedLocation().LocationBean.longitude,
								user.getLastVisitedLocation().LocationBean.latitude
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
		return gpsUtilProxy.getUserLocation(userId);
	}

	/**
	 *
	 * @param visitedLocation
	 * @return
	 */
	@Override
	public List<AttractionBean> getNearByAttractions(VisitedLocationBean visitedLocation) {
		List<AttractionBean> nearbyAttractions = new ArrayList<>();
		for (AttractionBean attraction : gpsUtilProxy.getAttractionList()) {
			if (isWithinAttractionProximity(attraction, visitedLocation.LocationBean)) {
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
		return !(getDistance(attraction, location) > attractionProximityRange);
	}

	/**
	 *
	 * @param visitedLocation
	 * @param attraction
	 * @return
	 */
	@Override
	public boolean nearAttraction(VisitedLocationBean visitedLocation, AttractionBean attraction) {
		return !(getDistance(attraction, visitedLocation.LocationBean) > proximityBuffer);
	}

	/**
	 *
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	@Override
	public double getDistance(LocationBean loc1, LocationBean loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		LOGGER.info("ICI --> " + loc2.toString());
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);
		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
		double nauticalMiles = 60 * Math.toDegrees(angle);
		return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	}
}