package tourGuide.utils;

import tourGuide.dto.UserDto;
import tourGuide.model.User;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jonathan GOUVEIA
 * @version 1.0
 */
public class Convertion {

    /**
     * Convert user list to userDto list.
     *
     * @param userList a list of user object
     * @return a List of UserDto object
     */
    public static List<UserDto> convertUserListToUserDtoList(List<User> userList) {
        return userList.stream().map(user -> new UserDto(
                        user.getUserId(),
                        user.getUserName(),
                        user.getPhoneNumber(),
                        user.getEmailAddress(),
                        user.getUserPreferences().getNumberOfAdults(),
                        user.getUserPreferences().getNumberOfChildren())).collect(Collectors.toList());
    }
}