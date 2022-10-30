package tourGuide.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IRewardCentralService;
import tourGuide.service.IUserService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TourGuideIT {

    @Autowired
    private IGpsUtilService gpsUtilService;

    @Autowired
    private IRewardCentralService rewardCentralService;

    @Autowired
    private IUserService userService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void userGetRewards() throws ExecutionException, InterruptedException {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        AttractionBean attractionBean = gpsUtilService.getAttractionsList().get(0);
        user.addToVisitedLocations(new VisitedLocationBean(user.getUserId(), attractionBean, new Date()));
        rewardCentralService.calculateRewards(user).get();
        List<UserReward> userRewards = user.getUserRewards();
        assertEquals(1, userRewards.size());
    }

    @Test
    public void nearAllAttractions() throws ExecutionException, InterruptedException {
        rewardCentralService.setProximityBufferInMiles(Integer.MAX_VALUE);
        User user = userService.getAllUsers().get(0);
        CompletableFuture<User> future = rewardCentralService.calculateRewards(user);
        User finalUser = future.get();
        rewardCentralService.setDefaultProximityBuffer();
        List<UserReward> userRewards = userService.getUserRewards(user);
        assertEquals(gpsUtilService.getAttractionsList().size(), userRewards.size());
    }
}