package com.example.CouponSystemServer.repositories;

import com.example.CouponSystemServer.beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findCustomerByEmailAndPassword(String email, String password);
}
