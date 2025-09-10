package co.com.crediya.usecase.user.exceptions;

public class RolNotFoundException extends RuntimeException{
    public RolNotFoundException(String message) {
        super(message);
    }
}
