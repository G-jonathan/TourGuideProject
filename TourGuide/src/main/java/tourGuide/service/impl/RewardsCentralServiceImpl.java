package tourGuide.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.proxies.MicroserviceRewardCentralProxy;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IRewardCentralService;

@Service
public class RewardsCentralServiceImpl implements IRewardCentralService {
	private final MicroserviceGpsUtilProxy gpsUtilProxy;
	private final MicroserviceRewardCentralProxy rewardCentralProxy;

	@Autowired
	private IGpsUtilService gpsUtilService;

	public RewardsCentralServiceImpl(MicroserviceGpsUtilProxy gpsUtilProxy, MicroserviceRewardCentralProxy rewardCentralProxy) {
		this.gpsUtilProxy = gpsUtilProxy;
		this.rewardCentralProxy = rewardCentralProxy;
	}

	@Override
	public int getRewardPoints(AttractionBean attraction, User user) {
		return rewardCentralProxy.getAttractionRewardPoints(attraction.id, user.getUserId());
	}

	@Override
	public void calculateRewards(User user) {
		List<VisitedLocationBean> userLocations = user.getVisitedLocations();
		List<AttractionBean> attractions = gpsUtilProxy.getAttractionList();
		for (VisitedLocationBean visitedLocation : userLocations) {
			for (AttractionBean attraction : attractions) {
				if (user.getUserRewards().stream().noneMatch(r -> r.attraction.name.equals(attraction.name))) {
					if (gpsUtilService.nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
	}
}