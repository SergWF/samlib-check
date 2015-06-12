package my.wf.samlib.service.impl;

import my.wf.samlib.model.entity.UserData;
import my.wf.samlib.model.entity.VerificationData;
import my.wf.samlib.model.repositoriy.UserDataRepository;
import my.wf.samlib.model.repositoriy.VerificationDataRepository;
import my.wf.samlib.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

@Service
public class SecurityServiceImpl implements SecurityService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Value("${registration.link.expiration.hours:2}")
    private int  expirationHours;
    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    VerificationDataRepository verificationDataRepository;

    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isAnonymous(){
        return "ROLE_ANONYMOUS".equals(SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority());
    }

    public UserData getActiveUser(){
        logger.debug("CALL GET ACTIVE USER");
        Authentication auth = getAuthentication();
        if(!auth.isAuthenticated() || isAnonymous()){
            logger.debug("NO ACTIVE USER FOUND");
            return null;
        }
        return userDataRepository.findByUsername(auth.getName());
    }

    @Override
    public VerificationData registerNewUser(UserData user) {
        user = userDataRepository.save(user);

        VerificationData vData = new VerificationData();
        vData.setUserData(user);
        vData.setId(UUID.randomUUID().toString());
        vData.setExpirationDate(getExpirationTime(new Date(), expirationHours));
        return verificationDataRepository.save(vData);
    }

    protected Date getExpirationTime(Date date, int expirationHours) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.HOUR, expirationHours);
        return cal.getTime();
    }

    @Override
    public UserData verifyUser(String verificationId) {
        VerificationData data = verificationDataRepository.findOne(verificationId);
        return null == data || new Date().after(data.getExpirationDate())?null:data.getUserData();
    }

    @Override
    public String generateVerificationLink(VerificationData verificationData) {
        return "/register/"+verificationData.getId();
    }


}
