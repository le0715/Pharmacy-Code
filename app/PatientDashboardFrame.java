package pharmacy.app;

import pharmacy.domain.*;

import java.util.List;
import java.util.Scanner;

// UC02
public class PatientDashboardFrame {

    private final PharmacyDataStore pharmacyDataStore;
    private final PatientUser loggedInPatient;
    private final Scanner scanner;

    public PatientDashboardFrame(PatientUser loggedInPatient) {
        this.loggedInPatient = loggedInPatient;
        this.pharmacyDataStore = PharmacyDataStore.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void showDashboard() {

        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("        PATIENT DASHBOARD");
            System.out.println("====================================");
            System.out.println("Welcome, " + loggedInPatient.getFullName());
            System.out.println("1. View Prescriptions");
            System.out.println("2. View Notifications");
            System.out.println("3. Refresh Prescription Status");
            System.out.println("4. Log Out");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    viewPrescriptions();
                    break;

                case 2:
                    showNotifications();
                    break;

                case 3:
                    viewPrescriptions();
                    break;

                case 4:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);
    }

    private void viewPrescriptions() {

        List<Prescription> patientPrescriptions =
                pharmacyDataStore.findPrescriptionsForPatient(
                        loggedInPatient.getUserId());

        System.out.println("\n====================================");
        System.out.println("       PRESCRIPTION STATUS");
        System.out.println("====================================");

        if (patientPrescriptions.isEmpty()) {
            System.out.println("No active prescription records found.");
            return;
        }

        for (Prescription prescription : patientPrescriptions) {

            System.out.println("------------------------------------");
            System.out.println("Prescription ID: "
                    + prescription.getPrescriptionId());

            System.out.println("Date Issued: "
                    + prescription.getDateIssued());

            System.out.println("Items: "
                    + prescription.getItemSummary());

            System.out.println("Status: "
                    + prescription.getCurrentStatus().getDisplayLabel());

            System.out.printf("Total: RM %.2f%n",
                    prescription.getTotalAmount());
        }

        System.out.println("------------------------------------");
    }

    private void showNotifications() {

        List<NotificationMessage> notifications =
                pharmacyDataStore.findNotificationsForUser(
                        loggedInPatient.getEmailAddress());

        System.out.println("\n====================================");
        System.out.println("          NOTIFICATIONS");
        System.out.println("====================================");

        if (notifications.isEmpty()) {

            System.out.println("You have no notifications.");

        } else {

            for (NotificationMessage notification : notifications) {

                System.out.println("- "
                        + notification.getMessageText());
            }
        }
    }
}
