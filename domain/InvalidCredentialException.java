package pharmacy.domain;


public class InvalidCredentialException extends Exception {

    public InvalidCredentialException(String message) {
        super(message);
    }
}
