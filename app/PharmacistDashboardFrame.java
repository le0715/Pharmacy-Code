package pharmacy.app;

import pharmacy.domain.*;

import java.util.List;
import java.util.Scanner;

// UC03 - Verify prescription
// UC05 - Dispense / sell medication
// UC06 - Manage and update inventory
public class PharmacistDashboardFrame {

    private final PharmacyDataStore pharmacyDataStore;
    private final PharmacistUser loggedInPharmacist;
    private final Scanner scanner;

    public PharmacistDashboardFrame(PharmacistUser loggedInPharmacist) {
        this.loggedInPharmacist = loggedInPharmacist;
        this.pharmacyDataStore = PharmacyDataStore.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void showDashboard() {

        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("       PHARMACIST DASHBOARD");
            System.out.println("====================================");
            System.out.println("Welcome, " + loggedInPharmacist.getFullName());

            System.out.println("\n1. Verify Prescription");
            System.out.println("2. Dispense Medication");
            System.out.println("3. Manage Inventory");
            System.out.println("4. Log Out");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    verifyPrescription();
                    break;

                case 2:
                    dispenseMedication();
                    break;

                case 3:
                    manageInventory();
                    break;

                case 4:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);
    }

    // UC03
    private void verifyPrescription() {

        List<Prescription> pendingPrescriptions =
                pharmacyDataStore.findPrescriptionsByStatus(
                        PrescriptionStatus.PENDING);

        System.out.println("\n====================================");
        System.out.println("       PENDING PRESCRIPTIONS");
        System.out.println("====================================");

        if (pendingPrescriptions.isEmpty()) {
            System.out.println("No pending prescriptions.");
            return;
        }

        for (Prescription prescription : pendingPrescriptions) {

            System.out.println("------------------------------------");
            System.out.println("Prescription ID: "
                    + prescription.getPrescriptionId());

            System.out.println("Patient: "
                    + prescription.getPatient().getFullName());

            System.out.println("Items: "
                    + prescription.getItemSummary());

            System.out.println("Allergy History: "
                    + prescription.getPatient().getAllergyHistory());
        }

        System.out.print("\nEnter Prescription ID to verify: ");
        String prescriptionId = scanner.next();

        Prescription selectedPrescription =
                findPrescriptionById(
                        prescriptionId,
                        pendingPrescriptions);

        if (selectedPrescription == null) {

            System.out.println("Prescription not found.");
            return;
        }

        pharmacyDataStore.verifyAndApprovePrescription(
                selectedPrescription);

        System.out.println("Prescription "
                + prescriptionId
                + " verified successfully.");

        System.out.println("Status changed to Preparing Medication.");
    }

    // UC05
    private void dispenseMedication() {

        List<Prescription> preparingPrescriptions =
                pharmacyDataStore.findPrescriptionsByStatus(
                        PrescriptionStatus.PREPARING);

        System.out.println("\n====================================");
        System.out.println("       READY TO DISPENSE");
        System.out.println("====================================");

        if (preparingPrescriptions.isEmpty()) {
            System.out.println("No prescriptions are ready.");
            return;
        }

        for (Prescription prescription : preparingPrescriptions) {

            System.out.println("------------------------------------");
            System.out.println("Prescription ID: "
                    + prescription.getPrescriptionId());

            System.out.println("Patient: "
                    + prescription.getPatient().getFullName());

            System.out.println("Items: "
                    + prescription.getItemSummary());

            System.out.printf("Total: RM %.2f%n",
                    prescription.getTotalAmount());
        }

        System.out.print("\nEnter Prescription ID to dispense: ");
        String prescriptionId = scanner.next();

        Prescription selectedPrescription =
                findPrescriptionById(
                        prescriptionId,
                        preparingPrescriptions);

        if (selectedPrescription == null) {

            System.out.println("Prescription not found.");
            return;
        }

        pharmacyDataStore.dispenseMedication(
                selectedPrescription);

        System.out.println("\nPayment processed.");
        System.out.println("Prescription "
                + prescriptionId
                + " has been dispensed.");
        System.out.println("Thank you for your purchase!");
    }

    // UC06
    private void manageInventory() {

        List<Medication> medicationList =
                pharmacyDataStore.getAllMedications();

        System.out.println("\n====================================");
        System.out.println("          INVENTORY");
        System.out.println("====================================");

        for (Medication medication : medicationList) {

            System.out.println("------------------------------------");

            System.out.println("Medication ID: "
                    + medication.getMedicationId());

            System.out.println("Name: "
                    + medication.getMedicationName());

            System.out.printf("Unit Price: RM %.2f%n",
                    medication.getUnitPrice());

            System.out.println("Stock Quantity: "
                    + medication.getStockQuantity());

            System.out.println("Low Stock: "
                    + (medication.isLowStock() ? "YES" : "NO"));
        }

        System.out.println("------------------------------------");

        System.out.print("Enter Medication ID: ");
        String medicationId = scanner.next();

        Medication selectedMedication = null;

        for (Medication medication : medicationList) {

            if (medication.getMedicationId().equals(medicationId)) {
                selectedMedication = medication;
                break;
            }
        }

        if (selectedMedication == null) {

            System.out.println("Medication not found.");
            return;
        }

        System.out.println("\n1. Restock +10");
        System.out.println("2. Deduct -10");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();

        int quantityDelta;

        if (choice == 1) {

            quantityDelta = 10;

        } else if (choice == 2) {

            quantityDelta = -10;

        } else {

            System.out.println("Invalid choice.");
            return;
        }

        try {

            pharmacyDataStore.adjustMedicationStock(
                    selectedMedication,
                    quantityDelta);

            System.out.println("Stock updated successfully.");
            System.out.println("New stock quantity: "
                    + selectedMedication.getStockQuantity());

        } catch (InvalidStockAdjustmentException exception) {

            System.out.println("Invalid stock adjustment.");
            System.out.println(exception.getMessage());
        }
    }

    private Prescription findPrescriptionById(
            String prescriptionId,
            List<Prescription> prescriptionList) {

        for (Prescription prescription : prescriptionList) {

            if (prescription.getPrescriptionId()
                    .equals(prescriptionId)) {

                return prescription;
            }
        }

        return null;
    }
}
