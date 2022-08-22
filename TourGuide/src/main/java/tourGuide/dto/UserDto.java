package tourGuide.dto;

import java.util.UUID;

/**
 * This class purpose a variation of user model destined to be exposed by the controller
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
public class UserDto {
    private final UUID userId;
    private final String userName;
    private final String phoneNumber;
    private final String emailAddress;
    private int numberOfAdults = 1;
    private int numberOfChildren = 0;

    public UserDto(UUID userId, String userName, String phoneNumber, String emailAddress, int numberOfAdults, int numberOfChildren) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }
}