package pharmacy.domain;


public class DuplicateAccountException extends Exception {

    public DuplicateAccountException(String message) {
        super(message);
    }
}
