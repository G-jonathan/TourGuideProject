package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.VisitedLocationBean;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "microservice-gpsUtil", url = "localhost:8081")
public interface MicroserviceGpsUtilProxy {

    @GetMapping(value = "/attractions")
    List<AttractionBean> getAttractionList();

    @GetMapping("/user-location")
    VisitedLocationBean getUserLocation(@RequestParam UUID userId);
}