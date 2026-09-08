package pharmacy.app;

import pharmacy.domain.*;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

//UC02
public class PatientDashboardFrame extends JFrame {

    private static final String[] TABLE_COLUMN_NAMES =
            {"Prescription ID", "Date Issued", "Items", "Status", "Total (RM)"};

    private final PharmacyDataStore pharmacyDataStore = PharmacyDataStore.getInstance();
    private final PatientUser loggedInPatient;
    private final DefaultTableModel prescriptionTableModel;

    public PatientDashboardFrame(PatientUser loggedInPatient) {
        super("Patient Dashboard - " + loggedInPatient.getFullName());
        this.loggedInPatient = loggedInPatient;
        this.prescriptionTableModel = new DefaultTableModel(TABLE_COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        buildUserInterface();
        refreshPrescriptionTable();
    }

    private void buildUserInterface() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headingLabel = new JLabel("Welcome, " + loggedInPatient.getFullName(), JLabel.CENTER);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(headingLabel, BorderLayout.NORTH);

        JTable prescriptionTable = new JTable(prescriptionTableModel);
        add(new JScrollPane(prescriptionTable), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(event -> refreshPrescriptionTable());

        JButton notificationsButton = new JButton("View Notifications");
        notificationsButton.addActionListener(event -> showNotificationsDialog());

        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(event -> handleLogoutButtonClicked());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(notificationsButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshPrescriptionTable() {
        prescriptionTableModel.setRowCount(0);
        List<Prescription> patientPrescriptions =
                pharmacyDataStore.findPrescriptionsForPatient(loggedInPatient.getUserId());

        if (patientPrescriptions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No active prescription records found.",
                    "Track Prescription Status", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Prescription prescription : patientPrescriptions) {
            prescriptionTableModel.addRow(new Object[]{
                    prescription.getPrescriptionId(),
                    prescription.getDateIssued(),
                    prescription.getItemSummary(),
                    prescription.getCurrentStatus().getDisplayLabel(),
                    String.format("%.2f", prescription.getTotalAmount())
            });
        }
    }

    private void showNotificationsDialog() {
        List<NotificationMessage> notifications =
                pharmacyDataStore.findNotificationsForUser(loggedInPatient.getEmailAddress());
        StringBuilder messageBuilder = new StringBuilder();
        if (notifications.isEmpty()) {
            messageBuilder.append("You have no notifications.");
        } else {
            for (NotificationMessage notification : notifications) {
                messageBuilder.append("- ").append(notification.getMessageText()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(this, messageBuilder.toString(),
                "Notifications", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleLogoutButtonClicked() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
