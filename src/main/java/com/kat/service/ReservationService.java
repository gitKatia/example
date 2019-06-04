package com.kat.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kat.model.Reservation;

public interface ReservationService {
	
	Optional<Reservation> getReservation(long id);
	Optional<Reservation> getReservationFor(long id, long routeId, LocalDate date);
	List<Reservation> getAllReservations();
	Map<Long,List<String>> getReservationsFor(long routeId, LocalDate date);
	Reservation createReservation(Reservation reservation);
	Optional<Reservation> editReservation(Reservation reservation);
	Optional<Reservation> deleteReservation(long id);
}
