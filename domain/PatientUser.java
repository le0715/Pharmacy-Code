package pharmacy.domain;

public class PatientUser extends SystemUser {

    private String allergyHistory;

    public PatientUser(String userId,
                        String fullName,
                        String emailAddress,
                        String phoneNumber,
                        String password,
                        String allergyHistory) {
        super(userId, fullName, emailAddress, phoneNumber, password, UserRole.PATIENT);
        this.allergyHistory = allergyHistory;
    }

    public String getAllergyHistory() {
        return allergyHistory;
    }

    public void setAllergyHistory(String allergyHistory) {
        this.allergyHistory = allergyHistory;
    }
}
