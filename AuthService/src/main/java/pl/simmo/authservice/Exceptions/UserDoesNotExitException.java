package pl.simmo.authservice.Exceptions;

public class UserDoesNotExitException extends Throwable {
    public UserDoesNotExitException(String e) {
        super(e);
    }
}
