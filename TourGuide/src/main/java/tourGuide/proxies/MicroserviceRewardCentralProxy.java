package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

/**
 * This class is used to perform requests on microservice-rewardCentral
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@FeignClient(name = "microservice-rewardCentral", url = "localhost:8082")
public interface MicroserviceRewardCentralProxy {

    /**
     * This method allows to obtain the reward points earned with an attraction
     *
     * @param attractionId The attraction id
     * @param userId The user id
     * @return An integer corresponding to the number of reward points
     */
    @GetMapping("/attraction-reward-points")
    int getAttractionRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId);
}