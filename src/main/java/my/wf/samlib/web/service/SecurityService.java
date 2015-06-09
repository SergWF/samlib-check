package my.wf.samlib.web.service;

import my.wf.samlib.model.entity.Customer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isAnonymous(){
        return "ROLE_ANONYMOUS".equals(SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority());
    }

    public Customer getActiveCustomer(){
        System.out.println("CALL GET ACTIVE USER");
        Authentication auth = getAuthentication();
        if(!auth.isAuthenticated() || isAnonymous()){
            System.out.println("NO ACTIVE USER FOUND");
            return null;
        }

        Customer customer = new Customer();
        customer.setName(auth.getName());
        System.out.println("ACTIVE USER: " + customer.getName());
        return customer;
    }

}
