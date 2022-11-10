package com.example.CouponSystemServer.utils;

import com.example.CouponSystemServer.beans.Coupon;
import com.example.CouponSystemServer.beans.Customer;
import com.example.CouponSystemServer.repositories.CouponRepository;
import com.example.CouponSystemServer.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CouponExpirationDailyJob implements Runnable {

    private CouponRepository couponRepository;
    private CustomerRepository customerRepository;
    private boolean quit;

    public CouponExpirationDailyJob(CouponRepository couponRepository, CustomerRepository customerRepository) {
        this.couponRepository = couponRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * This method stops the job's thread
     */
    public void stop() {
        quit = true;
    }

    /**
     * This method runs once a day and deletes expired coupons
     */
    @Override
    public void run()
    {
        while (!quit)
        {
            try {
                Set<Coupon> couponsToDelete = couponRepository.findAll().stream().filter(coupon -> coupon.getEndDate().getTime() <
                        new Date(System.currentTimeMillis()).getTime()).collect(Collectors.toSet());
                for (Coupon coupon : couponsToDelete) {
                    for (Customer customer : customerRepository.findAll()) {
                        if (customer.getCoupons().contains(coupon)) {
                            this.couponRepository.deleteCouponPurchase(coupon.getId(), customer.getId());
                        }
                    }
                    this.couponRepository.deleteById(coupon.getId());
                }
                Thread.sleep(1000 * 60 * 60 * 24);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
