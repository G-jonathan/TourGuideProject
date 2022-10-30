package tourGuide.tracker;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import tourGuide.model.User;
import tourGuide.service.IGpsUtilService;
import tourGuide.service.IRewardCentralService;
import tourGuide.service.IUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TrackerTest {
    @MockBean
    IGpsUtilService gpsUtilServiceImpl;

    @MockBean
    IRewardCentralService rewardCentralServiceImpl;

    @MockBean
    IUserService userServiceImpl;

    @Test
    void trackerRun() throws InterruptedException {
        List<User> users = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            users.add(new User(UUID.randomUUID(),"userTest" + i, "000" + i, "User@email.com" + i));
        });
        when(userServiceImpl.getAllUsers()).thenReturn(users);
        when(gpsUtilServiceImpl.getAndAddUserLocation(any(User.class))).thenReturn(CompletableFuture.completedFuture(null));
        when(rewardCentralServiceImpl.calculateRewards(any(User.class))).thenReturn(CompletableFuture.completedFuture(null));
        Thread thread = new Thread(() -> new Tracker(userServiceImpl, gpsUtilServiceImpl, rewardCentralServiceImpl));
        thread.start();
        TimeUnit.MILLISECONDS.sleep(500);
        Mockito.verify(gpsUtilServiceImpl, times(10)).getAndAddUserLocation(any(User.class));
        Mockito.verify(rewardCentralServiceImpl, times(10)).calculateRewards(any(User.class));
        Mockito.verify(userServiceImpl, times(1)).getAllUsers();
    }
}