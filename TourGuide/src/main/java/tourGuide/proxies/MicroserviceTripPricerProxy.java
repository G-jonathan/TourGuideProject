package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.beans.ProviderBean;
import java.util.List;
import java.util.UUID;

/**
 * This class is used to perform requests on microservice-tripPricer
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@FeignClient(name = "microservice-tripPricer", url = "localhost:8083")
public interface MicroserviceTripPricerProxy {

    /**
     * This method allow you to calculate, for an attraction, the providers price according to the parameters requested:
     * number of adults, number of children, number of nights, reward points
     *
     * @param apiKey        It's a unique identifier that authenticates requests
     * @param attractionId  Unique identifier of an attraction
     * @param adults        Integer, number of adults
     * @param children      Integer, number of children
     * @param nightsStay    Integer, number of nights required
     * @param rewardsPoints Integer corresponding to the number of reward points
     * @return A list of ProviderBean object. Each object contain an id, a name and a price
     */
    @GetMapping("/price")
    List<ProviderBean> getPrice(@RequestParam String apiKey,
                                @RequestParam UUID attractionId,
                                @RequestParam int adults,
                                @RequestParam int children,
                                @RequestParam int nightsStay,
                                @RequestParam int rewardsPoints);

    /**
     * @param apiKey It's a unique identifier that authenticates requests
     * @param adults integer, number of adults
     * @return A String corresponding
     */
    @GetMapping("/provider-name")
    String getProviderName(@RequestParam String apiKey, @RequestParam int adults);
}