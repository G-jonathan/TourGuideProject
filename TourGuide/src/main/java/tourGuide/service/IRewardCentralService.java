package tourGuide.service;

import tourGuide.dto.NearbyAttractionDto;
import tourGuide.model.User;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IRewardCentralService {

    CompletableFuture<User> calculateRewards(User user);

    int getRewardPoints(UUID attractionId, UUID userId);

    List<NearbyAttractionDto> setNearbyAttractionRewardPoints(List<NearbyAttractionDto> nearbyAttractionDtoList, UUID userId);

    void setProximityBufferInMiles(int proximityBufferInMiles);
}