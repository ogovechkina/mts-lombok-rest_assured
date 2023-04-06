package com.example.library.repository;

import com.example.library.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Customer getById(Long customerId);
}
