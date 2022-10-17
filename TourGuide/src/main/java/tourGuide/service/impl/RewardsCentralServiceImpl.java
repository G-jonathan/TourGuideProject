package tourGuide.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.dto.NearbyAttractionDto;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.proxies.MicroserviceRewardCentralProxy;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IRewardCentralService;
import tourGuide.utils.DistanceCalculations;

/**
 * Provides methods for manipulating UserRewards and call the MicroserviceRewardCentralProxy
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@Service
public class RewardsCentralServiceImpl implements IRewardCentralService {
	private final Logger LOGGER = LoggerFactory.getLogger(RewardsCentralServiceImpl.class);
	private final int defaultProximityBufferInMiles = 10;
	private int proximityBufferInMiles = defaultProximityBufferInMiles;
	private final MicroserviceRewardCentralProxy rewardCentralProxy;

	@Autowired
	DistanceCalculations distanceCalculations;

	@Autowired
	IGpsUtilService gpsUtilService;

	public RewardsCentralServiceImpl(MicroserviceRewardCentralProxy rewardCentralProxy) {
		this.rewardCentralProxy = rewardCentralProxy;
	}

	public void setProximityBufferInMiles(int proximityBufferInMiles) {
		this.proximityBufferInMiles = proximityBufferInMiles;
	}

	public void setDefaultProximityBuffer() {
		proximityBufferInMiles = defaultProximityBufferInMiles;
	}

	/**
	 * For each available attraction, if the user has not yet received a reward
	 * then we check if his position is close to this attraction.
	 * If so, the corresponding rewards are calculated and added to the user.
	 *
	 * @param user The user for whom we calculate their rewards
	 * @return User object after the asynchronous computation
	 */
	@Override
	@Async
	public CompletableFuture<User> calculateRewards(User user) {
		LOGGER.info("[SERVICE] Call RewardsCentralServiceImpl method: calculateRewards()");
		List<AttractionBean> attractions = gpsUtilService.getAttractionsList();
		List<VisitedLocationBean> visitedLocationList = new ArrayList<>(user.getVisitedLocations());
		for (VisitedLocationBean visitedLocation : visitedLocationList) {
			for (AttractionBean attraction : attractions) {
				System.out.println("ENTREE 111111");
				if (user.getUserRewards().stream().noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName))) {
					System.out.println("ENTREE 222222");
					if (distanceCalculations.getDistance(attraction, visitedLocation.locationBean) <= proximityBufferInMiles) {
						System.out.println("ENTREE 333333");
						int rewardPoints = rewardCentralProxy.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
						user.addUserReward(new UserReward(visitedLocation, attraction, rewardPoints));
					}
				}
			}
		}
		return CompletableFuture.completedFuture(user);
	}

	/**
	 * Get a user's rewards points for an attraction
	 *
	 * @param attractionId attraction id
	 * @param userId       user id
	 * @return an integer, the reward points
	 */
	@Override
	public int getRewardPoints(UUID attractionId, UUID userId) {
		LOGGER.info("[SERVICE] Call RewardsCentralServiceImpl method: getRewardPoints()");
		return rewardCentralProxy.getAttractionRewardPoints(attractionId, userId);
	}

	/**
	 * Set rewards points to a NearbyAttractionDList
	 *
	 * @param nearbyAttractionDtoList a list of NearbyAttractionDto object
	 * @param userId                  The user for whom the nearby locations rewards are determined
	 * @return the completed list with the reward points
	 */
	@Override
	public List<NearbyAttractionDto> setNearbyAttractionRewardPoints(List<NearbyAttractionDto> nearbyAttractionDtoList, UUID userId) {
		LOGGER.info("[SERVICE] Call RewardsCentralServiceImpl method: setNearbyAttractionRewardPoints()");
		for (NearbyAttractionDto nearbyAttractionDto : nearbyAttractionDtoList) {
			int points = rewardCentralProxy.getAttractionRewardPoints(nearbyAttractionDto.getAttractionId(), userId);
			nearbyAttractionDto.setRewardPoints(points);
		}
		return nearbyAttractionDtoList;
	}
}