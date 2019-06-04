package com.kat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kat.model.Stop;

public interface StopRepository extends JpaRepository<Stop, Long> {

}
