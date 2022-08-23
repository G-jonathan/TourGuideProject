package tourGuide.controller;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jsoniter.output.JsonStream;
import tourGuide.beans.ProviderBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.exceptions.UserNotFoundException;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.ITripPricerService;
import tourGuide.service.IUserService;
import tourGuide.model.User;
import tourGuide.utils.DtoConversion;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@RestController
public class TourGuideController {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    IGpsUtilService gpsUtilService;

    @Autowired
    IUserService userService;

    @Autowired
    ITripPricerService tripPricerService;

    /**
     * Entry point
     *
     * @return A welcome message
     */
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * Get a list of every user's most recent location
     *
     * @return a JSON mapping of userId to Locations like:
     * {"userId":"17190a91-c3e3-4633-aace-92de27f807d7","location":{"latitude":-155.263092,"longitude":-30.622786}}
     */
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        return JsonStream.serialize(gpsUtilService.getAllCurrentLocations(userService.getAllUsers()));
    }

    /**
     * Get a list of every user
     *
     * @return a toString UserDto list
     */
    @RequestMapping("/users")
    public String getAllUsers() throws JsonProcessingException {
        return objectMapper.writeValueAsString(DtoConversion.convertUserListToUserDtoList(userService.getAllUsers()));
    }

    /**
     * Get a single user
     *
     * @param userName The name of the requested user
     * @return The userDto Object requested
     * @throws UserNotFoundException This exception is thrown when a user is not found in our database
     */
    @RequestMapping("/user")
    private User getUser(@RequestParam String userName) throws UserNotFoundException {
        return userService.getInternalUser(userName);
    }

//TODO PRIVATE GETUSER + PUBLIC GETUSER ??????

    /**
     *
     * @param userName
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) throws UserNotFoundException, JsonProcessingException {
        VisitedLocationBean visitedLocation = userService.getUserLocation(getUser(userName));
        return objectMapper.writeValueAsString(visitedLocation.locationBean);
    }

    //  TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    // Note: Attraction reward points can be gathered from RewardsCentral

    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) throws UserNotFoundException {
        VisitedLocationBean visitedLocation = userService.getUserLocation(getUser(userName));
        return JsonStream.serialize(gpsUtilService.getNearByAttractions(visitedLocation));
    }

    /**
     *
     * @param userName Request param, user.userName
     * @return a JSON mapping of user rewards
     * @throws UserNotFoundException Custom exception when a user is not found in application database
     */
    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) throws UserNotFoundException {
        return JsonStream.serialize( userService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) throws UserNotFoundException {
        List<ProviderBean> providers = tripPricerService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }
}