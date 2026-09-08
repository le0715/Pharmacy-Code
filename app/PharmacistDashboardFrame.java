package pharmacy.app;

import pharmacy.domain.*;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

/**
 *  "Verify prescription" (UC03)
 *  "Dispense/sell medication" (UC05)
 *  "Manage and update inventory" (UC06)
 */
public class PharmacistDashboardFrame extends JFrame {

    private static final String[] PENDING_COLUMN_NAMES =
            {"Prescription ID", "Patient", "Items", "Allergy History"};
    private static final String[] PREPARING_COLUMN_NAMES =
            {"Prescription ID", "Patient", "Items", "Total (RM)"};
    private static final String[] INVENTORY_COLUMN_NAMES =
            {"Medication ID", "Name", "Unit Price (RM)", "Stock Quantity", "Low Stock?"};

    private final PharmacyDataStore pharmacyDataStore = PharmacyDataStore.getInstance();
    private final PharmacistUser loggedInPharmacist;

    private final DefaultTableModel pendingTableModel;
    private final DefaultTableModel preparingTableModel;
    private final DefaultTableModel inventoryTableModel;

    private JTable pendingPrescriptionTable;
    private JTable preparingPrescriptionTable;
    private JTable inventoryTable;

    public PharmacistDashboardFrame(PharmacistUser loggedInPharmacist) {
        super("Pharmacist Dashboard - " + loggedInPharmacist.getFullName());
        this.loggedInPharmacist = loggedInPharmacist;
        this.pendingTableModel = createReadOnlyTableModel(PENDING_COLUMN_NAMES);
        this.preparingTableModel = createReadOnlyTableModel(PREPARING_COLUMN_NAMES);
        this.inventoryTableModel = createReadOnlyTableModel(INVENTORY_COLUMN_NAMES);
        buildUserInterface();
        refreshAllTables();
    }

    private DefaultTableModel createReadOnlyTableModel(String[] columnNames) {
        return new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
    }

    private void buildUserInterface() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headingLabel = new JLabel("Welcome, " + loggedInPharmacist.getFullName(), JLabel.CENTER);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(headingLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Verify Prescriptions", buildVerifyPrescriptionsPanel());
        tabbedPane.addTab("Dispense Medication", buildDispenseMedicationPanel());
        tabbedPane.addTab("Manage Inventory", buildManageInventoryPanel());
        add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(event -> handleLogoutButtonClicked());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel buildVerifyPrescriptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        pendingPrescriptionTable = new JTable(pendingTableModel);
        pendingPrescriptionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(pendingPrescriptionTable), BorderLayout.CENTER);

