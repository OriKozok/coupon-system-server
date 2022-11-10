package com.example.CouponSystemServer.utils;

import com.example.CouponSystemServer.beans.ClientType;
import com.example.CouponSystemServer.facades.AdminFacade;
import com.example.CouponSystemServer.facades.ClientFacade;
import com.example.CouponSystemServer.facades.CompanyFacade;
import com.example.CouponSystemServer.facades.CustomerFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class LoginManager {

    private final ApplicationContext applicationContext;

    public LoginManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * This method receives email, password and client's type and checks if the user with these credentials exists in the DB
     * @param email the user's email
     * @param password the user's password
     * @return a corresponding facade
     * @throws LoginException if there isn't a user with these credentials
     */
    public ClientFacade login(String email, String password) throws LoginException {
        ClientFacade facade = applicationContext.getBean(AdminFacade.class);
        if(facade.login(email, password) != -1)
            return facade;
        else {
            facade = applicationContext.getBean(CustomerFacade.class);
            if(facade.login(email, password) != -1)
                return facade;
            else {
                facade = applicationContext.getBean(CompanyFacade.class);
                if(facade.login(email, password) != -1)
                    return facade;
                else
                    throw new LoginException();
            }
        }
    }
}
