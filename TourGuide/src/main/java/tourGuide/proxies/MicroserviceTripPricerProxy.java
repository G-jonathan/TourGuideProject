package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.beans.ProviderBean;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "microservice-tripPricer", url = "localhost:8083")
public interface MicroserviceTripPricerProxy {

    @GetMapping("/price")
    List<ProviderBean> getPrice(@RequestParam String apiKey,
                                @RequestParam UUID attractionId,
                                @RequestParam int adults,
                                @RequestParam int children,
                                @RequestParam int nightsStay,
                                @RequestParam int rewardsPoints);

    @GetMapping("/provider-name")
    String getProviderName(@RequestParam String apiKey, @RequestParam int adults);
}