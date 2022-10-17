package tourGuide.service.impl;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.dto.NearbyAttractionDto;
import tourGuide.model.Location;
import tourGuide.model.User;
import tourGuide.model.UserLocation;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.service.IGpsUtilService;
import tourGuide.utils.DistanceCalculations;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@Service
public class GpsUtilServiceImpl implements IGpsUtilService {
	private final Logger LOGGER = LoggerFactory.getLogger(GpsUtilServiceImpl.class);
	private static final int attractionProximityRange = 200;
	private final MicroserviceGpsUtilProxy gpsUtilProxy;

	@Autowired
	DistanceCalculations distanceCalculations;

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
		LOGGER.info("[SERVICE] Call GpsUtilServiceImpl method: getUserLocation(" + userId + ")");
		return gpsUtilProxy.getUserLocation(userId);
	}

	/**
	 * @param visitedLocation
	 * @return
	 */
	@Override
	public List<NearbyAttractionDto> getNearByAttractions(VisitedLocationBean visitedLocation) {
		LOGGER.info("[SERVICE] Call GpsUtilServiceImpl method: getNearByAttractions(" + visitedLocation + ")");
		List<NearbyAttractionDto> nearbyAttractionsListDto = new ArrayList<>();
		List<AttractionBean> attractionBeanList = gpsUtilProxy.getAttractionList();
		TreeMap<Double, AttractionBean> treeMap = new TreeMap<>();
		for (AttractionBean attractionBean : attractionBeanList) {
			double distance = distanceCalculations.getDistance(attractionBean, visitedLocation.locationBean);
			treeMap.put(distance, attractionBean);
		}
		Set<Map.Entry<Double, AttractionBean>> set = treeMap.entrySet();
		Iterator<Map.Entry<Double, AttractionBean>> iterator = set.iterator();
		int i = 0;
		while (iterator.hasNext() & i < 5) {
			Map.Entry<Double, AttractionBean> entryMap = iterator.next();
			double distance = entryMap.getKey();
			AttractionBean attractionBean = entryMap.getValue();
			NearbyAttractionDto nearbyAttractionDto = new NearbyAttractionDto();
			nearbyAttractionDto.setAttractionId(attractionBean.attractionId);
			nearbyAttractionDto.setAttractionName(attractionBean.attractionName);
			nearbyAttractionDto.setAttractionLocation(new LocationBean(attractionBean.longitude, attractionBean.latitude));
			nearbyAttractionDto.setDistance(distance);
			nearbyAttractionsListDto.add(nearbyAttractionDto);
			i += 1;
		}
		return nearbyAttractionsListDto;
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
		return !(distanceCalculations.getDistance(attraction, location) > attractionProximityRange);
	}

	/**
	 *
	 * @param user
	 * @return
	 */
	@Override
	@Async
	public CompletableFuture<VisitedLocationBean> getAndAddUserLocation(User user) {
		LOGGER.info("[SERVICE] Call GpsUtilServiceImpl method: getAndAddUserLocation()");
		VisitedLocationBean location = gpsUtilProxy.getUserLocation(user.getUserId());
		user.addToVisitedLocations(location);
		return CompletableFuture.completedFuture(location);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<AttractionBean> getAttractionsList() {
		LOGGER.info("[SERVICE] Call GpsUtilServiceImpl method: getAttractionsList()");
		return gpsUtilProxy.getAttractionList();
	}
}