package pharmacy.app;

import pharmacy.domain.AdministratorUser;
import pharmacy.domain.PharmacyDataStore;
import pharmacy.domain.SystemUser;

import java.util.List;
import java.util.Scanner;

public class AdministratorDashboardFrame {

    private final PharmacyDataStore pharmacyDataStore;
    private final AdministratorUser loggedInAdministrator;
    private final Scanner scanner;

    public AdministratorDashboardFrame(AdministratorUser loggedInAdministrator) {
        this.loggedInAdministrator = loggedInAdministrator;
        this.pharmacyDataStore = PharmacyDataStore.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void showDashboard() {

        int choice;

        do {
            System.out.println("\n=================================");
            System.out.println("      ADMINISTRATOR DASHBOARD");
            System.out.println("=================================");
            System.out.println("Welcome, " + loggedInAdministrator.getFullName());
            System.out.println("1. Manage User Access");
            System.out.println("2. Generate Inventory Report");
            System.out.println("3. Generate Sales Report");
            System.out.println("4. Log Out");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manageUserAccess();
                    break;

                case 2:
                    generateInventoryReport();
                    break;

                case 3:
                    generateSalesReport();
                    break;

                case 4:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);
    }

    private void manageUserAccess() {

        List<SystemUser> users =
                pharmacyDataStore.getAllRegisteredUsers();

        System.out.println("\n=================================");
        System.out.println("       MANAGE USER ACCESS");
        System.out.println("=================================");

        for (SystemUser user : users) {

            System.out.println("User ID: " + user.getUserId());
            System.out.println("Name: " + user.getFullName());
            System.out.println("Email: " + user.getEmailAddress());
            System.out.println("Role: " + user.getUserRole());
            System.out.println("Status: "
                    + (user.isActiveAccount() ? "Active" : "Inactive"));

            System.out.println("---------------------------------");
        }

        System.out.print("Enter User ID to change status: ");
        String userId = scanner.next();

        System.out.println("1. Activate");
        System.out.println("2. Deactivate");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();

        SystemUser targetUser = findUserById(userId);

        if (targetUser == null) {
            System.out.println("User not found.");
            return;
        }

        if (choice == 1) {

            pharmacyDataStore.setUserAccountActive(targetUser, true);
            System.out.println("User account activated.");

        } else if (choice == 2) {

            pharmacyDataStore.setUserAccountActive(targetUser, false);
            System.out.println("User account deactivated.");

        } else {

            System.out.println("Invalid choice.");
        }
    }

    private SystemUser findUserById(String userId) {

        List<SystemUser> users =
                pharmacyDataStore.getAllRegisteredUsers();

        for (SystemUser user : users) {

            if (user.getUserId().equals(userId)) {
                return user;
            }
        }

        return null;
    }

    private void generateInventoryReport() {

        System.out.println("\n=================================");
        System.out.println("       INVENTORY REPORT");
        System.out.println("=================================");

        System.out.println(
                pharmacyDataStore.generateInventoryReport()
        );
    }

    private void generateSalesReport() {

        System.out.println("\n=================================");
        System.out.println("          SALES REPORT");
        System.out.println("=================================");

        System.out.println(
                pharmacyDataStore.generateSalesReport()
        );
    }
}
