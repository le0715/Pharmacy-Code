package pharmacy.domain;


public class Medication {

    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 10;

    private final String medicationId;
    private String medicationName;
    private double unitPrice;
    private int stockQuantity;
    private int lowStockThreshold;

    public Medication(String medicationId, String medicationName, double unitPrice, int stockQuantity) {
        this(medicationId, medicationName, unitPrice, stockQuantity, DEFAULT_LOW_STOCK_THRESHOLD);
    }

    public Medication(String medicationId,
                       String medicationName,
                       double unitPrice,
                       int stockQuantity,
                       int lowStockThreshold) {
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void adjustStockQuantity(int quantityDelta) {
        this.stockQuantity += quantityDelta;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public boolean isLowStock() {
        return stockQuantity <= lowStockThreshold;
    }

    @Override
    public String toString() {
        return medicationName + " (RM " + String.format("%.2f", unitPrice) + ")";
    }
}
