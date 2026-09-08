package pharmacy.domain;


public class AdministratorUser extends SystemUser {

    public AdministratorUser(String userId,
                              String fullName,
                              String emailAddress,
                              String phoneNumber,
                              String password) {
        super(userId, fullName, emailAddress, phoneNumber, password, UserRole.ADMINISTRATOR);
    }
}
