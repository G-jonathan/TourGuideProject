package tourGuide.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tourGuide.beans.LocationBean;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class DistanceCalculationsTest {

    @Autowired
    DistanceCalculations distanceCalculations;

    @Test
    void getDistanceCalculations_shouldReturnCorrectResult() {
        LocationBean locationBean1 = new LocationBean(10, 20);
        LocationBean locationBean2 = new LocationBean(20, 10);
        double result = distanceCalculations.getDistance(locationBean1, locationBean2);
        assertEquals(959.2210598676558, result);
    }
}