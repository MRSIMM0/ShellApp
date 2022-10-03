package pl.simmo.authservice.Exceptions;

public class CredentialsDoesNotMatchException extends Throwable {
    public CredentialsDoesNotMatchException() {
        super("Credentials does not mach");
    }
}
