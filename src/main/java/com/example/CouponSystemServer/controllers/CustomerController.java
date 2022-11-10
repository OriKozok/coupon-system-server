package com.example.CouponSystemServer.controllers;

import com.example.CouponSystemServer.exceptions.*;
import com.example.CouponSystemServer.beans.Category;
import com.example.CouponSystemServer.beans.Coupon;
import com.example.CouponSystemServer.beans.OurSession;
import com.example.CouponSystemServer.facades.CustomerFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    private HashMap<String, OurSession> sessions;

    public CustomerController(HashMap<String, OurSession> sessions) {
        this.sessions = sessions;
    }

    /***
     * This method queries all the coupons purchased by the logged customer
     * @param request for user authorization
     * @return a set of all the coupons purchased by the logged customer
     */
    @GetMapping(path = "/coupons")
   public ResponseEntity<?> getCustomerCoupons(HttpServletRequest request){
       try {
           CustomerFacade customerFacade = checkAndUpdate(request);
           return ResponseEntity.ok(customerFacade.getCustomerCoupons());
       } catch (LoginException e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
       } catch (NoSuchCustomerException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
   }

    /***
     * This method queries all the purchased coupons of a specific category by the logged customer
     * @param request for user authorization
     * @return a set of all the purchased coupons of a specific category by the logged customer
     */
   @GetMapping(path = "/coupons/category/{category}")
   public ResponseEntity<?> getCustomerCoupons(@PathVariable Category category, HttpServletRequest request){
       try {
           CustomerFacade customerFacade = checkAndUpdate(request);
           return ResponseEntity.ok(customerFacade.getCustomerCoupons(category));
       } catch (LoginException e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
       } catch (NoSuchCustomerException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
   }

    /***
     * This method queries all the purchased coupons up to a certain price by the logged customer
     * @param request for user authorization
     * @return a set of all the purchased coupons of a specific category by the logged customer
     */
    @GetMapping(path = "/coupons/price/{maxPrice}")
    public ResponseEntity<?> getCustomerCoupons(@PathVariable double maxPrice, HttpServletRequest request){
        try {
            CustomerFacade customerFacade = checkAndUpdate(request);
            return ResponseEntity.ok(customerFacade.getCustomerCoupons(maxPrice));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        } catch (NoSuchCustomerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method gets the details of the logged customer
     * @param request for user authorization
     * @return a customer object representing the logged customer
     */
    @GetMapping
    public ResponseEntity<?> getCustomerDetails(HttpServletRequest request){
        try {
            CustomerFacade customerFacade = checkAndUpdate(request);
            return ResponseEntity.ok(customerFacade.getCustomerDetails());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        } catch (NoSuchCustomerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method receives a coupon object and purchases it for the customer
     * @param coupon a coupon object
     * @param request for user authorization
     * @return a String indicating that the coupon was purchased
     */
    @PostMapping
    public ResponseEntity<String> purchaseCoupon(@RequestBody Coupon coupon, HttpServletRequest request){
        try {
            CustomerFacade customerFacade = checkAndUpdate(request);
            customerFacade.purchaseCoupon(coupon);
            return ResponseEntity.ok("Coupon purchased!");
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method receives a coupon's id and removes its purchase it from the customer
     * @param id a coupon's id to remove purchase
     * @param request for user authorization
     * @return a String indicating that the coupon was purchased
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> deleteCouponPurchase(@PathVariable int id, HttpServletRequest request){
        try {
            CustomerFacade customerFacade = checkAndUpdate(request);
            customerFacade.deleteCouponPurchase(id);
            return ResponseEntity.ok("Coupon purchase deleted!");
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This method queries all the coupons from the DB
     * @param request for user authorization.
     * @return a Set of all coupons
     */
    @GetMapping("coupons/all")
    public ResponseEntity<?> getAllCoupons(HttpServletRequest request){
        try {
            CustomerFacade customerFacade = checkAndUpdate(request);
            return ResponseEntity.ok(customerFacade.getAllCoupons());
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
    @GetMapping("coupons/{id}")
    public ResponseEntity<?> getOneCoupon(@PathVariable int id, HttpServletRequest request){
        try {
            CustomerFacade customerFacade = checkAndUpdate(request);
            return ResponseEntity.ok(customerFacade.getOneCoupon(id));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }  catch (NoSuchCouponException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method is called from every other function in this controller.
     * It receives the token from the HTTPRequest and decodes it to determine if the user is authorized to execute these methods.
     * @param request contains a token for user authorization
     * @throws LoginException if a token is not valid
     * @return a customer facade
     */
    public CustomerFacade checkAndUpdate(HttpServletRequest request) throws LoginException {
        Object type = request.getAttribute("type");
        if(type != null && !(type.toString().equals("CUSTOMER"))) {
            throw new LoginException();
        }
        String token = request.getHeader("authorization").replace("Bearer ", "");
        OurSession session;
        try {
            session = sessions.get(token);
        }catch (Exception e){
            throw new LoginException();
        }
        session.setLastActive(System.currentTimeMillis());
        return (CustomerFacade) session.getFacade();
    }
}
