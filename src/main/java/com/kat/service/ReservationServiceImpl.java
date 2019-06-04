package com.kat.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kat.model.Reservation;
import com.kat.repository.ReservationRepository;

@Service
public class ReservationServiceImpl implements ReservationService {
	
	private ReservationRepository reservationRepository;

	public ReservationServiceImpl(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Override
	public Optional<Reservation> getReservation(long id) {
		return reservationRepository.findById(id);
	}
	
	@Override
	public Optional<Reservation> getReservationFor(long id, long routeId, LocalDate date) {
		Optional<Reservation> reservationOptional = reservationRepository.findById(id);
		if(reservationOptional.isPresent()) {
			Reservation reservation= reservationOptional.get();
			if(reservation.getDate().isEqual(date) && reservation.getRoute().getId() == routeId) {
				return Optional.of(reservation);
			}
			return Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public List<Reservation> getAllReservations() {
		return reservationRepository.findAll();
	}
	
	@Override
	public Map<Long, List<String>> getReservationsFor(long routeId, LocalDate date) {
		return getAllReservations().stream()
				.filter(reservation -> reservation.getRoute().getId() == routeId)
				.filter(reservation -> reservation.getDate().isEqual(date))
				.collect(Collectors.groupingBy(reservation -> reservation.getStop().getId(),
						Collectors.mapping(reservation -> childname(reservation), Collectors.toList())));
	}

	@Override
	public Reservation createReservation(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	@Override
	public Optional<Reservation> editReservation(Reservation reservation) {
		Optional<Reservation> reservationOptional = getReservation(reservation.getId());
		if(reservationOptional.isPresent()) {
			return Optional.of(reservationRepository.save(reservation));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Reservation> deleteReservation(long id) {
		Optional<Reservation> reservationOptional = getReservation(id);
		if(reservationOptional.isPresent()) {
			reservationRepository.deleteById(id);
			return reservationOptional;
		} else {
			return Optional.empty();
		}
	}
	
	private String childname(Reservation reservation) {
		return reservation.getChildname();
	}

}
