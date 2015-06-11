package my.wf.samlib.service;


import my.wf.samlib.model.entity.UserData;
import my.wf.samlib.model.entity.VerificationData;

public interface SecurityService {

    UserData getActiveUser();

    VerificationData registerNewUser(UserData user);

    UserData verifyUser(String verificationId);

    String generateVerificationLink(VerificationData verificationData);

}
