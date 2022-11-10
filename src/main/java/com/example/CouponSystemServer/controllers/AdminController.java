package com.example.CouponSystemServer.controllers;

import com.example.CouponSystemServer.exceptions.*;
import com.example.CouponSystemServer.beans.Company;
import com.example.CouponSystemServer.beans.Customer;
import com.example.CouponSystemServer.facades.AdminFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private AdminFacade adminFacade;

    /**
     * This method queries for a customer from the DB based on its id
     * @param id the id of the customer.
     * @param request for user authorization.
     * @return a customer object from the DB
     */
    @GetMapping(path = "/customer/{id}")
    public ResponseEntity<?> getOneCustomer(@PathVariable int id, HttpServletRequest request) {
        try {
            ifAuthorized(request);
            return ResponseEntity.ok(adminFacade.getOneCustomer(id));
        } catch (NoSuchCustomerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /**
     * This method queries for a company from the DB based on its id
     * @param id the id of the company.
     * @param request for user authorization
     * @return a company object from the DB
     */
    @GetMapping(path = "/company/{id}")
    public ResponseEntity<?> getOneCompany(@PathVariable int id, HttpServletRequest request) {
        try {
            ifAuthorized(request);
            return ResponseEntity.ok(adminFacade.getOneCompany(id));
        } catch (NoSuchCompanyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method queries all the companies from the DB
     * @param request for user authorization
     * @return a Set of all the companies.
     */
    @GetMapping(path = "/company")
    public ResponseEntity<?> getAllCompanies(HttpServletRequest request) {
        try {
            ifAuthorized(request);
            return ResponseEntity.ok(adminFacade.getAllCompanies());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method queries all the customers from the DB
     * @param request for user authorization
     * @return a Set of all the customers.
     */
    @GetMapping(path = "/customer")
    public ResponseEntity<?> getAllCustomers(HttpServletRequest request) {
        try {
            ifAuthorized(request);
            return ResponseEntity.ok(adminFacade.getAllCustomers());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method attempts to add a customer to the DB
     * @param customer a customer object to add to the DB.
     * @param request for user authorization
     * @return the added customer object.
     */
    @PostMapping(path = "/customer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer, HttpServletRequest request) {
        try {
            ifAuthorized(request);
            return ResponseEntity.ok(adminFacade.addCustomer(customer));
        } catch (CustomerAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method attempts to add a company to the DB
     * @param company a company object to add to the DB.
     * @param request for user authorization
     * @return the added company object.
     */
    @PostMapping(path = "/company")
    public ResponseEntity<?> addCompany(@RequestBody Company company, HttpServletRequest request) {
        try {
            ifAuthorized(request);
            return ResponseEntity.ok(adminFacade.addCompany(company));
        } catch (CompanyAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method attempts to update a company at the DB.
     * @param company a company object to update.
     * @param request for user authorization
     * @return the updated company object.
     */
    @PutMapping(path = "/company")
    public ResponseEntity<?> updateCompany(@RequestBody Company company, HttpServletRequest request) {
        try {
            ifAuthorized(request);
            return ResponseEntity.ok(adminFacade.updateCompany(company));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method attempts to update a customer at the DB.
     * @param customer a customer object to update.
     * @param request for user authorization
     * @return the updated customer object.
     */
    @PutMapping(path = "/customer")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, HttpServletRequest request) {
        try {
            ifAuthorized(request);
            return ResponseEntity.ok(adminFacade.updateCustomer(customer));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method receives an id and deletes its corresponding customer from the DB
     * @param id a customer's id to delete
     * @param request for user authorization
     * @return String which indicates that the customer was deleted.
     */
    @DeleteMapping(path = "/customer/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable int id, HttpServletRequest request) {
        try {
            ifAuthorized(request);
            adminFacade.deleteCustomer(id);
            return ResponseEntity.ok("Customer deleted!");
        } catch (NoSuchCustomerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method receives an id and deletes its corresponding company from the DB
     * @param id a company's id to delete
     * @param request for user authorization
     * @return String which indicates that the company was deleted.
     */
    @DeleteMapping(path = "/company/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable int id, HttpServletRequest request) {
        try {
            ifAuthorized(request);
            adminFacade.deleteCompany(id);
            return ResponseEntity.ok("Company deleted!");
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized for this action");
        }
    }

    /***
     * This method is called from every other function in this controller.
     * It receives the token from the HTTPRequest and decodes it to determine if the user is authorized to execute these methods.
     * @param request contains a token for user authorization
     * @throws LoginException if a token is not valid
     */
    public void ifAuthorized(HttpServletRequest request) throws LoginException {
        String type = request.getAttribute("type").toString();
        if (!type.equals("ADMINISTRATOR")) {
            throw new LoginException();
        }
    }
}
