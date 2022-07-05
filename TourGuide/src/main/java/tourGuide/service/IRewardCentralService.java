package tourGuide.service;

import tourGuide.beans.AttractionBean;
import tourGuide.model.User;

public interface IRewardCentralService {

    int getRewardPoints(AttractionBean attraction, User user);

    void calculateRewards(User user);
}