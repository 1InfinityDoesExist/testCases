package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.beans.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("Select Product from #{#entityName} Product where id=?1")
    public Product getProductById(Long id);
    
    @Query("Select Product from #{#entityName} Product where productName=?1")
    public Product getProductByName(String productName);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update product set customer_id=?2 where id=?1")
    public void updateJoinBetweenProductAndCustomer(Long productId, Long customerId);

    
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update product set available_product_count = ?1 where product_name=?2")
    public void updateProductInInventoryTable(Long amount, String productName);

    
    /*
     * @Modifying
     * 
     * @Transactional
     * 
     * @Query("update product set customer = null where productName=?1 and customer = ?2"
     * ) public void updateCustomerIdInventoryTable(String productName, Long id);
     */
    
   

}
