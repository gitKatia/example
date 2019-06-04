package com.kat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kat.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
