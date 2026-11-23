package com.example.demo.repository;

import com.example.demo.model.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KichCoRepository extends JpaRepository<KichCo, Integer> {
    KichCo findByTenSize(String tenSize);
}