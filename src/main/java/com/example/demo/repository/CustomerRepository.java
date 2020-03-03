package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.beans.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    @Query("Select Customer from #{#entityName} Customer where id=?1")
    public Customer getCustomerById(Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update customer set notes = ?2 where email=?1")
    public void updateNotes(String email, String string);

    
    @Query("Select Customer from #{#entityName} Customer where email=?1")
    public Customer findCusotmerByEmailId(String email);

}
