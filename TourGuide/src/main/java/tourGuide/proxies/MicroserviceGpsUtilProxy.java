package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.VisitedLocationBean;
import java.util.List;
import java.util.UUID;

/**
 * This class is used to perform requests on microservice-gpsUtil
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@FeignClient(name = "microservice-gpsUtil", url = "localhost:8081")
public interface MicroserviceGpsUtilProxy {

    /**
     * This method provides the list of possible attractions
     *
     * @return A list of attractions
     */
    @GetMapping(value = "/attractions")
    List<AttractionBean> getAttractionList();

    /**
     * This method allow to obtain the current location of a user
     *
     * @param userId The user id
     * @return A VisitedLocationBean object who contains the user id, the location of the mobile phone or the user's laptop
     * and the actual date.
     */
    @GetMapping("/user-location")
    VisitedLocationBean getUserLocation(@RequestParam UUID userId);
}