package pharmacy.app;

import pharmacy.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// UC09 - Doctor issues a prescription
public class DoctorDashboardFrame {

    private PharmacyDataStore pharmacyDataStore;
    private DoctorUser loggedInDoctor;

    private Scanner scanner;

    private List<PrescriptionItem> currentPrescriptionItems;


    // Constructor
    public DoctorDashboardFrame(DoctorUser loggedInDoctor) {

        this.loggedInDoctor = loggedInDoctor;

        pharmacyDataStore = PharmacyDataStore.getInstance();

        scanner = new Scanner(System.in);

        currentPrescriptionItems = new ArrayList<>();
    }


    // Start the doctor dashboard
    public void showDashboard() {

        System.out.println("=================================");
        System.out.println("       DOCTOR DASHBOARD");
        System.out.println("=================================");

        System.out.println("Doctor: "
                + loggedInDoctor.getFullName());

        System.out.println();


        // Select patient
        PatientUser selectedPatient = selectPatient();

        if (selectedPatient == null) {
            System.out.println("No patient selected.");
            return;
        }


        // Add medications
        addPrescriptionItems();


        // Check whether medication was added
        if (currentPrescriptionItems.isEmpty()) {

            System.out.println(
                    "No medication was added."
            );

            return;
        }


        // Submit prescription
        submitPrescription(selectedPatient);
    }


    // Select a patient
    private PatientUser selectPatient() {

        List<PatientUser> patients =
                pharmacyDataStore.getAllPatients();


        if (patients.isEmpty()) {

            System.out.println(
                    "No patients available."
            );

            return null;
        }


        System.out.println("Select Patient:");

        for (int i = 0; i < patients.size(); i++) {

            System.out.println(
                    (i + 1)
                    + ". "
                    + patients.get(i).getFullName()
            );
        }


        System.out.print("Enter patient number: ");

        int choice = scanner.nextInt();

        scanner.nextLine();


        if (choice < 1 || choice > patients.size()) {

            System.out.println(
                    "Invalid patient selection."
            );

            return null;
        }


        return patients.get(choice - 1);
    }


    // Add medications to prescription
    private void addPrescriptionItems() {

        boolean addMore = true;


        while (addMore) {

            Medication selectedMedication =
                    selectMedication();


            if (selectedMedication == null) {
                return;
            }


            // Quantity
            System.out.print("Enter quantity: ");

            int quantity = scanner.nextInt();

            scanner.nextLine();


            if (quantity <= 0) {

                System.out.println(
                        "Quantity must be greater than 0."
                );

                continue;
            }


            // Dosage instructions
            System.out.print(
                    "Enter dosage instructions: "
            );

            String dosageInstructions =
                    scanner.nextLine().trim();


            if (dosageInstructions.isEmpty()) {

                System.out.println(
                        "Dosage instructions cannot be empty."
                );

                continue;
            }


            // Create prescription item
            PrescriptionItem item =
                    new PrescriptionItem(
                            selectedMedication,
                            quantity,
                            dosageInstructions
                    );


            // Add item to ArrayList
            currentPrescriptionItems.add(item);


            System.out.println(
                    "Medication added successfully."
            );


            // Ask whether doctor wants another medication
            System.out.print(
                    "Add another medication? (Y/N): "
            );

            String answer =
                    scanner.nextLine();


            if (!answer.equalsIgnoreCase("Y")) {

                addMore = false;
            }


            System.out.println();
        }
    }


    // Select medication
    private Medication selectMedication() {

        List<Medication> medications =
                pharmacyDataStore.getAllMedications();


        if (medications.isEmpty()) {

            System.out.println(
                    "No medications available."
            );

            return null;
        }


        System.out.println();
        System.out.println("Select Medication:");


        for (int i = 0; i < medications.size(); i++) {

            System.out.println(
                    (i + 1)
                    + ". "
                    + medications.get(i)
            );
        }


        System.out.print(
                "Enter medication number: "
        );

        int choice = scanner.nextInt();

        scanner.nextLine();


        if (choice < 1 || choice > medications.size()) {

            System.out.println(
                    "Invalid medication selection."
            );

            return null;
        }


        return medications.get(choice - 1);
    }


    // Submit prescription
    private void submitPrescription(
            PatientUser selectedPatient) {


        System.out.println();
        System.out.println(
                "================================="
        );

        System.out.println(
                "       PRESCRIPTION SUMMARY"
        );

        System.out.println(
                "================================="
        );


        System.out.println(
                "Patient: "
                + selectedPatient.getFullName()
        );


        System.out.println(
                "Doctor: "
                + loggedInDoctor.getFullName()
        );


        System.out.println();


        // Display prescription items
        System.out.println("Medications:");

        for (PrescriptionItem item :
                currentPrescriptionItems) {

            System.out.println(
                    "- " + item
            );
        }


        System.out.println();


        // Create prescription
        Prescription newPrescription =
                pharmacyDataStore.createPrescription(
                        selectedPatient,
                        loggedInDoctor,
                        currentPrescriptionItems
                );


        System.out.println(
                "Prescription submitted successfully."
        );


        System.out.println(
                "Prescription ID: "
                + newPrescription.getPrescriptionId()
        );


        System.out.println(
                "Status: "
                + newPrescription
                        .getCurrentStatus()
                        .getDisplayLabel()
        );


        // Clear the list after submission
        currentPrescriptionItems.clear();
    }
}