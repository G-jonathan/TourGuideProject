package tourGuide.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tourGuide.beans.LocationBean;
import tourGuide.beans.VisitedLocationBean;
import tourGuide.model.User;
import tourGuide.service.impl.UserServiceImpl;
import tourGuide.tracker.Tracker;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

/**
 * For internal testing
 * Provide a user list stored in memory
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
public class InternalTestData {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public InternalTestData() {
    }
    /**
     * Getter
     *
     * @return a list of Users

    public Map<String, User> getInternalUserMap() {
        return internalUserMap;
    }
    */
}