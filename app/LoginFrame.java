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

public class LoginFrame extends JFrame {

    private static final String WINDOW_TITLE = "Pharmacy Management System - Log In";

    private final PharmacyDataStore pharmacyDataStore = PharmacyDataStore.getInstance();

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public LoginFrame() {
        super(WINDOW_TITLE);
        buildUserInterface();
    }

    private void buildUserInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headingLabel = new JLabel("Pharmacy Management System", JLabel.CENTER);
        headingLabel.setFont(headingLabel.getFont().deriveFont(18f));
        add(headingLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(new JLabel("Email:"), constraints);
        constraints.gridx = 1;
        formPanel.add(emailField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(new JLabel("Password:"), constraints);
        constraints.gridx = 1;
        formPanel.add(passwordField, constraints);

        JLabel demoHintLabel = new JLabel(
                "<html><i>Demo accounts: doctor@pharmacy.com / doctor123,<br>"
                        + "pharmacist@pharmacy.com / pharma123,<br>"
                        + "admin@pharmacy.com / admin123,<br>"
                        + "patient@pharmacy.com / patient123</i></html>");
        demoHintLabel.setFont(demoHintLabel.getFont().deriveFont(11f));
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        formPanel.add(demoHintLabel, constraints);

        add(formPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Log In");
        loginButton.addActionListener(event -> handleLoginButtonClicked());

        JButton registerButton = new JButton("Register New Account");
        registerButton.addActionListener(event -> openRegisterFrame());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLoginButtonClicked() {
        String emailAddress = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            SystemUser authenticatedUser = pharmacyDataStore.authenticateUser(emailAddress, password);
            routeToDashboard(authenticatedUser);
            dispose();
        } catch (InvalidCredentialException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Log In Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void routeToDashboard(SystemUser authenticatedUser) {
        if (authenticatedUser instanceof PatientUser) {
            new PatientDashboardFrame((PatientUser) authenticatedUser).setVisible(true);
        } else if (authenticatedUser instanceof DoctorUser) {
            new DoctorDashboardFrame((DoctorUser) authenticatedUser).setVisible(true);
        } else if (authenticatedUser instanceof PharmacistUser) {
            new PharmacistDashboardFrame((PharmacistUser) authenticatedUser).setVisible(true);
        } else if (authenticatedUser instanceof AdministratorUser) {
            new AdministratorDashboardFrame((AdministratorUser) authenticatedUser).setVisible(true);
        }
    }

    private void openRegisterFrame() {
        new RegisterFrame().setVisible(true);
        dispose();
    }
}
