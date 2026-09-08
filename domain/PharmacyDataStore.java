package pharmacy.domain;


import java.util.ArrayList;
import java.util.List;

public final class PharmacyDataStore {

    private static final PharmacyDataStore SHARED_INSTANCE = new PharmacyDataStore();

    private final List<SystemUser> registeredUserList = new ArrayList<>();
    private final List<Prescription> prescriptionList = new ArrayList<>();
    private final List<Medication> medicationInventory = new ArrayList<>();
    private final List<NotificationMessage> notificationList = new ArrayList<>();

    private int nextUserIdNumber = 1;
    private int nextPrescriptionIdNumber = 1;
    private int nextNotificationIdNumber = 1;

    private PharmacyDataStore() {
        seedDemoAccounts();
        seedDemoInventory();
    }

    public static PharmacyDataStore getInstance() {
        return SHARED_INSTANCE;
    }

    private void seedDemoAccounts() {
        registeredUserList.add(new DoctorUser(generateUserId(), "Dr. Aiman Rahman",
                "doctor@pharmacy.com", "012-3456789", "doctor123", "General Medicine"));
        registeredUserList.add(new PharmacistUser(generateUserId(), "Chong Wei Ling",
                "pharmacist@pharmacy.com", "012-2345678", "pharma123", "PH-88213"));
        registeredUserList.add(new AdministratorUser(generateUserId(), "System Administrator",
                "admin@pharmacy.com", "012-1234567", "admin123"));
        registeredUserList.add(new PatientUser(generateUserId(), "Low Kai Bing",
                "patient@pharmacy.com", "012-9876543", "patient123", "Penicillin"));
    }

    private void seedDemoInventory() {
        medicationInventory.add(new Medication("MED-001", "Paracetamol 500mg", 5.50, 200));
        medicationInventory.add(new Medication("MED-002", "Amoxicillin 250mg", 12.00, 80));
        medicationInventory.add(new Medication("MED-003", "Ibuprofen 200mg", 7.20, 15, 20));
        medicationInventory.add(new Medication("MED-004", "Cetirizine 10mg", 9.90, 60));
    }

    private String generateUserId() {
        return "USR-" + String.format("%03d", nextUserIdNumber++);
    }

    private String generatePrescriptionId() {
        return "RX-" + String.format("%04d", nextPrescriptionIdNumber++);
    }

    private String generateNotificationId() {
        return "NTF-" + String.format("%04d", nextNotificationIdNumber++);
    }

    // ---------------------------------------------------------------
    // Use case: Register
    // ---------------------------------------------------------------
    public PatientUser registerPatient(String fullName,
                                        String emailAddress,
                                        String phoneNumber,
                                        String password,
                                        String allergyHistory) throws DuplicateAccountException {
        if (findUserByEmail(emailAddress) != null) {
            throw new DuplicateAccountException(
                    "An account already exists for email: " + emailAddress);
        }
        PatientUser newPatient = new PatientUser(generateUserId(), fullName, emailAddress,
                phoneNumber, password, allergyHistory);
        registeredUserList.add(newPatient);
        return newPatient;
    }

