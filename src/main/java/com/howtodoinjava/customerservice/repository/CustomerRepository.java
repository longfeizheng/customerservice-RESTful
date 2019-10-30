package com.howtodoinjava.customerservice.repository;

import com.howtodoinjava.customerservice.domin.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}