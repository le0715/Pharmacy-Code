package pharmacy.domain;


public class InvalidStockAdjustmentException extends Exception {

    public InvalidStockAdjustmentException(String message) {
        super(message);
    }
}
