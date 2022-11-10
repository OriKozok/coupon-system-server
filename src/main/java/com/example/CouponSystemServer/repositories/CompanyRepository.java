package com.example.CouponSystemServer.repositories;

import com.example.CouponSystemServer.beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Company findCompanyByEmailAndPassword(String email, String password);


}
