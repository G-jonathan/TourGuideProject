package tourGuide.controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jsoniter.output.JsonStream;
import tourGuide.beans.ProviderBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.exceptions.UserNotFoundException;
import tourGuide.model.UserLocation;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.ITripPricerService;
import tourGuide.service.IUserService;
import tourGuide.model.User;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@RestController
public class TourGuideController {

    //TODO CUSTOM CONTROLLER LOGGER
    private final Logger LOGGER = LoggerFactory.getLogger(TourGuideController.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    IGpsUtilService gpsUtilService;

    @Autowired
    IUserService userService;

    @Autowired
    ITripPricerService tripPricerService;

    /**
     *
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     *
     * @return
     */
    @RequestMapping("/users")
    public String getAllUsers() {
        LOGGER.info("ENTREE CONTROLLER: /USERS");
        return userService.getAllUsers().toString();
    }

    /**
     *
     * @param userName
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) throws UserNotFoundException, JsonProcessingException {
        VisitedLocationBean visitedLocation = userService.getUserLocation(getUser(userName));
        return objectMapper.writeValueAsString(visitedLocation.LocationBean);
    }

    //  TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) throws UserNotFoundException {



        VisitedLocationBean visitedLocation = userService.getUserLocation(getUser(userName));
        return JsonStream.serialize(gpsUtilService.getNearByAttractions(visitedLocation));
    }

    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) throws UserNotFoundException {
        return JsonStream.serialize( userService.getUserRewards(getUser(userName)));
    }

    /**
     * Note: TODO reminder
     * Get a list of every user's most recent location as JSON
     * does not use gpsUtil to query for their current location,
     * but rather gathers the user's current location from their stored location history.
     * Return object should be the just a JSON mapping of userId to Locations similar to:
     * {
     * "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
     * ...
     * }
     *
     * @return
     */
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        return JsonStream.serialize(gpsUtilService.getAllCurrentLocations(userService.getAllUsers()));
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) throws UserNotFoundException {
        List<ProviderBean> providers = tripPricerService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    private User getUser(String userName) throws UserNotFoundException {
        return userService.getInternalUser(userName);
    }
}