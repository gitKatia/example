package com.kat.controller;

import static com.kat.model.ReservationBuilder.reservationBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kat.model.Reservation;
import com.kat.model.ReservationRequest;
import com.kat.model.Route;
import com.kat.model.Stop;
import com.kat.service.ReservationService;
import com.kat.service.RouteService;
import com.kat.service.StopService;

@RestController
public class ReservationController {

	private ReservationService reservationService;
	private StopService stopService;
	private RouteService routeService;

	@Autowired
	public ReservationController(ReservationService reservationService, StopService stopService,
			RouteService routeService) {
		this.reservationService = reservationService;
		this.stopService = stopService;
		this.routeService = routeService;
	}

	@RequestMapping(value = "reservations/{route_id}/{date}", method = RequestMethod.GET)
	public ResponseEntity<Map<Long, List<String>>> getReservations(@PathVariable("route_id") long routeId,
			@PathVariable("date") @DateTimeFormat(pattern = "ddMMyyyy") LocalDate date) {
		Map<Long, List<String>> reservationsMap = reservationService.getReservationsFor(routeId, date);
		if (reservationsMap == null || reservationsMap.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok().body(reservationsMap);
	}

	@RequestMapping(value = "reservations/{route_id}/{date}/{reservation_id}", method = RequestMethod.GET)
	public ResponseEntity<Reservation> getReservation(@PathVariable("route_id") long routeId,
			@PathVariable("date") @DateTimeFormat(pattern = "ddMMyyyy") LocalDate date,
			@PathVariable("reservation_id") long reservationId) {
		Optional<Reservation> reservationOptional = reservationService.getReservationFor(reservationId, routeId, date);
		if (reservationOptional.isPresent()) {
			return ResponseEntity.ok().body(reservationOptional.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@RequestMapping(value = "reservations/{route_id}/{date}", method = RequestMethod.POST)
	public ResponseEntity<Long> createReservation(@RequestBody ReservationRequest request,
			@PathVariable("route_id") long routeId,
			@PathVariable("date") @DateTimeFormat(pattern = "ddMMyyyy") LocalDate date) {
		Optional<Route> routeOptional = routeService.getRoute(routeId);
		if (!routeOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Stop> stopOptional = stopService.getStop(request.getStopId());
		if (!stopOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		if (!routeOptional.get().getStops().contains(stopOptional.get())) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
		}
		Reservation reservation = reservationBuilder()
				.childname(request.getChildname())
				.route(routeOptional.get())
				.stop(stopOptional.get())
				.date(date)
				.build();
		Reservation savedReservation = reservationService.createReservation(reservation);
		return ResponseEntity.ok().body(savedReservation.getId());
	}

	@RequestMapping(value = "reservations/{route_id}/{date}/{reservation_id}", method = RequestMethod.PUT)
	public ResponseEntity<Reservation> updateReservation(@RequestBody ReservationRequest request,
			@PathVariable("route_id") long routeId,
			@PathVariable("date") @DateTimeFormat(pattern = "ddMMyyyy") LocalDate date,
			@PathVariable("reservation_id") long reservationId) {
		Optional<Reservation> reservationOptional = reservationService.getReservationFor(reservationId, routeId, date);
		if (reservationOptional.isPresent()) {
			Optional<Stop> stopOptional = stopService.getStop(request.getStopId());
			if (!stopOptional.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			Optional<Route> routeOptional = routeService.getRoute(routeId);
			if (!routeOptional.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			Reservation reservation = reservationBuilder()
					.id(reservationId)
					.childname(request.getChildname())
					.route(routeOptional.get())
					.stop(stopOptional.get())
					.date(date).build();
			Reservation editedReservation = reservationService.editReservation(reservation).get();
			return ResponseEntity.ok().body(editedReservation);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@RequestMapping(value = "reservations/{route_id}/{date}/{reservation_id}", method = RequestMethod.DELETE)
	public ResponseEntity<Reservation> deleteReservation(@PathVariable("route_id") long routeId,
			@PathVariable("date") @DateTimeFormat(pattern = "ddMMyyyy") LocalDate date,
			@PathVariable("reservation_id") long reservationId) {
		Optional<Reservation> reservationOptional = reservationService.getReservationFor(reservationId, routeId, date);
		if (reservationOptional.isPresent()) {
			reservationService.deleteReservation(reservationId);
			Reservation deletedReservation = reservationOptional.get();
			return ResponseEntity.ok().body(deletedReservation);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

}
