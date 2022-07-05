package tourGuide.service;

import tourGuide.beans.ProviderBean;
import tourGuide.model.User;
import java.util.List;

public interface ITripPricerService {

    List<ProviderBean> getTripDeals(User user);
}