package pharmacy.app;

import pharmacy.domain.*;

import java.util.Scanner;

public class LoginFrame {

    private final PharmacyDataStore pharmacyDataStore;
    private final Scanner scanner;

    public LoginFrame() {
        pharmacyDataStore = PharmacyDataStore.getInstance();
        scanner = new Scanner(System.in);
    }

    public void startLogin() {

        System.out.println("====================================");
        System.out.println("     PHARMACY MANAGEMENT SYSTEM");
        System.out.println("====================================");

        System.out.print("Email: ");
        String emailAddress = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {

            SystemUser authenticatedUser =
                    pharmacyDataStore.authenticateUser(
                            emailAddress,
                            password
                    );

            System.out.println("\nLogin successful!");
            System.out.println(
                    "Welcome, " + authenticatedUser.getFullName()
            );

            routeToDashboard(authenticatedUser);

        } catch (InvalidCredentialException exception) {

            System.out.println("\nLogin failed.");
            System.out.println(exception.getMessage());

            System.out.println("Returning to main menu...");

            return;
        }
    }

    private void routeToDashboard(SystemUser authenticatedUser) {

        if (authenticatedUser instanceof PatientUser) {

            PatientDashboardFrame dashboard =
                    new PatientDashboardFrame(
                            (PatientUser) authenticatedUser
                    );

            dashboard.showDashboard();

        } else if (authenticatedUser instanceof DoctorUser) {

            DoctorDashboardFrame dashboard =
                    new DoctorDashboardFrame(
                            (DoctorUser) authenticatedUser
                    );

            dashboard.showDashboard();

        } else if (authenticatedUser instanceof PharmacistUser) {

            PharmacistDashboardFrame dashboard =
                    new PharmacistDashboardFrame(
                            (PharmacistUser) authenticatedUser
                    );

            dashboard.showDashboard();

        } else if (authenticatedUser instanceof AdministratorUser) {

            AdministratorDashboardFrame dashboard =
                    new AdministratorDashboardFrame(
                            (AdministratorUser) authenticatedUser
                    );

            dashboard.showDashboard();
        }
    }
}