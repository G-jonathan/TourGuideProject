package tourGuide.service;

import tourGuide.model.User;
import java.util.concurrent.CompletableFuture;

public interface IRewardCentralService {

    CompletableFuture<User> calculateRewards(User user);
}