package tourGuide.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when a user is not found in our database
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception{

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}