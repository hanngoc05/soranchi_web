package com.example.demo.repository;

import com.example.demo.model.ShippingUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingUnitRepository extends JpaRepository<ShippingUnit, Integer> {
    ShippingUnit findByNameIgnoreCase(String name);
}