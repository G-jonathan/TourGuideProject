package tourGuide.exceptions;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}