    private SystemUser findUserByEmail(String emailAddress) {
        for (SystemUser user : registeredUserList) {
            if (user.getEmailAddress().equalsIgnoreCase(emailAddress)) {
                return user;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------
    // Use case: Log in
    // ---------------------------------------------------------------
    public SystemUser authenticateUser(String emailAddress, String password)
            throws InvalidCredentialException {
        SystemUser matchingUser = findUserByEmail(emailAddress);
        if (matchingUser == null || !matchingUser.matchesPassword(password)) {
            throw new InvalidCredentialException("Invalid email or password.");
        }
        if (!matchingUser.isActiveAccount()) {
            throw new InvalidCredentialException("This account has been deactivated.");
        }
        return matchingUser;
    }

    // ---------------------------------------------------------------
    // Use case: Manage and update prescription
    // ---------------------------------------------------------------
    public Prescription createPrescription(PatientUser patient,
                                            DoctorUser prescribingDoctor,
                                            List<PrescriptionItem> prescriptionItems) {
        Prescription newPrescription = new Prescription(generatePrescriptionId(), patient,
                prescribingDoctor, prescriptionItems);
        prescriptionList.add(newPrescription);
        sendNotification(patient.getEmailAddress(),
                "Your prescription " + newPrescription.getPrescriptionId()
                        + " has been submitted and is pending pharmacist verification.");
        return newPrescription;
    }

    // ---------------------------------------------------------------
    // Use case: Track prescription status
    // ---------------------------------------------------------------
    public List<Prescription> findPrescriptionsForPatient(String patientUserId) {
        List<Prescription> matchingPrescriptions = new ArrayList<>();
        for (Prescription prescription : prescriptionList) {
            if (prescription.getPatient().getUserId().equals(patientUserId)) {
                matchingPrescriptions.add(prescription);
            }
        }
        return matchingPrescriptions;
    }

    // ---------------------------------------------------------------
    // Use case: Verify prescription
    // ---------------------------------------------------------------
    public List<Prescription> findPrescriptionsByStatus(PrescriptionStatus status) {
        List<Prescription> matchingPrescriptions = new ArrayList<>();
        for (Prescription prescription : prescriptionList) {
            if (prescription.getCurrentStatus() == status) {
                matchingPrescriptions.add(prescription);
            }
        }
        return matchingPrescriptions;
    }

    public void verifyAndApprovePrescription(Prescription prescription) {
        prescription.setCurrentStatus(PrescriptionStatus.PREPARING);
        sendNotification(prescription.getPatient().getEmailAddress(),
                "Your prescription " + prescription.getPrescriptionId()
                        + " has been verified and is now being prepared.");
    }

    // ---------------------------------------------------------------
    // Use case: Dispense/sell medication (also updates inventory)
    // ---------------------------------------------------------------
    public void dispenseMedication(Prescription prescription) {
        for (PrescriptionItem item : prescription.getPrescriptionItems()) {
            item.getMedication().adjustStockQuantity(-item.getQuantity());
        }
        prescription.setCurrentStatus(PrescriptionStatus.DISPENSED);
        sendNotification(prescription.getPatient().getEmailAddress(),
                "Your prescription " + prescription.getPrescriptionId()
                        + " has been dispensed. Thank you for your purchase.");
    }

    // ---------------------------------------------------------------
    // Use case: Manage and update inventory
    // ---------------------------------------------------------------
    public List<Medication> getAllMedications() {
        return new ArrayList<>(medicationInventory);
    }

    public void adjustMedicationStock(Medication medication, int quantityDelta)
            throws InvalidStockAdjustmentException {
        if (medication.getStockQuantity() + quantityDelta < 0) {
            throw new InvalidStockAdjustmentException(
                    "Stock quantity cannot become negative for " + medication.getMedicationName());
        }
        medication.adjustStockQuantity(quantityDelta);
    }

    public List<Medication> getLowStockMedications() {
        List<Medication> lowStockItems = new ArrayList<>();
        for (Medication medication : medicationInventory) {
            if (medication.isLowStock()) {
                lowStockItems.add(medication);
            }
        }
        return lowStockItems;
    }

    // ---------------------------------------------------------------
    // Use case: Generate report
    // ---------------------------------------------------------------
    public String generateInventoryReport() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("=== INVENTORY REPORT ===\n");
        for (Medication medication : medicationInventory) {
            reportBuilder.append(String.format("%-25s | Stock: %-5d | Price: RM %.2f %s%n",
                    medication.getMedicationName(),
                    medication.getStockQuantity(),
                    medication.getUnitPrice(),
                    medication.isLowStock() ? "  <-- LOW STOCK" : ""));
        }
        return reportBuilder.toString();
    }

    public String generateSalesReport() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("=== SALES REPORT (Dispensed Prescriptions) ===\n");
        double grandTotalRevenue = 0.0;
        int dispensedCount = 0;
        for (Prescription prescription : prescriptionList) {
            if (prescription.getCurrentStatus() == PrescriptionStatus.DISPENSED) {
                dispensedCount++;
                grandTotalRevenue += prescription.getTotalAmount();
                reportBuilder.append(String.format("%-8s | %-20s | RM %.2f%n",
                        prescription.getPrescriptionId(),
                        prescription.getPatient().getFullName(),
                        prescription.getTotalAmount()));
            }
        }
        if (dispensedCount == 0) {
            reportBuilder.append("No records found.\n");
        } else {
            reportBuilder.append(String.format("Total revenue: RM %.2f%n", grandTotalRevenue));
        }
        return reportBuilder.toString();
    }

    // ---------------------------------------------------------------
    // Use case: Manage user access
    // ---------------------------------------------------------------
    public List<SystemUser> getAllRegisteredUsers() {
        return new ArrayList<>(registeredUserList);
    }

    public void setUserAccountActive(SystemUser user, boolean shouldBeActive) {
        user.setActiveAccount(shouldBeActive);
    }

    // ---------------------------------------------------------------
    // Use case: Send notification and alert
    // ---------------------------------------------------------------
    public void sendNotification(String recipientEmailAddress, String messageText) {
        notificationList.add(new NotificationMessage(generateNotificationId(),
                recipientEmailAddress, messageText));
    }

    public List<NotificationMessage> findNotificationsForUser(String emailAddress) {
        List<NotificationMessage> matchingNotifications = new ArrayList<>();
        for (NotificationMessage notification : notificationList) {
            if (notification.getRecipientEmailAddress().equalsIgnoreCase(emailAddress)) {
                matchingNotifications.add(notification);
            }
        }
        return matchingNotifications;
    }

    public List<DoctorUser> getAllDoctors() {
        List<DoctorUser> doctorList = new ArrayList<>();
        for (SystemUser user : registeredUserList) {
            if (user instanceof DoctorUser) {
                doctorList.add((DoctorUser) user);
            }
        }
        return doctorList;
    }

    public List<PatientUser> getAllPatients() {
        List<PatientUser> patientList = new ArrayList<>();
        for (SystemUser user : registeredUserList) {
            if (user instanceof PatientUser) {
                patientList.add((PatientUser) user);
            }
        }
        return patientList;
    }
}
