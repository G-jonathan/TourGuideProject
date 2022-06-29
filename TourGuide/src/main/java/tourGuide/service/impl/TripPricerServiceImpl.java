package tourGuide.service.impl;

import org.springframework.stereotype.Service;
import tourGuide.beans.ProviderBean;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.proxies.MicroserviceTripPricerProxy;
import tourGuide.service.ITripPricerService;
import java.util.List;

@Service
public class TripPricerServiceImpl implements ITripPricerService {
    private final MicroserviceTripPricerProxy tripPricerProxy;
    private static final String tripPricerApiKey = "test-server-api-key";

    public TripPricerServiceImpl(MicroserviceTripPricerProxy tripPricerProxy) {
        this.tripPricerProxy = tripPricerProxy;
    }

    @Override
    public List<ProviderBean> getTripDeals(User user) {
        int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(UserReward::getRewardPoints).sum();
        List<ProviderBean> providers = tripPricerProxy.getPrice(
                tripPricerApiKey,
                user.getUserId(),
                user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(),
                user.getUserPreferences().getTripDuration(),
                cumulativeRewardPoints
        );
        user.setTripDeals(providers);
        return providers;
    }
}