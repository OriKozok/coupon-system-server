package com.example.CouponSystemServer.facades;


import com.example.CouponSystemServer.exceptions.*;
import com.example.CouponSystemServer.beans.Company;
import com.example.CouponSystemServer.beans.Coupon;
import com.example.CouponSystemServer.beans.Customer;
import com.example.CouponSystemServer.repositories.CompanyRepository;
import com.example.CouponSystemServer.repositories.CouponRepository;
import com.example.CouponSystemServer.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AdminFacade extends ClientFacade{

    public AdminFacade(CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository) {
        super(companyRepository, customerRepository, couponRepository);
    }

    /**
     * This method attempts to login as the admin.
     * @param email the email of the admin
     * @param password the password of the admin
     * @return 1 if the login succeeded and -1 if it failed.
     */
    @Override
    public int login(String email, String password) {
        return (email.equals("admin@admin.com") && password.equals("admin")) ? 1 : -1;
    }


    /**
     * This method receives a company object and attempts to add the company to the db using the BL.
     * @param company A comapny object
     * @throws CompanyAlreadyExistsException if the company already exists.
     */
    public Company addCompany(Company company) throws CompanyAlreadyExistsException {
        List<Company> companies = this.getCompanyRepository().findAll();
        if(companies.stream().anyMatch(c -> c.getName().equals(company.getName()) || c.getEmail().equals(company.getEmail())))
            throw new CompanyAlreadyExistsException();
        return this.getCompanyRepository().save(company);
    }

    /**
     * This method receives a company object and attempts to update the given company at the db using the BL.
     * @param company A comapny object
     * @throws NoSuchCompanyException if the company id isn't in the db.
     * @throws InvalidCompanyChangeException if the BL fails.
     */
    public Company updateCompany(Company company) throws InvalidCompanyChangeException, NoSuchCompanyException {
        Company c = this.getCompanyRepository().findById(company.getId()).orElseThrow(NoSuchCompanyException::new);
        if(!c.getName().equals(company.getName())){
            throw new InvalidCompanyChangeException();
        }
        return this.getCompanyRepository().save(company);
    }

    /**
     * This method receives a company id and attempts to delete the company from the db using the BL.
     * @param companyId a company's id.
     */
    public void deleteCompany(int companyId) {
        Set<Coupon> coupons = this.getCouponRepository().findByCompanyId(companyId);
        coupons.stream().forEach(c-> this.getCouponRepository().deleteById(c.getId()));
        this.getCompanyRepository().deleteById(companyId);
    }

    /**
     * This method returns all companies in an ArrayList.
     * @return an ArrayList containing all the companies.
     */
    public List<Company> getAllCompanies()
    {
        return this.getCompanyRepository().findAll();
    }

    /**
     * This method returns a company with the given id
     * @param companyID a company's id
     * @return a company object
     * @throws NoSuchCompanyException if the company id isn't in the db.
     */
    public Company getOneCompany(int companyID) throws NoSuchCompanyException {
        return this.getCompanyRepository().findById(companyID).orElseThrow(NoSuchCompanyException::new);
    }

    /**
     * This method add a customer to the DB using the BL.
     * @param customer A customer object
     * @throws CustomerAlreadyExistsException if the customer already exists.
     */
    public Customer addCustomer(Customer customer) throws CustomerAlreadyExistsException {

        List<Customer> customers = this.getCustomerRepository().findAll();
        if(customers.stream().anyMatch(c-> c.getEmail().equals(customer.getEmail())))
            throw new CustomerAlreadyExistsException();
        return this.getCustomerRepository().save(customer);
    }

    /**
     * This method updates a customer in the DB using the BL
     * @param customer A customer object
     * @throws CustomerAlreadyExistsException if the customer already exists.
     * @throws NoSuchCustomerException if the customer doesn't exist in the DB
     */
    public Customer updateCustomer(Customer customer) throws CustomerAlreadyExistsException, NoSuchCustomerException {
        List<Customer> customers = this.getCustomerRepository().findAll();
        Customer cust = this.getCustomerRepository().findById(customer.getId()).orElseThrow(NoSuchCustomerException::new);
        if(customers.stream().anyMatch(c-> c.getEmail().equals(customer.getEmail()) && c.getId() != customer.getId()))
            throw new CustomerAlreadyExistsException();
        return this.getCustomerRepository().save(customer);
    }

    /**
     * This method deletes a customer from the DB using the BL
     * @param customerId a customer's ID
     */
    public void deleteCustomer(int customerId) throws NoSuchCustomerException {
        Customer customer = this.getCustomerRepository().findById(customerId).orElseThrow(NoSuchCustomerException::new);
        for(Coupon coupon : customer.getCoupons())//Every coupon that's delted from this customer should have its amount raised by 1
        {
            coupon.setAmount(coupon.getAmount() + 1);
            this.getCouponRepository().save(coupon);
        }
        this.getCouponRepository().deleteCustomerCoupons(customerId);
        this.getCustomerRepository().deleteById(customerId);
    }

    /**
     * This method returns all the customers from the DB.
     * @return ArrayList of customers
     */
    public List<Customer> getAllCustomers(){
        return this.getCustomerRepository().findAll();
    }

    /**
     * This method returns a customer object from an id
     * @param customerID a customer's id
     * @return a customer object
     * @throws NoSuchCustomerException if the customer doesn't exist in the DB.
     */
    public Customer getOneCustomer(int customerID) throws NoSuchCustomerException {
        return this.getCustomerRepository().findById(customerID).orElseThrow(NoSuchCustomerException::new);
    }


}
