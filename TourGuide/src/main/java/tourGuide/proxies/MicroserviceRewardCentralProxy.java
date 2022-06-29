package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@FeignClient(name = "microservice-rewardCentral", url = "localhost:8082")
public interface MicroserviceRewardCentralProxy {

    @GetMapping("/attraction-reward-points")
    int getAttractionRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId);
}