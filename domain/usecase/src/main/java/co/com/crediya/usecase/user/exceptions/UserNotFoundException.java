package co.com.crediya.usecase.user.exceptions;

import java.util.List;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
