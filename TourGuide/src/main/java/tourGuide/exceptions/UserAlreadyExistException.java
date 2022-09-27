package tourGuide.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when trying to add an existing user
 * The client is trying to create a resource that already exists on the server
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}