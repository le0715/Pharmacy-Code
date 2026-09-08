package pharmacy.app;

import pharmacy.domain.*;


import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

//UC09
public class DoctorDashboardFrame extends JFrame {

    private final PharmacyDataStore pharmacyDataStore = PharmacyDataStore.getInstance();
    private final DoctorUser loggedInDoctor;

    private final JComboBox<PatientUser> patientComboBox = new JComboBox<>();
    private final JComboBox<Medication> medicationComboBox = new JComboBox<>();
    private final JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final JTextField dosageInstructionsField = new JTextField(18);

    private final DefaultListModel<PrescriptionItem> pendingItemListModel = new DefaultListModel<>();
    private final List<PrescriptionItem> currentPrescriptionItems = new ArrayList<>();

    public DoctorDashboardFrame(DoctorUser loggedInDoctor) {
        super("Doctor Dashboard - " + loggedInDoctor.getFullName());
        this.loggedInDoctor = loggedInDoctor;
        buildUserInterface();
    }

    private void buildUserInterface() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(560, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headingLabel = new JLabel("Issue New Prescription", JLabel.CENTER);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(headingLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        populatePatientComboBox();
        populateMedicationComboBox();

        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(new JLabel("Patient:"), constraints);
        constraints.gridx = 1;
        formPanel.add(patientComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(new JLabel("Medication:"), constraints);
        constraints.gridx = 1;
        formPanel.add(medicationComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(new JLabel("Quantity:"), constraints);
        constraints.gridx = 1;
        formPanel.add(quantitySpinner, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        formPanel.add(new JLabel("Dosage Instructions:"), constraints);
        constraints.gridx = 1;
        formPanel.add(dosageInstructionsField, constraints);

        JButton addItemButton = new JButton("Add Item to Prescription");
        addItemButton.addActionListener(event -> handleAddItemButtonClicked());
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        formPanel.add(addItemButton, constraints);

        add(formPanel, BorderLayout.WEST);

        JList<PrescriptionItem> pendingItemList = new JList<>(pendingItemListModel);
        JScrollPane itemScrollPane = new JScrollPane(pendingItemList);
        itemScrollPane.setBorder(BorderFactory.createTitledBorder("Items in this Prescription"));
        add(itemScrollPane, BorderLayout.CENTER);

        JButton submitPrescriptionButton = new JButton("Submit Prescription");
        submitPrescriptionButton.addActionListener(event -> handleSubmitPrescriptionButtonClicked());

        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(event -> handleLogoutButtonClicked());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitPrescriptionButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populatePatientComboBox() {
        for (PatientUser patient : pharmacyDataStore.getAllPatients()) {
            patientComboBox.addItem(patient);
        }
    }

    private void populateMedicationComboBox() {
        for (Medication medication : pharmacyDataStore.getAllMedications()) {
            medicationComboBox.addItem(medication);
        }
    }

    private void handleAddItemButtonClicked() {
        Medication selectedMedication = (Medication) medicationComboBox.getSelectedItem();
        String dosageInstructions = dosageInstructionsField.getText().trim();

        if (selectedMedication == null || dosageInstructions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a medication and enter dosage instructions.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedQuantity = (Integer) quantitySpinner.getValue();
        PrescriptionItem newItem = new PrescriptionItem(selectedMedication, selectedQuantity, dosageInstructions);
        currentPrescriptionItems.add(newItem);
        pendingItemListModel.addElement(newItem);
        dosageInstructionsField.setText("");
    }

    private void handleSubmitPrescriptionButtonClicked() {
        PatientUser selectedPatient = (PatientUser) patientComboBox.getSelectedItem();

        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentPrescriptionItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one medication item.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Prescription newPrescription = pharmacyDataStore.createPrescription(
                selectedPatient, loggedInDoctor, currentPrescriptionItems);

        JOptionPane.showMessageDialog(this,
                "Prescription " + newPrescription.getPrescriptionId()
                        + " submitted with status: " + newPrescription.getCurrentStatus().getDisplayLabel(),
                "Prescription Submitted", JOptionPane.INFORMATION_MESSAGE);

        currentPrescriptionItems.clear();
        pendingItemListModel.clear();
    }

    private void handleLogoutButtonClicked() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
