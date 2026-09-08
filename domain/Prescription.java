package pharmacy.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Prescription {

    private final String prescriptionId;
    private final PatientUser patient;
    private final DoctorUser prescribingDoctor;
    private final List<PrescriptionItem> prescriptionItems;
    private final LocalDate dateIssued;
    private PrescriptionStatus currentStatus;

    public Prescription(String prescriptionId,
                         PatientUser patient,
                         DoctorUser prescribingDoctor,
                         List<PrescriptionItem> prescriptionItems) {
        this.prescriptionId = prescriptionId;
        this.patient = patient;
        this.prescribingDoctor = prescribingDoctor;
        this.prescriptionItems = new ArrayList<>(prescriptionItems);
        this.dateIssued = LocalDate.now();
        this.currentStatus = PrescriptionStatus.PENDING;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PatientUser getPatient() {
        return patient;
    }

    public DoctorUser getPrescribingDoctor() {
        return prescribingDoctor;
    }

    public List<PrescriptionItem> getPrescriptionItems() {
        return Collections.unmodifiableList(prescriptionItems);
    }

    public LocalDate getDateIssued() {
        return dateIssued;
    }

    public PrescriptionStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(PrescriptionStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public double getTotalAmount() {
        double totalAmount = 0.0;
        for (PrescriptionItem item : prescriptionItems) {
            totalAmount += item.getSubtotalAmount();
        }
        return totalAmount;
    }

    public String getItemSummary() {
        StringBuilder summaryBuilder = new StringBuilder();
        for (int index = 0; index < prescriptionItems.size(); index++) {
            if (index > 0) {
                summaryBuilder.append(", ");
            }
            summaryBuilder.append(prescriptionItems.get(index).getMedication().getMedicationName())
                    .append(" x")
                    .append(prescriptionItems.get(index).getQuantity());
        }
        return summaryBuilder.toString();
    }
}
