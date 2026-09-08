package pharmacy.domain;

public abstract class SystemUser {

    private final String userId;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String password;
    private final UserRole userRole;
    private boolean isActiveAccount;

    protected SystemUser(String userId,
                          String fullName,
                          String emailAddress,
                          String phoneNumber,
                          String password,
                          UserRole userRole) {
        this.userId = userId;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userRole = userRole;
        this.isActiveAccount = true;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean matchesPassword(String candidatePassword) {
        return this.password.equals(candidatePassword);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public boolean isActiveAccount() {
        return isActiveAccount;
    }

    public void setActiveAccount(boolean activeAccount) {
        this.isActiveAccount = activeAccount;
    }

    @Override
    public String toString() {
        return fullName + " (" + userRole + ")";
    }
}
