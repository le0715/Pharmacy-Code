package pharmacy.app;

import pharmacy.domain.*;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.regex.Pattern;

//UC01
public class RegisterFrame extends JFrame {

    private static final String WINDOW_TITLE = "Pharmacy Management System - Register";
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9-]{7,15}$");

    private final PharmacyDataStore pharmacyDataStore = PharmacyDataStore.getInstance();

    private final JTextField fullNameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField phoneNumberField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JTextField allergyHistoryField = new JTextField(20);

    public RegisterFrame() {
        super(WINDOW_TITLE);
        buildUserInterface();
    }

    private void buildUserInterface() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(440, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headingLabel = new JLabel("Create a New Account", JLabel.CENTER);
        headingLabel.setFont(headingLabel.getFont().deriveFont(16f));
        add(headingLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, constraints, 0, "Full Name:", fullNameField);
        addFormRow(formPanel, constraints, 1, "Email:", emailField);
        addFormRow(formPanel, constraints, 2, "Phone Number:", phoneNumberField);
        addFormRow(formPanel, constraints, 3, "Password:", passwordField);
        addFormRow(formPanel, constraints, 4, "Allergy History (optional):", allergyHistoryField);

        add(formPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Register");
        submitButton.addActionListener(event -> handleSubmitButtonClicked());

        JButton backToLoginButton = new JButton("Back to Log In");
        backToLoginButton.addActionListener(event -> returnToLoginFrame());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(backToLoginButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormRow(JPanel formPanel, GridBagConstraints constraints, int rowIndex,
                             String labelText, JTextField inputField) {
        constraints.gridx = 0;
        constraints.gridy = rowIndex;
        constraints.gridwidth = 1;
        formPanel.add(new JLabel(labelText), constraints);
        constraints.gridx = 1;
        formPanel.add(inputField, constraints);
    }

    private void handleSubmitButtonClicked() {
        String fullName = fullNameField.getText().trim();
        String emailAddress = emailField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String password = new String(passwordField.getPassword());
        String allergyHistory = allergyHistoryField.getText().trim();

        if (fullName.isEmpty() || emailAddress.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please fill in all required fields.");
            return;
        }
        if (!EMAIL_PATTERN.matcher(emailAddress).matches()) {
            showErrorMessage("Please enter a valid email address.");
            return;
        }
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            showErrorMessage("Please enter a valid phone number.");
            return;
        }

        try {
            pharmacyDataStore.registerPatient(fullName, emailAddress, phoneNumber, password, allergyHistory);
            JOptionPane.showMessageDialog(this,
                    "Registration successful! You may now log in.",
                    "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
            returnToLoginFrame();
        } catch (DuplicateAccountException exception) {
            showErrorMessage(exception.getMessage() + "\nPlease log in instead.");
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }

    private void returnToLoginFrame() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
