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
import tourGuide.beans.ProviderBean;
import tourGuide.model.User;
import tourGuide.proxies.MicroserviceTripPricerProxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TripPricerServiceImplTest {

    @Autowired
    ITripPricerService tripPricerService;

    @MockBean
    MicroserviceTripPricerProxy tripPricerProxy;

    @Test
    void getTripDeals_shouldReturnAProviderBeanList_whenGivenAUser() {
        List<ProviderBean> providerBeanList = new ArrayList<>();
        Random random = new Random();
        int numberOfProviderBean = random.nextInt(10);
        int iteration = 0;
        while (iteration < numberOfProviderBean) {
            providerBeanList.add(new ProviderBean("name" + iteration, 50.00, UUID.randomUUID()));
            iteration += 1;
        }
        User userTest = new User(UUID.randomUUID(), "userTest", "000", "User@email.com");
        when(tripPricerProxy.getPrice(any(String.class), any(UUID.class), any(Integer.class), any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(providerBeanList);
        tripPricerService.getTripDeals(userTest);
        assertEquals(numberOfProviderBean, userTest.getTripDeals().size());
    }
}