package tourGuide.controller;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsoniter.output.JsonStream;
import tourGuide.beans.ProviderBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.dto.NearbyAttractionDto;
import tourGuide.dto.NearbyAttractionListDto;
import tourGuide.exceptions.UserNotFoundException;
import tourGuide.model.UserPreferences;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IRewardCentralService;
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

    @Autowired
    IRewardCentralService rewardCentralService;

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

    /**
     * @param userName The name of the requested user
     * @return
     * @throws UserNotFoundException This exception is thrown when a user is not found in our database
     */
    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) throws UserNotFoundException, JsonProcessingException {
        VisitedLocationBean visitedLocation = userService.getUserLocation(getUser(userName));
        return objectMapper.writeValueAsString(visitedLocation.locationBean);
    }

    /**
     * Get the closest five tourist attractions to the user
     *
     * @param userName The name of the requested user
     * @return JSON object that contains:
     * The user's location;
     * The attractions names, ids and Locations ;
     * The distance in miles between the user's location and each attraction;
     * The reward points for visiting each Attraction;
     * @throws UserNotFoundException This exception is thrown when a user is not found in our database
     */
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) throws UserNotFoundException {
        User user = userService.getInternalUser(userName);
        VisitedLocationBean visitedLocation = userService.getUserLocation(user);
        List<NearbyAttractionDto> nearbyAttractionDtoListWithoutRewardPoints = gpsUtilService.getNearByAttractions(visitedLocation);
        List<NearbyAttractionDto> nearbyAttractionDtoListWithRewardPoints = rewardCentralService.setNearbyAttractionRewardPoints(nearbyAttractionDtoListWithoutRewardPoints, user.getUserId());
        return JsonStream.serialize(new NearbyAttractionListDto(visitedLocation.locationBean, nearbyAttractionDtoListWithRewardPoints));
    }

    /**
     * Get a user's rewards points
     *
     * @param userName Request param, user.userName
     * @return a JSON mapping of user rewards
     * @throws UserNotFoundException Custom exception when a user is not found in application database
     */
    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) throws UserNotFoundException {
        return JsonStream.serialize(userService.getUserRewards(getUser(userName)));
    }

    /**
     * Get User trip deals
     *
     * @param userName The name of the requested user
     * @return A providerBean list
     * @throws UserNotFoundException Custom exception when a user is not found in application database
     */
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) throws UserNotFoundException {
        List<ProviderBean> providers = tripPricerService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    /**
     * Allows you to update a user's preferences
     *
     * @param userName The name of the requested user
     * @param userPreferences the preference to modify
     * @return The updated preferences
     * @throws UserNotFoundException Custom exception when a user is not found in application database
     */
    @PutMapping("/updateUserPreferences")
    public String updateUserPreferences(@RequestParam String userName, @RequestBody UserPreferences userPreferences) throws UserNotFoundException, JsonProcessingException {
        User user = userService.getInternalUser(userName);
        userService.updateUserPreferences(userName, userPreferences);
        return (objectMapper.writeValueAsString(user.getUserPreferences()));
    }
}