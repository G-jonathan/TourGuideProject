package tourGuide.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.proxies.MicroserviceRewardCentralProxy;
import tourGuide.service.IRewardCentralService;

@Service
public class RewardsCentralServiceImpl implements IRewardCentralService {
	private final MicroserviceGpsUtilProxy gpsUtilProxy;
	private final MicroserviceRewardCentralProxy rewardCentralProxy;

	private final Logger LOGGER = LoggerFactory.getLogger(RewardsCentralServiceImpl.class);

	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	// proximity in miles
	private final int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;

	public RewardsCentralServiceImpl(MicroserviceGpsUtilProxy gpsUtilProxy, MicroserviceRewardCentralProxy rewardCentralProxy) {
		this.gpsUtilProxy = gpsUtilProxy;
		this.rewardCentralProxy = rewardCentralProxy;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	@Override
	public int getRewardPoints(AttractionBean attraction, User user) {
		return rewardCentralProxy.getAttractionRewardPoints(attraction.id, user.getUserId());
	}

	/**
	 *
	 * @param visitedLocation
	 * @param attraction
	 * @return
	 */
	@Override
	public boolean nearAttraction(VisitedLocationBean visitedLocation, AttractionBean attraction) {
		return !(getDistance(attraction, visitedLocation.locationBean) > proximityBuffer);
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
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);
		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
		double nauticalMiles = 60 * Math.toDegrees(angle);
		return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	}

	@Override
	public void calculateRewards(User user) {
		List<VisitedLocationBean> userLocations = user.getVisitedLocations();
		List<AttractionBean> attractions = gpsUtilProxy.getAttractionList();
		int i =0;
		for (VisitedLocationBean visitedLocation : userLocations) {
			i = i+1;
			for (AttractionBean attraction : attractions) {
				if (user.getUserRewards().stream().noneMatch(r -> r.attraction.name.equals(attraction.name))) {
					if (nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
	}
}