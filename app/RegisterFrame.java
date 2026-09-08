package pharmacy.app;

import pharmacy.domain.*;

import java.util.Scanner;
import java.util.regex.Pattern;

// UC01 - Patient Registration
public class RegisterFrame {

    private PharmacyDataStore pharmacyDataStore;
    private Scanner scanner;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9-]{7,15}$");


    // Constructor
    public RegisterFrame() {

        pharmacyDataStore =
                PharmacyDataStore.getInstance();

        scanner = new Scanner(System.in);
    }


    // Start registration
    public void start() {

        System.out.println();
        System.out.println("======================================");
        System.out.println("     PHARMACY MANAGEMENT SYSTEM");
        System.out.println("          PATIENT REGISTRATION");
        System.out.println("======================================");


        // Get full name
        System.out.print("Enter Full Name: ");

        String fullName =
                scanner.nextLine().trim();


        // Get email
        System.out.print("Enter Email: ");

        String emailAddress =
                scanner.nextLine().trim();


        // Get phone number
        System.out.print("Enter Phone Number: ");

        String phoneNumber =
                scanner.nextLine().trim();


        // Get password
        System.out.print("Enter Password: ");

        String password =
                scanner.nextLine();


        // Get allergy history
        System.out.print(
                "Enter Allergy History (optional): "
        );

        String allergyHistory =
                scanner.nextLine().trim();


        // Validate required fields
        if (fullName.isEmpty()
                || emailAddress.isEmpty()
                || phoneNumber.isEmpty()
                || password.isEmpty()) {

            System.out.println();
            System.out.println(
                    "Error: Please fill in all required fields."
            );

            return;
        }


        // Validate email
        if (!EMAIL_PATTERN
                .matcher(emailAddress)
                .matches()) {

            System.out.println();
            System.out.println(
                    "Error: Please enter a valid email address."
            );

            return;
        }


        // Validate phone number
        if (!PHONE_PATTERN
                .matcher(phoneNumber)
                .matches()) {

            System.out.println();
            System.out.println(
                    "Error: Please enter a valid phone number."
            );

            return;
        }


        // Register patient
        try {

            pharmacyDataStore.registerPatient(
                    fullName,
                    emailAddress,
                    phoneNumber,
                    password,
                    allergyHistory
            );

            System.out.println();
            System.out.println(
                    "Registration successful!"
            );

            System.out.println(
                    "You may now log in."
            );

        } catch (DuplicateAccountException exception) {

            System.out.println();
            System.out.println(
                    "Registration Error: "
                    + exception.getMessage()
            );

            System.out.println(
                    "Please log in instead."
            );
        }
    }
}