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
import tourGuide.model.UserLocation;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.utils.DistanceCalculations;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class GpsUtilServiceImplTest {

    @MockBean
    DistanceCalculations distanceCalculations;

    @MockBean
    MicroserviceGpsUtilProxy gpsUtilProxy;

    @Autowired
    IGpsUtilService gpsUtilService;

    @Test
    void getAllCurrentLocations_shouldReturnAUserLocationList_ForGivenAUserList() {
        List<User> userList = new ArrayList<>();
        Random random = new Random();
        int numberOfUsers = random.nextInt(5);
        int iteration = 0;
        while (iteration < numberOfUsers) {
            UUID randomUuid = UUID.randomUUID();
            Date actualDate = new Date();
            User userTest = new User(randomUuid, "NameTest" + iteration, "0000", "email@email.com" + iteration);
            userTest.addToVisitedLocations(new VisitedLocationBean(randomUuid, new LocationBean(50, 50), actualDate));
            userList.add(userTest);
            iteration += 1;
        }
        List<UserLocation> userLocationList = gpsUtilService.getAllCurrentLocations(userList);
        assertEquals(numberOfUsers, userLocationList.size());
    }

    @Test
    void getUserLocation_whenGivenUserId_shouldReturnCorrectVisitedLocationBean() {
        UUID randomUuid = UUID.randomUUID();
        when(gpsUtilProxy.getUserLocation(randomUuid)).thenReturn(new VisitedLocationBean(randomUuid, new LocationBean(45, 45), new Date()));
        VisitedLocationBean visitedLocationBeanResult = gpsUtilService.getUserLocation(randomUuid);
        assertEquals(45, visitedLocationBeanResult.locationBean.longitude);
        assertEquals(45, visitedLocationBeanResult.locationBean.latitude);
        assertEquals(randomUuid, visitedLocationBeanResult.userId);
    }

    @Test
    void isWithinAttractionProximity_shouldReturnTrue_whenDistanceCalculationsReturnNearbyDistance() {
        when(distanceCalculations.getDistance(any(AttractionBean.class), any(LocationBean.class))).thenReturn(0.0);
        AttractionBean attractionBeanTest = new AttractionBean("name", "city", "state", UUID.randomUUID(), 25, 25);
        LocationBean locationBeanTest = new LocationBean(25, 25);
        boolean result = gpsUtilService.isWithinAttractionProximity(attractionBeanTest, locationBeanTest);
        assertTrue(result);
    }

    @Test
    void isWithinAttractionProximity_shouldReturnFalse_whenDistanceCalculationsReturnAwayDistance() {
        when(distanceCalculations.getDistance(any(AttractionBean.class), any(LocationBean.class))).thenReturn(Double.MAX_VALUE);
        AttractionBean attractionBeanTest = new AttractionBean("name", "city", "state", UUID.randomUUID(), 25, 25);
        LocationBean locationBeanTest = new LocationBean(25, 25);
        boolean result = gpsUtilService.isWithinAttractionProximity(attractionBeanTest, locationBeanTest);
        assertFalse(result);
    }


    @Test
    void getAttractionsList_shouldReturnCorrectListSize() {
        List<AttractionBean> attractionBeanListTest = new ArrayList<>();
        Random random = new Random();
        int numberOfAttractions = random.nextInt(10);
        int iteration = 0;
        while (iteration < numberOfAttractions) {
            AttractionBean attractionBeanTest = new AttractionBean(
                    "name" + iteration,
                    "city" + iteration,
                    "state" + iteration,
                    UUID.randomUUID(),
                    25 + iteration,
                    25 + iteration
            );
            attractionBeanListTest.add(attractionBeanTest);
            iteration += 1;
        }
        when(gpsUtilProxy.getAttractionList()).thenReturn(attractionBeanListTest);
        List<AttractionBean> attractionBeanListResult = gpsUtilService.getAttractionsList();
        assertEquals(numberOfAttractions, attractionBeanListResult.size());
    }


    @Test
    void getAndAddUserLocation_ShouldReturnCorrectVisitedLocationBean_forGivenUser() throws ExecutionException, InterruptedException {
        UUID randomUuid = UUID.randomUUID();
        Date actualDate = new Date();
        User userTest = new User(randomUuid, "NameTest", "0000", "email@email.com");
        when(gpsUtilProxy.getUserLocation(randomUuid)).thenReturn(new VisitedLocationBean(randomUuid, new LocationBean(6, 6), actualDate));
        CompletableFuture<VisitedLocationBean> visitedLocationBeanResult = gpsUtilService.getAndAddUserLocation(userTest);
        assertEquals(6, visitedLocationBeanResult.get().locationBean.longitude);
        assertEquals(6, visitedLocationBeanResult.get().locationBean.latitude);
        assertEquals(randomUuid, visitedLocationBeanResult.get().userId);
        assertEquals(actualDate, visitedLocationBeanResult.get().timeVisited);
    }


    @Test
    void getNearByAttractions_shouldReturnCorrectNearbyAttractionDtoList_forGivenVisitedLocation() {
        UUID randomUuid = UUID.randomUUID();
        Date actualDate = new Date();
        VisitedLocationBean visitedLocationBeanTest = new VisitedLocationBean(randomUuid, new LocationBean(15, 15), actualDate);
        List<AttractionBean> attractionBeanListTest = new ArrayList<>();
        int iteration = 0;
        while (iteration < 10) {
            AttractionBean attractionBeanTest = new AttractionBean(
                    "name" + iteration,
                    "city" + iteration,
                    "state" + iteration,
                    UUID.randomUUID(),
                    1 + iteration,
                    2 + iteration
            );
            attractionBeanListTest.add(attractionBeanTest);
            iteration += 1;
        }
        Random random = new Random();
        when(gpsUtilProxy.getAttractionList()).thenReturn(attractionBeanListTest);
        when(distanceCalculations.getDistance(any(AttractionBean.class), any(LocationBean.class))).thenReturn(0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.10);
        List<NearbyAttractionDto> nearbyAttractionDtoListResult = gpsUtilService.getNearByAttractions(visitedLocationBeanTest);
        assertEquals(5, nearbyAttractionDtoListResult.size());
        for (NearbyAttractionDto nearbyAttractionDto : nearbyAttractionDtoListResult) {
            assertTrue(nearbyAttractionDto.getRewardPoints() <= 0.05);
        }
    }
}