package com.example.CouponSystemServer.repositories;

import com.example.CouponSystemServer.beans.Category;
import com.example.CouponSystemServer.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    Set<Coupon> findByCompanyId(int companyId);
    Coupon findByIdAndCompanyId(int id, int companyId);
    List<Coupon> findByCompanyIdAndTitle(int companyId, String title);
    Set<Coupon> findByCompanyIdAndCategory(int companyId, Category category);
    Set<Coupon> findByCompanyIdAndPriceLessThan(int companyId, double price);

    /**
     * This method receives a coupon's id and deletes all its purchases from the customers_coupons table
     * @param id a coupon's id
     */
    @Transactional
    @Query(value = "delete from customers_coupons where coupons_id = ?1", nativeQuery = true)
    @Modifying
    void deleteCouponPurchaseHistory(int id);

    /**
     * This method receives a coupon's id and deletes all its purchases from the customers_coupons table
     * @param couponId a coupon's id
     * @param customerId a customer's id
     */
    @Transactional
    @Query(value = "delete from customers_coupons where coupons_id = ?1 and customer_id = ?2", nativeQuery = true)
    @Modifying
    void deleteCouponPurchase(int couponId, int customerId);

    /**
     * This method receives a customer's id and deletes all its purchases from the customers_coupons table
     * @param id a customer's id
     */
    @Transactional
    @Query(value = "delete from customers_coupons where customer_id = ?1", nativeQuery = true)
    @Modifying
    void deleteCustomerCoupons(int id);
}
