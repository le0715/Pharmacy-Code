package pharmacy.domain;


public class PharmacistUser extends SystemUser {

    private String licenseNumber;

    public PharmacistUser(String userId,
                           String fullName,
                           String emailAddress,
                           String phoneNumber,
                           String password,
                           String licenseNumber) {
        super(userId, fullName, emailAddress, phoneNumber, password, UserRole.PHARMACIST);
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
