package com.example.demo.repository;
import com.example.demo.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PolicyRepository extends JpaRepository<Policy, Integer> {}