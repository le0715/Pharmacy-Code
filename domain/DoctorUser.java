package pharmacy.domain;

public class DoctorUser extends SystemUser {

    private String medicalDepartment;

    public DoctorUser(String userId,
                       String fullName,
                       String emailAddress,
                       String phoneNumber,
                       String password,
                       String medicalDepartment) {
        super(userId, fullName, emailAddress, phoneNumber, password, UserRole.DOCTOR);
        this.medicalDepartment = medicalDepartment;
    }

    public String getMedicalDepartment() {
        return medicalDepartment;
    }

    public void setMedicalDepartment(String medicalDepartment) {
        this.medicalDepartment = medicalDepartment;
    }
}
