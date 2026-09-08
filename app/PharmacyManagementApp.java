package pharmacy.app;

import java.util.Scanner;

public class PharmacyManagementApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("====================================");
            System.out.println("     PHARMACY MANAGEMENT SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("====================================");

            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {

                LoginFrame login = new LoginFrame();

                login.startLogin();

                // After startLogin() finishes,
                // the program comes back here.
                System.out.println("\nBack to main menu...");

            } else if (choice.equals("2")) {

                RegisterFrame register = new RegisterFrame();

                register.start();

            } else if (choice.equals("3")) {

                System.out.println("Goodbye!");

                running = false;

            } else {

                System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}