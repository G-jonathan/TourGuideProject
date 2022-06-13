package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.proxies.MicroserviceGpsUtilProxy;

@RestController
public class TestController {

    private final MicroserviceGpsUtilProxy microserviceGpsUtilProxy;

    public TestController(MicroserviceGpsUtilProxy microserviceGpsUtilProxy) {
        this.microserviceGpsUtilProxy = microserviceGpsUtilProxy;
    }

    @RequestMapping("/test")
    public String GetAttractionList() {
        return JsonStream.serialize(microserviceGpsUtilProxy.getAttractionList());
    }
}