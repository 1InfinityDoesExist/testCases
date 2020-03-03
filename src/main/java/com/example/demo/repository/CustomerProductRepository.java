package com.example.demo.repository;

import java.time.LocalDate;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.beans.CustomerProduct;

@Repository
public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Long> {

    @Query("Select CustomerProduct from #{#entityName} CustomerProduct where id=?1")
    public CustomerProduct getCustomerProductById(Long id);

    
    @Query("Select CustomerProduct from #{#entityName} CustomerProduct where emailId = ?1 and billingDate=?2")
    public Set<CustomerProduct> getAllCustomerProductDeliveryDate(String email, LocalDate billingDate);


    @Query("Select CustomerProduct from #{#entityName} CustomerProduct where productName = ?1 and deliveryStatus=?2 order by creationDate asc")
    public Set<CustomerProduct> findPendingCustomers(String productName, String string);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update CustomerProduct delivery_status=?1 where customer_emailid=?2")
    public void updatePendingCustomerStatus(String string, String email);

    @Query("Select CustomerProduct from #{#entityName} CustomerProduct where productName = ?1 and deliveryStatus=?2")
    public CustomerProduct getPendingCustomer(String productName, String string);

}
