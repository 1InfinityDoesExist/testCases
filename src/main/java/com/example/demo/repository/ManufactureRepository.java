package com.example.demo.repository;

import java.time.LocalDate;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.beans.ManufacturingUnit;

@Repository
public interface ManufactureRepository extends JpaRepository<ManufacturingUnit, Long> {

    @Query("UPDATE #{#entityName} ManufacturingUnit SET   deleteFlag=true WHERE productName =?1 and manufacturedEndDate=?2")
    @Modifying
    @Transactional
    void updateDeleteFlag(String productName , LocalDate date);

    @Query("Select ManufacturingUnit from #{#entityName} ManufacturingUnit where id= ?1")
    public ManufacturingUnit getManuUnitById(Long id);

    @Query("Select ManufacturingUnit from #{#entityName} ManufacturingUnit where productName=?1 and deleteFlag=false  ")
    public ManufacturingUnit findProductHistoryByName(String productName);

    // Get All the products that can be exported to the inventory today...!!!
    @Query("Select ManufacturingUnit from #{#entityName} ManufacturingUnit where deleteFlag = false and availibility_date = ?1")
    public Set<ManufacturingUnit> getAllReadyProductOfToday(LocalDate date);

}
