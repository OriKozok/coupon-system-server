package com.example.CouponSystemServer.facades;

import com.example.CouponSystemServer.repositories.CompanyRepository;
import com.example.CouponSystemServer.repositories.CouponRepository;
import com.example.CouponSystemServer.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class ClientFacade {

    private CompanyRepository companyRepository;
    private CustomerRepository customerRepository;
    private CouponRepository couponRepository;

    public ClientFacade(CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public CouponRepository getCouponRepository() {
        return couponRepository;
    }

    public abstract int login(String email, String password);
}
