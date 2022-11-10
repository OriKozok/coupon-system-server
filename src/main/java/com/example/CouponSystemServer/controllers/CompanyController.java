package com.example.CouponSystemServer.controllers;


import com.example.CouponSystemServer.exceptions.*;
import com.example.CouponSystemServer.beans.Category;
import com.example.CouponSystemServer.beans.Coupon;
import com.example.CouponSystemServer.beans.OurSession;
import com.example.CouponSystemServer.facades.CompanyFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController ///WORKS
@RequestMapping(path = "/company")
public class CompanyController {


    private HashMap<String, OurSession> sessions;


    public CompanyController(HashMap<String, OurSession> sessions) {
        this.sessions = sessions;
    }

    /***
     * This method queries all the coupons by the logged company
     * @param request for user authorization
     * @return a set of all the coupons by the logged company
     */
    @GetMapping(path = "/coupons")
    public ResponseEntity<?> getCompanyCoupons(HttpServletRequest request) {
        try {
            CompanyFacade companyFacade = checkAndUpdate(request);
            return ResponseEntity.ok(companyFacade.getCompanyCoupons());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method queries all the coupons of a specific category by the logged company
     * @param request for user authorization
     * @return a set of all the coupons of a specific category by the logged company
     */
    @GetMapping(path = "/coupons/category/{category}")
    public ResponseEntity<?> getCompanyCoupons(@PathVariable Category category, HttpServletRequest request) {
        try {
            CompanyFacade companyFacade = checkAndUpdate(request);
            return ResponseEntity.ok(companyFacade.getCompanyCoupons(category));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method queries all the coupons up to a certain price by the logged company
     * @param request for user authorization
     * @return a set of all the coupons of a specific category by the logged company
     */
    @GetMapping(path = "/coupons/price/{maxPrice}")
    public ResponseEntity<?> getCompanyCoupons(@PathVariable double maxPrice, HttpServletRequest request) {
        try {
            CompanyFacade companyFacade = checkAndUpdate(request);
            return ResponseEntity.ok(companyFacade.getCompanyCoupons(maxPrice));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method receives a coupon object and adds it to the DB
     * @param coupon a coupon object
     * @param request for user authorization
     * @return the coupon object with the new id
     */
    @PostMapping
    public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon, HttpServletRequest request) {
        try {
            CompanyFacade companyFacade = checkAndUpdate(request);
            return ResponseEntity.ok(companyFacade.addCoupon(coupon));
        } catch (CouponAlreadyExistsException | InvalidCouponDateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method receives an existing coupon object and updates it to DB.
     * @param coupon a coupon object
     * @param request for user authorization
     * @return the coupon object with the new id
     */
    @PutMapping
    public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon, HttpServletRequest request) {
        try {
            CompanyFacade companyFacade = checkAndUpdate(request);
            return ResponseEntity.ok(companyFacade.updateCoupon(coupon));
        } catch (InvalidCouponUpdateException | NoSuchCouponException | InvalidCouponDateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method receives a coupon's id and deletes it from the DB
     * @param id a coupon id
     * @param request for user authorization
     * @return a string indicating that the coupon was deleted
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable int id, HttpServletRequest request) {
        try {
            CompanyFacade companyFacade = checkAndUpdate(request);
            companyFacade.deleteCoupon(id);
            return ResponseEntity.ok("Coupon Deleted!");
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method gets the details of the logged company
     * @param request for user authorization
     * @return a company object representing the logged company
     */
    @GetMapping
    public ResponseEntity<?> getCompanyDetails(HttpServletRequest request) {
        try {
            CompanyFacade companyFacade = checkAndUpdate(request);
            return ResponseEntity.ok(companyFacade.getCompanyDetails());
        } catch (NoSuchCompanyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /**
     * This method queries for a coupon from the DB based on its id
     * @param id the id of the coupon.
     * @param request for user authorization.
     * @return a coupon object from the DB
     */
    @GetMapping(path = "coupons/{id}")
    public ResponseEntity<?> getOneCoupon(@PathVariable int id, HttpServletRequest request){
        try {
            CompanyFacade companyFacade = checkAndUpdate(request);
            return ResponseEntity.ok(companyFacade.getOneCoupon(id));
        } catch (NoSuchCouponException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method is called from every other function in this controller.
     * It receives the token from the HTTPRequest and decodes it to determine if the user is authorized to execute these methods.
     * @param request contains a token for user authorization
     * @throws LoginException if a token is not valid
     * @return a company facade
     */
    public CompanyFacade checkAndUpdate(HttpServletRequest request) throws LoginException {
        Object type = request.getAttribute("type");
        if(type != null && !(type.toString().equals("COMPANY"))) {
            throw new LoginException();
        }
        String token = request.getHeader("authorization").replace("Bearer ", "");
        OurSession session;
        try {
            session = sessions.get(token);
            session.setLastActive(System.currentTimeMillis());
            return (CompanyFacade) session.getFacade();
        }catch (Exception e){
            throw new LoginException();
        }

    }
}