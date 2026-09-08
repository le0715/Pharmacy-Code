package pharmacy.domain;


public class PrescriptionItem {

    private final Medication medication;
    private final int quantity;
    private final String dosageInstructions;

    public PrescriptionItem(Medication medication, int quantity, String dosageInstructions) {
        this.medication = medication;
        this.quantity = quantity;
        this.dosageInstructions = dosageInstructions;
    }

    public Medication getMedication() {
        return medication;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDosageInstructions() {
        return dosageInstructions;
    }

    public double getSubtotalAmount() {
        return medication.getUnitPrice() * quantity;
    }

    @Override
    public String toString() {
        return medication.getMedicationName() + " x" + quantity + " (" + dosageInstructions + ")";
    }
}