        JButton approveAndVerifyButton = new JButton("Approve & Verify Selected");
        approveAndVerifyButton.addActionListener(event -> handleApproveAndVerifyButtonClicked());
        JPanel actionPanel = new JPanel();
        actionPanel.add(approveAndVerifyButton);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildDispenseMedicationPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        preparingPrescriptionTable = new JTable(preparingTableModel);
        preparingPrescriptionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(preparingPrescriptionTable), BorderLayout.CENTER);

        JButton dispenseButton = new JButton("Process Payment & Dispense Selected");
        dispenseButton.addActionListener(event -> handleDispenseButtonClicked());
        JPanel actionPanel = new JPanel();
        actionPanel.add(dispenseButton);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildManageInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        JButton restockButton = new JButton("Restock +10");
        restockButton.addActionListener(event -> handleAdjustStockButtonClicked(10));

        JButton deductStockButton = new JButton("Deduct -10");
        deductStockButton.addActionListener(event -> handleAdjustStockButtonClicked(-10));

        JPanel actionPanel = new JPanel();
        actionPanel.add(restockButton);
        actionPanel.add(deductStockButton);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAllTables() {
        refreshPendingPrescriptionTable();
        refreshPreparingPrescriptionTable();
        refreshInventoryTable();
    }

    private void refreshPendingPrescriptionTable() {
        pendingTableModel.setRowCount(0);
        List<Prescription> pendingPrescriptions =
                pharmacyDataStore.findPrescriptionsByStatus(PrescriptionStatus.PENDING);
        for (Prescription prescription : pendingPrescriptions) {
            pendingTableModel.addRow(new Object[]{
                    prescription.getPrescriptionId(),
                    prescription.getPatient().getFullName(),
                    prescription.getItemSummary(),
                    prescription.getPatient().getAllergyHistory()
            });
        }
    }

    private void refreshPreparingPrescriptionTable() {
        preparingTableModel.setRowCount(0);
        List<Prescription> preparingPrescriptions =
                pharmacyDataStore.findPrescriptionsByStatus(PrescriptionStatus.PREPARING);
        for (Prescription prescription : preparingPrescriptions) {
            preparingTableModel.addRow(new Object[]{
                    prescription.getPrescriptionId(),
                    prescription.getPatient().getFullName(),
                    prescription.getItemSummary(),
                    String.format("%.2f", prescription.getTotalAmount())
            });
        }
    }

    private void refreshInventoryTable() {
        inventoryTableModel.setRowCount(0);
        List<Medication> medicationList = pharmacyDataStore.getAllMedications();
        for (Medication medication : medicationList) {
            inventoryTableModel.addRow(new Object[]{
                    medication.getMedicationId(),
                    medication.getMedicationName(),
                    String.format("%.2f", medication.getUnitPrice()),
                    medication.getStockQuantity(),
                    medication.isLowStock() ? "YES" : "No"
            });
        }
    }

    private void handleApproveAndVerifyButtonClicked() {
        int selectedRowIndex = pendingPrescriptionTable.getSelectedRow();
        if (selectedRowIndex == -1) {
            showSelectionRequiredMessage();
            return;
        }
        String prescriptionId = (String) pendingTableModel.getValueAt(selectedRowIndex, 0);
        Prescription selectedPrescription = findPrescriptionById(prescriptionId,
                pharmacyDataStore.findPrescriptionsByStatus(PrescriptionStatus.PENDING));
        if (selectedPrescription != null) {
            pharmacyDataStore.verifyAndApprovePrescription(selectedPrescription);
            JOptionPane.showMessageDialog(this,
                    "Prescription " + prescriptionId + " verified and is now Preparing Medication.");
            refreshAllTables();
        }
    }

    private void handleDispenseButtonClicked() {
        int selectedRowIndex = preparingPrescriptionTable.getSelectedRow();
        if (selectedRowIndex == -1) {
            showSelectionRequiredMessage();
            return;
        }
        String prescriptionId = (String) preparingTableModel.getValueAt(selectedRowIndex, 0);
        Prescription selectedPrescription = findPrescriptionById(prescriptionId,
                pharmacyDataStore.findPrescriptionsByStatus(PrescriptionStatus.PREPARING));
        if (selectedPrescription != null) {
            pharmacyDataStore.dispenseMedication(selectedPrescription);
            JOptionPane.showMessageDialog(this,
                    "Payment processed. Prescription " + prescriptionId + " has been dispensed. "
                            + "Thanks for your purchase!");
            refreshAllTables();
        }
    }

    private void handleAdjustStockButtonClicked(int quantityDelta) {
        int selectedRowIndex = inventoryTable.getSelectedRow();
        if (selectedRowIndex == -1) {
            showSelectionRequiredMessage();
            return;
        }
        String medicationId = (String) inventoryTableModel.getValueAt(selectedRowIndex, 0);
        Medication selectedMedication = null;
        for (Medication medication : pharmacyDataStore.getAllMedications()) {
            if (medication.getMedicationId().equals(medicationId)) {
                selectedMedication = medication;
                break;
            }
        }
        if (selectedMedication == null) {
            return;
        }
        try {
            pharmacyDataStore.adjustMedicationStock(selectedMedication, quantityDelta);
            refreshInventoryTable();
        } catch (InvalidStockAdjustmentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Invalid Stock Adjustment", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Prescription findPrescriptionById(String prescriptionId, List<Prescription> prescriptionList) {
        for (Prescription prescription : prescriptionList) {
            if (prescription.getPrescriptionId().equals(prescriptionId)) {
                return prescription;
            }
        }
        return null;
    }

    private void showSelectionRequiredMessage() {
        JOptionPane.showMessageDialog(this, "Please select a row first.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
    }

    private void handleLogoutButtonClicked() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
