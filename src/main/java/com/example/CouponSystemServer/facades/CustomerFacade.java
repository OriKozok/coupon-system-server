package com.example.CouponSystemServer.facades;

import com.example.CouponSystemServer.exceptions.*;
import com.example.CouponSystemServer.beans.Category;
import com.example.CouponSystemServer.beans.Coupon;
import com.example.CouponSystemServer.beans.Customer;
import com.example.CouponSystemServer.repositories.CompanyRepository;
import com.example.CouponSystemServer.repositories.CouponRepository;
import com.example.CouponSystemServer.repositories.CustomerRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class CustomerFacade extends ClientFacade{

    private int customerID;

    public CustomerFacade(CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository) {
        super(companyRepository, customerRepository, couponRepository);
    }

    /**
     * This method attempts to login as a customer.
     * @param email the email of a customer
     * @param password the password of a customer
     * @return customer's id if the login succeeded and -1 if it failed.
     */
    @Override
    public int login(String email, String password) {
        Customer customer = this.getCustomerRepository().findCustomerByEmailAndPassword(email, password);
        this.customerID = customer != null? customer.getId() : -1;
        return this.customerID;
    }

    /**
     * This method receives a coupon object and attempts to purchase it to the customer.
     * @param coupon a coupon object
     * @throws CustomerAlreadyHasCouponException if the customer already has that coupon
     * @throws CouponIsNotAvailableException if the amount of the coupon is 0
     * @throws InvalidCouponDateException if the coupon's date expired
     * @throws NoSuchCustomerException if there is no such customer in the DB.
     * @throws NoSuchCouponException if there is no such coupon in the DB.
     */
    public void purchaseCoupon(Coupon coupon) throws CustomerAlreadyHasCouponException, CouponIsNotAvailableException, InvalidCouponDateException, NoSuchCustomerException, NoSuchCouponException {
        if (coupon.getAmount() == 0) {
            throw new CouponIsNotAvailableException();
        }
        if (coupon.getStartDate().getTime() > new Date(System.currentTimeMillis()).getTime() ||
                coupon.getEndDate().getTime() < new Date(System.currentTimeMillis()).getTime()) {
            throw new InvalidCouponDateException();
        }
        Customer customer = this.getCustomerRepository().findById(this.customerID).orElseThrow(NoSuchCustomerException::new);
        Set<Coupon> coupons = customer.getCoupons();
        if(coupons.stream().anyMatch(c -> c.getId() == coupon.getId()))
            throw new CustomerAlreadyHasCouponException();
        coupon.setAmount(coupon.getAmount() - 1);
        this.getCouponRepository().save(coupon);
        coupons.add(coupon);
        this.getCustomerRepository().save(customer);
    }

    /**
     * This method receives a coupon and delete its purchase from this customer
     * @param couponId a coupon's Id
     */
    public void deleteCouponPurchase(int couponId) throws NoSuchCouponException {
        this.getCouponRepository().deleteCouponPurchase(couponId, this.customerID);
        Coupon coupon = getCouponRepository().findById(couponId).orElseThrow(NoSuchCouponException::new);
        coupon.setAmount(coupon.getAmount() +1);
        this.getCouponRepository().save(coupon);
    }

    public List<Coupon> getAllCoupons(){
        return getCouponRepository().findAll();
    }

    /**
     * This method returns all the coupons the customer purchased
     * @return A list of the customer's coupons
     */
    public Set<Coupon> getCustomerCoupons() throws NoSuchCustomerException {
        Customer customer = this.getCustomerRepository().findById(this.customerID).orElseThrow(NoSuchCustomerException::new);
        return customer.getCoupons();
    }

    /**
     * This method returns all the coupons the customer purchased in a specific category
     * @param category A category enum object
     * @return A list of the customer's coupons in a specific category
     */
    public Set<Coupon> getCustomerCoupons(Category category) throws NoSuchCustomerException {
        Set<Coupon> coupons = getCustomerCoupons();
        return coupons.stream().filter(coupon -> coupon.getCategory() == category).collect(Collectors.toSet());
    }

    /**
     * This method returns all the coupons the customer purchased under a certain price
     * @param maxPrice the max price
     * @return A list of the customer's coupons under a certain price
     */
    public Set<Coupon> getCustomerCoupons(double maxPrice) throws NoSuchCustomerException {
        Set<Coupon> coupons = getCustomerCoupons();
        return coupons.stream().filter(coupon -> coupon.getPrice() <= maxPrice).collect(Collectors.toSet());
    }

    /**
     * This method returns the details of the customer
     * @return customer object with its details
     * @throws NoSuchCustomerException if the customer doesn't exist in the DB.
     */
    public Customer getCustomerDetails() throws NoSuchCustomerException {
        return this.getCustomerRepository().findById(this.customerID).orElseThrow(NoSuchCustomerException::new);
    }

    public Coupon getOneCoupon(int id) throws NoSuchCouponException {
        return getCouponRepository().findById(id).orElseThrow(NoSuchCouponException::new);
    }


    public int getCustomerID() {
        return customerID;
    }
}
