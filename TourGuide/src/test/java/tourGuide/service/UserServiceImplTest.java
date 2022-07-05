package tourGuide.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.beans.AttractionBean;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.exceptions.UserAlreadyExistException;
import tourGuide.exceptions.UserNotFoundException;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

//TODO ? @DisplayName on test methods ?
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserService userServiceToSpy;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenExistingUser_whenIsUserExist_thenReturnTrue() {
        assertTrue(userService.isUserExist("internalUserTest1"));
    }

    @Test
    void givenNotExistingUser_whenIsUserExist_thenReturnFalse() {
        assertFalse(userService.isUserExist("ThisUserDoesNotExist"));
    }

    @Test
    void givenExistingUser_whenGetInternalUser_thenReturnUserObject() throws UserNotFoundException {
        User userResponse = userService.getInternalUser("internalUserTest1");
        assertEquals("internalUserTest1", userResponse.getUserName());
    }

    @Test
    void givenNotExistingUser_whenGetInternalUser_thenTrowsUserNotFoundException() {
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getInternalUser("ThisUserDoesNotExist"));
        assertTrue(exception.getMessage().contains("User Not found with: userName= "));
    }

    @Test
    void givenNotExingUser_whenAddUser_thenReturnCorrectUser() throws UserAlreadyExistException, UserNotFoundException {
        User userTest = new User(UUID.randomUUID(), "username", "111", "emailAddress");
        User userResult = userService.addUser(userTest);
        assertEquals("username", userResult.getUserName());
        assertEquals("111", userResult.getPhoneNumber());
        assertEquals("emailAddress", userResult.getEmailAddress());
    }

    @Test
    void givenExingUser_whenAddUser_thenTrowsUserAlreadyExistException() {
        User userTest = new User(UUID.randomUUID(), "internalUserTest0", "000", "internalUserTest0@tourGuide.com");
        Exception exception = assertThrows(UserAlreadyExistException.class, () -> userService.addUser(userTest));
        assertTrue(exception.getMessage().contains("User: [internalUserTest0] already exist"));
    }

    @Test
    void givenUser_whenGetUserRewards_thenReturnCorrectList() throws UserNotFoundException, UserAlreadyExistException {
        UUID userId = UUID.randomUUID();
        User userTest = new User(userId, "userTestGetUserReward", "42", "userTestGetUserReward@tourGuide.com");
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        for (int increment = 0; increment < randomNum; increment++) {
            LocationBean locationBeanTest = new LocationBean(increment + 1, increment + 2);
            VisitedLocationBean visitedLocationBeanTest = new VisitedLocationBean(userId, locationBeanTest, new Date());
            AttractionBean attractionBeanTest = new AttractionBean("userTest" + increment, "cityTest" + increment, "stateTest" + increment, userId, increment + 1, increment + 2);
            UserReward rewardTest = new UserReward(visitedLocationBeanTest, attractionBeanTest, increment);
            userTest.addUserReward(rewardTest);
        }
        userService.addUser(userTest);
        List<UserReward> userRewardListTest = userService.getUserRewards(userTest);
        assertEquals(randomNum, userRewardListTest.size());
    }

    @Test
    void givenUserWithNotEmptyVisitedLocationList_whenGetUserLocation_thenReturnLastVisitedLocation() {
        UUID userId = UUID.randomUUID();
        User userTest = new User(userId, "userTestGetUserLocation", "42", "userTestGetUserLocation@tourGuide.com");
        LocationBean locationBeanTest = new LocationBean(1000, 2000);
        VisitedLocationBean visitedLocationBeanTest = new VisitedLocationBean(userId, locationBeanTest, new Date());
        userTest.addToVisitedLocations(visitedLocationBeanTest);
        VisitedLocationBean visitedLocationBeanResult = userService.getUserLocation(userTest);
        assertEquals(visitedLocationBeanTest.LocationBean, visitedLocationBeanResult.LocationBean);
        assertEquals(visitedLocationBeanTest.LocationBean.latitude, visitedLocationBeanResult.LocationBean.latitude);
        assertEquals(visitedLocationBeanTest.LocationBean.longitude, visitedLocationBeanResult.LocationBean.longitude);
    }

    @Test
    void givenUserWithEmptyVisitedLocationList_whenGetUserLocation_thenReturnCurrentLocation() {
        UUID userId = UUID.randomUUID();
        User userTest = new User(userId, "userTestGetUserLocation", "42", "userTestGetUserLocation@tourGuide.com");
        LocationBean locationBeanTest = new LocationBean(2000, 3000);
        VisitedLocationBean visitedLocationBeanTest = new VisitedLocationBean(userId, locationBeanTest, new Date());
        IUserService userServiceSpy = Mockito.spy(userServiceToSpy);
        Mockito.doReturn(visitedLocationBeanTest).when(userServiceSpy).trackUserLocation(Mockito.any());
        VisitedLocationBean visitedLocationBeanResult = userServiceSpy.getUserLocation(userTest);
        assertEquals(visitedLocationBeanTest.LocationBean, visitedLocationBeanResult.LocationBean);
        assertEquals(visitedLocationBeanTest.LocationBean.latitude, visitedLocationBeanResult.LocationBean.latitude);
        assertEquals(visitedLocationBeanTest.LocationBean.longitude, visitedLocationBeanResult.LocationBean.longitude);
    }

    @Test
    void getAllUsers() {
        List<User> userListTest = userService.getAllUsers();
        assertTrue(userListTest.size() > 0);
    }

    //TODO REFACTOR THIS METHOD ?
    @Test
    void trackUserLocation() {
    }
}