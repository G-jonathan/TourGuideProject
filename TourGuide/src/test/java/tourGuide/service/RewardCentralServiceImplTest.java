package tourGuide.service;

import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.dto.NearbyAttractionDto;
import tourGuide.model.User;
import tourGuide.proxies.MicroserviceRewardCentralProxy;
import tourGuide.utils.DistanceCalculations;
import java.util.*;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class RewardCentralServiceImplTest {

    @MockBean
    private MicroserviceRewardCentralProxy rewardCentralProxy;

    @MockBean
    private DistanceCalculations distanceCalculations;

    @MockBean
    private IGpsUtilService gpsUtilService;

    @Autowired
    private IRewardCentralService rewardCentralService;

    @Test
    void getRewardPoints_ShouldReturnInteger_ForGivenUserAndAttraction() {
        UUID userId = UUID.randomUUID();
        UUID attractionId = UUID.randomUUID();
        Random random = new Random();
        int intPoint = random.nextInt(1000);
        when(rewardCentralProxy.getAttractionRewardPoints(attractionId, userId)).thenReturn(intPoint);
        int result = rewardCentralService.getRewardPoints(attractionId, userId);
        assertEquals(intPoint, result);
    }

    @Test
    void setNearbyAttractionRewardPoints_shouldReturnANearbyAttractionDtoListWithRewardPoints_forGivenNearbyAttractionDtoListWithoutRewardPoints(){
        List<NearbyAttractionDto> nearbyAttractionDtoList = new ArrayList<>();
        Random random = new Random();
        int numberOfNearbyAttractionDto = random.nextInt(10);
        int iterations = 0;
        while (iterations < numberOfNearbyAttractionDto) {
            NearbyAttractionDto nearbyAttractionDto = new NearbyAttractionDto();
            nearbyAttractionDto.setAttractionId(UUID.randomUUID());
            nearbyAttractionDto.setAttractionName("name" + iterations);
            nearbyAttractionDto.setAttractionLocation(new LocationBean(10 + iterations, 20 + iterations));
            nearbyAttractionDto.setDistance(0.20);
            nearbyAttractionDtoList.add(nearbyAttractionDto);
            iterations+=1;
        }
        Random randomPoints = new Random();
        when(rewardCentralProxy.getAttractionRewardPoints(any(UUID.class), any(UUID.class))).thenReturn(randomPoints.nextInt(100) + 1);
        List<NearbyAttractionDto> result = rewardCentralService.setNearbyAttractionRewardPoints(nearbyAttractionDtoList, UUID.randomUUID());
        assertEquals(numberOfNearbyAttractionDto, result.size());
        for (NearbyAttractionDto nearbyAttractionDto : result) {
            assertTrue(nearbyAttractionDto.getRewardPoints() > 0 && nearbyAttractionDto.getRewardPoints() < 101);
        }
    }


    @Test
    void calculateRewards_shouldSetTheCorrectRewardsNumber() throws ExecutionException, InterruptedException {
        User userTest = new User(UUID.randomUUID(), "userTest", "000", "User@email.com");
        LocationBean locationBean = new LocationBean(50, 50);
        VisitedLocationBean visitedLocationBean = new VisitedLocationBean(UUID.randomUUID(), locationBean, new Date());
        userTest.addToVisitedLocations(visitedLocationBean);
        List<AttractionBean> attractionBeanList = new ArrayList<>();
        Random random = new Random();
        int numberOfAttractions = random.nextInt(10);
        int iteration = 0;
        while (iteration < numberOfAttractions) {
            attractionBeanList.add(new AttractionBean("attractionTest" + iteration, "cityTest" + iteration, "stateTest" + iteration, UUID.randomUUID(), iteration + 1, iteration + 2));
            iteration += 1;
        }
        when(gpsUtilService.getAttractionsList()).thenReturn(attractionBeanList);
        when(distanceCalculations.getDistance(any(AttractionBean.class), any(LocationBean.class))).thenReturn(0.0);
        Random randomInt = new Random();
        when(rewardCentralProxy.getAttractionRewardPoints(any(UUID.class), any(UUID.class))).thenReturn(randomInt.nextInt(100) + 1);
        rewardCentralService.calculateRewards(userTest).get();
        assertEquals(numberOfAttractions, userTest.getUserRewards().size());
    }
}