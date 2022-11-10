package com.example.CouponSystemServer.facades;

import com.example.CouponSystemServer.exceptions.*;
import com.example.CouponSystemServer.beans.Category;
import com.example.CouponSystemServer.beans.Company;
import com.example.CouponSystemServer.beans.Coupon;
import com.example.CouponSystemServer.repositories.CompanyRepository;
import com.example.CouponSystemServer.repositories.CouponRepository;
import com.example.CouponSystemServer.repositories.CustomerRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Scope("prototype")
public class CompanyFacade extends ClientFacade{
    private int companyID;

    public CompanyFacade(CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository) {
        super(companyRepository, customerRepository, couponRepository);

    }

    /**
     * This method attempts to log in as a company.
     * @param email the email of a company
     * @param password the password of a company
     * @return company's id if the login succeeded and -1 if it failed.
     */
    @Override
    public int login(String email, String password) {
        Company company = getCompanyRepository().findCompanyByEmailAndPassword(email, password);
        this.companyID = company != null? company.getId() : -1;
        return this.companyID;
    }

    /**
     * This method receives a company object and attempts to add the company to the db using the BL.
     * @param coupon A coupom object
     * @throws CouponAlreadyExistsException if the coupon already exists.
     * @throws InvalidCouponDateException if the date is not valid
     */
    public Coupon addCoupon(Coupon coupon) throws CouponAlreadyExistsException, InvalidCouponDateException {
        List<Coupon> titledCompanyCoupons = this.getCouponRepository().findByCompanyIdAndTitle(this.companyID, coupon.getTitle());
        if (titledCompanyCoupons.size() != 0)
            throw new CouponAlreadyExistsException();
        if (coupon.getStartDate().getTime() > coupon.getEndDate().getTime() ||
                coupon.getEndDate().getTime() < new Date(System.currentTimeMillis()).getTime()) {
            throw new InvalidCouponDateException();
        }
        coupon.setCompany(getCompanyRepository().findById(companyID).orElseThrow(CouponAlreadyExistsException::new));
        return this.getCouponRepository().save(coupon);
    }

    /**
     * This method receives a company object and attempts to update the given company at the db using the BL.
     * @param coupon A coupon object
     * @throws  InvalidCouponUpdateException if the coupon's id or company is wrong
     * @throws InvalidCouponDateException if the date is not valid
     */
    public Coupon updateCoupon(Coupon coupon) throws InvalidCouponUpdateException, NoSuchCouponException, InvalidCouponDateException {
        Coupon coupon1 = this.getCouponRepository().findById(coupon.getId()).orElseThrow(NoSuchCouponException::new);
        if(coupon.getCompany().getId() != this.companyID)
            throw new InvalidCouponUpdateException();
        List<Coupon> companyCoupons = this.getCouponRepository().findByCompanyIdAndTitle(this.companyID, coupon.getTitle());
        if(companyCoupons.size() != 1)//there should be only one coupon with that title in this company
            throw new InvalidCouponUpdateException();
        if (coupon.getStartDate().getTime() > coupon.getEndDate().getTime() ||
                coupon.getEndDate().getTime() < new Date(System.currentTimeMillis()).getTime()) {
            throw new InvalidCouponDateException();
        }
        return this.getCouponRepository().save(coupon);
    }

    /**
     * This method receives a coupon id and attempts to delete the coupon from the db using the BL.
     * @param couponID a coupon's id.
     * @throws NoSuchCouponException if a coupon id isn't in the db.
     * @throws InvalidCouponDeletionException if the BL isn't working properly.
     */
    public void deleteCoupon(int couponID) throws NoSuchCouponException, InvalidCouponDeletionException {
        //Checking if the company that wants to delete it is the company that made it
        Coupon coupon = this.getCouponRepository().findById(couponID).orElseThrow(NoSuchCouponException::new);
        if (coupon.getCompany().getId() != this.companyID) {
            throw new InvalidCouponDeletionException();
        }
        this.getCouponRepository().deleteCouponPurchaseHistory(couponID);
        this.getCouponRepository().deleteById(couponID);
    }

    /**
     * This method returns all the coupons of a company
     * @return all the coupons of a company
     */
    public Set<Coupon> getCompanyCoupons() {
        return this.getCouponRepository().findByCompanyId(this.companyID);
    }

    /**
     * This method returns all the coupons of a company at a certain category
     * @param category a category enum object
     * @return all the coupons of a company
     */
    public Set<Coupon> getCompanyCoupons(Category category) {
        return this.getCouponRepository().findByCompanyIdAndCategory(this.companyID, category);
    }
    /**
     * This method returns all the coupons of a company under a certain price
     * @param maxPrice the maximum price.
     * @return all the coupons of a company
     */
    public Set<Coupon> getCompanyCoupons(double maxPrice) {
        return this.getCouponRepository().findByCompanyIdAndPriceLessThan(this.companyID, maxPrice);
    }

    /**
     * This method returns the details of the company
     * @return company object with it's details
     * @throws NoSuchCompanyException if the company doesn't exist in the DB.
     */
    public Company getCompanyDetails() throws NoSuchCompanyException {
        return this.getCompanyRepository().findById(this.companyID).orElseThrow(NoSuchCompanyException::new);
    }

    /**
     * This method receives a coupon id and returns the company's coupon with that id.
     * @param couponID a coupon id
     * @return the company's coupon with that id
     * @throws NoSuchCouponException if the coupon doesn't exist in the DB.
     */
    public Coupon getOneCoupon(int couponID) throws NoSuchCouponException {
        return this.getCouponRepository().findById(couponID).orElseThrow(NoSuchCouponException::new);
    }

    public int getCompanyID() {
        return companyID;
    }
}
