package com.kat.service;

import com.kat.model.Reservation;
import com.kat.model.Route;
import com.kat.repository.ReservationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kat.model.ReservationBuilder.reservationBuilder;
import static com.kat.model.StopBuilder.stopBuilder;
import static java.time.LocalDate.now;
import static java.time.LocalTime.NOON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceImplTest {

	@Mock
	private ReservationRepository reservationRepository;

	@InjectMocks
	private ReservationServiceImpl reservationService;

	@Test
	public void shouldRetrieveTheReservationGivenTheId() {

		Reservation reservation = new Reservation();
		Optional<Reservation> reservationOpt = Optional.of(reservation);
		when(reservationRepository.findById(anyLong())).thenReturn(reservationOpt);
		reservationService.getReservation(1);
		verify(reservationRepository).findById(1L);
	}

	@Test
	public void shouldRetrieveTheReservationGivenIdRouteIdAndDate() {

		LocalDate date = now();
		Reservation reservation = new Reservation();
		Route route = new Route();
		route.setId(1);
		reservation.setDate(date);
		reservation.setRoute(route);
		Optional<Reservation> reservationOpt = Optional.of(reservation);
		when(reservationRepository.findById(anyLong())).thenReturn(reservationOpt);
		Optional<Reservation> result = reservationService.getReservationFor(1, 1, date);
		verify(reservationRepository).findById(1L);
		assertTrue(result.isPresent());
	}

	@Test
	public void shouldNotRetrieveAnyReservationForTheGivenIdRouteIdAndDateBecauseOfDate() {

		LocalDate date = now();
		Reservation reservation = new Reservation();
		Route route = new Route();
		route.setId(1);
		reservation.setDate(date.plusDays(2));
		reservation.setRoute(route);
		Optional<Reservation> reservationOpt = Optional.of(reservation);
		when(reservationRepository.findById(anyLong())).thenReturn(reservationOpt);
		Optional<Reservation> result = reservationService.getReservationFor(1, 1, date);
		verify(reservationRepository).findById(1L);
		assertFalse(result.isPresent());
	}

	@Test
	public void shouldNotRetrieveAnyReservationForTheGivenIdRouteIdAndDateBecauseOfRouteId() {

		LocalDate date = now();
		Reservation reservation = new Reservation();
		Route route = new Route();
		route.setId(2);
		reservation.setDate(date);
		reservation.setRoute(route);
		Optional<Reservation> reservationOpt = Optional.of(reservation);
		when(reservationRepository.findById(anyLong())).thenReturn(reservationOpt);
		Optional<Reservation> result = reservationService.getReservationFor(1, 1, date);
		verify(reservationRepository).findById(anyLong());
		assertFalse(result.isPresent());
	}

	@Test
	public void shouldRetrieveChildrenBookedOnTheRouteAtTheSpecifiedDateGroupingByStop() {
		LocalDate date = now();
		Route route = new Route();
		route.setId(1);
		List<Reservation> reservations = new ArrayList<>();
		Reservation reservation1 = reservationBuilder()
				.route(route)
				.childname("Yvonne Dooling")
				.date(date)
				.stop(stopBuilder()
						.id(1)
						.pickupTime(NOON)
						.build())
				.build();
		Reservation reservation2 = reservationBuilder()
				.route(route)
				.childname("Benjamin Green")
				.date(date)
				.stop(stopBuilder()
						.id(1)
						.pickupTime(NOON)
						.build())
				.build();
		Reservation reservation3 = reservationBuilder()
				.route(route)
				.childname("Apara Miller")
				.date(date)
				.stop(stopBuilder()
						.id(2)
						.pickupTime(NOON.plusHours(1L))
						.build())
				.build();
		Reservation reservation4 = reservationBuilder()
				.route(route)
				.childname("James Simon")
				.date(date)
				.stop(stopBuilder()
						.id(2)
						.pickupTime(NOON.plusHours(1L))
						.build())
				.build();
		reservations.add(reservation1);
		reservations.add(reservation2);
		reservations.add(reservation3);
		reservations.add(reservation4);

		when(reservationRepository.findAll()).thenReturn(reservations);
		Map<Long,List<String>> result = reservationService.getReservationsFor(1, date);
		verify(reservationRepository).findAll();
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(result.get(1L), Arrays.asList("Yvonne Dooling", "Benjamin Green"));
		assertEquals(result.get(2L), Arrays.asList("Apara Miller", "James Simon"));
	}

	@Test
	public void shouldNotRetrieveChildrenBookedOnTheRouteAtTheSpecifiedDateGroupingByStop() {
		LocalDate date = now();
		Route route = new Route();
		route.setId(2);
		List<Reservation> reservations = new ArrayList<>();
		Reservation reservation1 = reservationBuilder()
				.route(route)
				.childname("Yvonne Dooling")
				.date(date)
				.stop(stopBuilder()
						.id(1)
						.pickupTime(NOON)
						.build())
				.build();
		Reservation reservation2 = reservationBuilder()
				.route(route)
				.childname("Benjamin Green")
				.date(date)
				.stop(stopBuilder()
						.id(1)
						.pickupTime(NOON)
						.build())
				.build();
		Reservation reservation3 = reservationBuilder()
				.route(route)
				.childname("Apara Miller")
				.date(date)
				.stop(stopBuilder()
						.id(2)
						.pickupTime(NOON.plusHours(1L))
						.build())
				.build();
		Reservation reservation4 = reservationBuilder()
				.route(route)
				.childname("James Simon")
				.date(date)
				.stop(stopBuilder()
						.id(2)
						.pickupTime(NOON.plusHours(1L))
						.build())
				.build();
		reservations.add(reservation1);
		reservations.add(reservation2);
		reservations.add(reservation3);
		reservations.add(reservation4);

		when(reservationRepository.findAll()).thenReturn(reservations);
		Map<Long,List<String>> result = reservationService.getReservationsFor(1, date);
		verify(reservationRepository).findAll();
		assertTrue(result.isEmpty());
	}

	@Test
	public void shouldRetrieveAllReservations() {

		when(reservationRepository.findAll()).thenReturn(new ArrayList<>());
		reservationService.getAllReservations();
		verify(reservationRepository).findAll();
	}

	@Test
	public void shouldSaveTheReservation() {

		Reservation reservation = new Reservation();
		when(reservationRepository.save(any())).thenReturn(reservation);
		reservationService.createReservation(reservation);
		verify(reservationRepository).save(reservation);
	}

	@Test
	public void shouldUpdateTheReservation() {

		Reservation reservation = new Reservation();
		Optional<Reservation> reservationOpt = Optional.of(reservation);
		when(reservationRepository.findById(anyLong())).thenReturn(reservationOpt);
		when(reservationRepository.save(any())).thenReturn(reservation);
		reservationService.editReservation(reservation);
		verify(reservationRepository).findById(anyLong());
		verify(reservationRepository).save(reservation);
	}

	@Test
	public void shouldNotUpdateTheReservation() {

		Reservation reservation = new Reservation();
		when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());
		reservationService.editReservation(reservation);
		verify(reservationRepository).findById(anyLong());
		verify(reservationRepository, never()).save(reservation);
	}

	@Test
	public void shouldDeleteTheReservation() {

		Reservation reservation = new Reservation();
		Optional<Reservation> reservationOpt = Optional.of(reservation);
		when(reservationRepository.findById(anyLong())).thenReturn(reservationOpt);
		doNothing().when(reservationRepository).deleteById(anyLong());
		reservationService.deleteReservation(1);
		verify(reservationRepository).findById(anyLong());
		verify(reservationRepository).deleteById(1L);
	}

	@Test
	public void shouldNotDeleteTheReservation() {

		when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());
		reservationService.deleteReservation(1);
		verify(reservationRepository).findById(anyLong());
		verify(reservationRepository, never()).deleteById(anyLong());
	}

}
