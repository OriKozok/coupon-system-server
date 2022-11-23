package com.example.CouponSystemServer.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.CouponSystemServer.beans.*;
import com.example.CouponSystemServer.facades.AdminFacade;
import com.example.CouponSystemServer.facades.ClientFacade;
import com.example.CouponSystemServer.facades.CompanyFacade;
import com.example.CouponSystemServer.facades.CustomerFacade;
import com.example.CouponSystemServer.repositories.CouponRepository;
import com.example.CouponSystemServer.utils.LoginManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    private LoginManager loginManager;
    private HashMap<String, OurSession> sessions;
    private CouponRepository couponRepository;

    public AuthController(LoginManager loginManager, HashMap<String, OurSession> sessions, CouponRepository couponRepository) {
        this.loginManager = loginManager;
        this.sessions = sessions;
        this.couponRepository = couponRepository;
    }

    /***
     * This method receives the credentials from the users and attempts to log it in.
     * The method also generates a token for the user and stores it in a session.
     * @param email user's email
     * @param password user's password
     * @return the user's id if it succeeded in logging in.
     */
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password){
        try {
            ClientFacade facade = loginManager.login(email, password);
            String token = createToken(facade);
            sessions.put(token, new OurSession(facade, System.currentTimeMillis()));
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("invalid credentials");
        }
    }

    /***
     * This method logs out the user, while removing its active token from the session.
     * @param request for user authorization
     * @return String which indicates that the user has signed out.
     */
    @PostMapping(path = "/out")
    public ResponseEntity<String> signOut(HttpServletRequest request){
        String token = request.getHeader("authorization").replace("Bearer ", "");
        sessions.remove(token);
        return ResponseEntity.ok("signed out");
    }

    /***
     * This method shows unauthorized users all the available coupons.
     * If a user clicks a coupon the program sends it to the login page.
     * @return a Set of all the coupons in the DB.
     */
    @GetMapping(path="/all")
    public List<Coupon> getAllCoupons(){
        return couponRepository.findAll();
    }


    /***
     * This method creates a token corresponding to a user's ClientType.
     * @param facade to determine the type of the user.
     * @return a Token object.
     * @throws Exception if there is a problem with the company or the customer.
     */
    private String createToken(ClientFacade facade) throws Exception{
        String token = "";
        if (facade instanceof CustomerFacade) {
            Customer customer = ((CustomerFacade) facade).getCustomerDetails();
            token = JWT.create()
                    .withIssuer("Dunder Mifflin")
                    .withIssuedAt(new Date())
                    .withClaim("id", customer.getId())
                    .withClaim("firstName", customer.getFirstName())
                    .withClaim("lastName", customer.getLastName())
                    .withClaim("email", customer.getEmail())
                    .withClaim("type", "CUSTOMER")
                    .sign(Algorithm.HMAC256("Fool me once, strike one. Fool me twice, strike three. - Wayne Gretzky - Michael Scott"));
        }
        else if (facade instanceof CompanyFacade) {
            Company company = ((CompanyFacade) facade).getCompanyDetails();
            token = JWT.create()
                    .withIssuer("Dunder Mifflin")
                    .withIssuedAt(new Date())
                    .withClaim("id", company.getId())
                    .withClaim("name", company.getName())
                    .withClaim("email", company.getEmail())
                    .withClaim("type", "COMPANY")
                    .sign(Algorithm.HMAC256("Fool me once, strike one. Fool me twice, strike three. - Wayne Gretzky - Michael Scott"));
        }
        if (facade instanceof AdminFacade) {
            token = JWT.create()
                    .withIssuer("Dunder Mifflin")
                    .withIssuedAt(new Date())
                    .withClaim("email", "admin@admin.com")
                    .withClaim("type", "ADMINISTRATOR")
                    .sign(Algorithm.HMAC256("Fool me once, strike one. Fool me twice, strike three. - Wayne Gretzky - Michael Scott"));
        }
        return token;
    }
}
