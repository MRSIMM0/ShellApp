package pl.simmo.authservice.Exceptions;

public class UserNotActivatedException extends Throwable {
    public UserNotActivatedException() {
        super("User is not activated");
    }
}
