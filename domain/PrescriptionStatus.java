package pharmacy.domain;


public enum PrescriptionStatus {
    PENDING("Prescription Pending"),
    PREPARING("Preparing Medication"),
    READY("Ready for Collection"),
    DISPENSED("Dispensed");

    private final String displayLabel;

    PrescriptionStatus(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }
}
