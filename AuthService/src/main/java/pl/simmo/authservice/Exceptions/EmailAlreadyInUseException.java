package pl.simmo.authservice.Exceptions;

public class EmailAlreadyInUseException extends Throwable {
    public EmailAlreadyInUseException(String s) {
        super(s);
    }
}
