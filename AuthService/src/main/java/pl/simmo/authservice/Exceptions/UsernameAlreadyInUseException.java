package pl.simmo.authservice.Exceptions;

public class UsernameAlreadyInUseException extends Throwable {
    public UsernameAlreadyInUseException(String s) {
        super(s);
    }
}
