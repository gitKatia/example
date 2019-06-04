package com.kat.controller;

import static com.kat.model.ReservationBuilder.reservationBuilder;
import static java.time.LocalDate.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kat.model.Reservation;
import com.kat.model.ReservationRequest;
import com.kat.model.Route;
import com.kat.model.Stop;
import com.kat.service.ReservationService;
import com.kat.service.RouteService;
import com.kat.service.StopService;

@RunWith(MockitoJUnitRunner.class)
public class ReservationControllerTest {

	@Mock
	private ReservationService reservationService;

	@Mock
	private RouteService routeService;

	@Mock
	private StopService stopService;

	@InjectMocks
	private ReservationController reservationController;

	@Test
	public void shouldGetReservationsForTheGivenRouteAtTheSpecifiedDate() {
		LocalDate date = now();
		Map<Long, List<String>> reservationsMap = new HashMap<>();
		List<String> children1 = Arrays.asList("Agatha Jordan", "Stephen Dooling");
		List<String> children2 = Arrays.asList("Robert White", "Jack Green");
		reservationsMap.put(1L, children1);
		reservationsMap.put(2L, children2);
		when(reservationService.getReservationsFor(anyLong(), any())).thenReturn(reservationsMap);
		ResponseEntity<Map<Long, List<String>>> result = reservationController.getReservations(1, date);
		verify(reservationService).getReservationsFor(anyLong(), any());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(reservationsMap, result.getBody());
	}

	@Test
	public void shouldGetNoReservationsForTheGivenRouteAtTheSpecifiedDate() {
		LocalDate date = now();
		Map<Long, List<String>> reservationsMap = new HashMap<>();
		when(reservationService.getReservationsFor(anyLong(), any())).thenReturn(reservationsMap);
		ResponseEntity<Map<Long, List<String>>> result = reservationController.getReservations(1, date);
		verify(reservationService).getReservationsFor(anyLong(), any());
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
		assertNull(result.getBody());
	}

	@Test
	public void shouldGetReservationForTheGivenReservationIdDateAndRouteId() {
		LocalDate date = now();
		Reservation reservation = new Reservation();
		when(reservationService.getReservationFor(anyLong(), anyLong(), any())).thenReturn(Optional.of(reservation));
		ResponseEntity<Reservation> result = reservationController.getReservation(1, date, 1);
		verify(reservationService).getReservationFor(anyLong(), anyLong(), any());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(reservation, result.getBody());
	}

	@Test
	public void shouldGetNoReservationForTheGivenReservationIdAndDateAndRouteId() {
		LocalDate date = now();
		when(reservationService.getReservationFor(anyLong(), anyLong(), any())).thenReturn(Optional.empty());
		ResponseEntity<Reservation> result = reservationController.getReservation(1, date, 1);
		verify(reservationService).getReservationFor(anyLong(), anyLong(), any());
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

	@Test
	public void shouldCreateAReservation() {
		LocalDate date = now();
		ReservationRequest request = new ReservationRequest();
		request.setChildname("Robin Jordan");
		request.setStopId(1);
		Route route = new Route();
		Stop stop = new Stop();
		Set<Stop> stops = new HashSet<>();
		stops.add(stop);
		route.setStops(stops);

		Reservation reservation = reservationBuilder().id(1).childname(request.getChildname()).route(route).stop(stop)
				.date(date).build();

		when(routeService.getRoute(anyLong())).thenReturn(Optional.of(route));
		when(stopService.getStop(anyLong())).thenReturn(Optional.of(stop));
		when(reservationService.createReservation(any())).thenReturn(reservation);

		ResponseEntity<Long> result = reservationController.createReservation(request, 1, date);

		verify(routeService).getRoute(anyLong());
		verify(stopService).getStop(request.getStopId());
		verify(reservationService).createReservation(any());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(Long.valueOf(reservation.getId()), result.getBody());
	}

	@Test
	public void shouldNotCreateTheReservationBecauseOfRouteNotFound() {
		LocalDate date = now();
		ReservationRequest request = new ReservationRequest();
		when(routeService.getRoute(anyLong())).thenReturn(Optional.empty());

		ResponseEntity<Long> result = reservationController.createReservation(request, 1, date);

		verify(routeService).getRoute(anyLong());
		verify(stopService, never()).getStop(anyLong());
		verify(reservationService, never()).createReservation(any());
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

	@Test
	public void shouldNotCreateTheReservationBecauseOfStopNotFound() {
		LocalDate date = now();
		ReservationRequest request = new ReservationRequest();
		Route route = new Route();
		when(routeService.getRoute(anyLong())).thenReturn(Optional.of(route));
		when(stopService.getStop(anyLong())).thenReturn(Optional.empty());

		ResponseEntity<Long> result = reservationController.createReservation(request, 1, date);

		verify(routeService).getRoute(anyLong());
		verify(stopService).getStop(anyLong());
		verify(reservationService, never()).createReservation(any());
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

	@Test
	public void shouldNotCreateTheReservationBecauseTheRouteDoesNotIncludeTheStop() {
		LocalDate date = now();
		ReservationRequest request = new ReservationRequest();
		Route route = new Route();
		Stop stop = new Stop();
		when(routeService.getRoute(anyLong())).thenReturn(Optional.of(route));
		when(stopService.getStop(anyLong())).thenReturn(Optional.of(stop));

		ResponseEntity<Long> result = reservationController.createReservation(request, 1, date);

		verify(routeService).getRoute(anyLong());
		verify(stopService).getStop(anyLong());
		verify(reservationService, never()).createReservation(any());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, result.getStatusCode());
		assertNull(result.getBody());
	}

	@Test
	public void shouldUpdateTheReservation() {
		LocalDate date = now();
		ReservationRequest request = new ReservationRequest();
		Reservation reservation = new Reservation();
		Stop stop = new Stop();
		Route route = new Route();
		when(reservationService.getReservationFor(anyLong(), anyLong(), any())).thenReturn(Optional.of(reservation));
		when(stopService.getStop(anyLong())).thenReturn(Optional.of(stop));
		when(routeService.getRoute(anyLong())).thenReturn(Optional.of(route));
		when(reservationService.editReservation(any())).thenReturn(Optional.of(reservation));

		ResponseEntity<Reservation> result = reservationController.updateReservation(request, 1, date, 2);

		verify(reservationService).getReservationFor(anyLong(), anyLong(), any());
		verify(stopService).getStop(anyLong());
		verify(routeService).getRoute(anyLong());
		verify(reservationService).editReservation(any());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(reservation, result.getBody());
	}

	@Test
	public void shouldNotUpdateTheReservationBecauseOfReservationNotFound() {
		LocalDate date = now();
		ReservationRequest request = new ReservationRequest();
		when(reservationService.getReservationFor(anyLong(), anyLong(), any())).thenReturn(Optional.empty());

		ResponseEntity<Reservation> result = reservationController.updateReservation(request, 1, date, 2);

		verify(reservationService).getReservationFor(anyLong(), anyLong(), any());
		verify(stopService, never()).getStop(anyLong());
		verify(routeService, never()).getRoute(anyLong());
		verify(reservationService, never()).editReservation(any());
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

	@Test
	public void shouldNotUpdateTheReservationBecauseOfStopNotFound() {
		LocalDate date = now();
		ReservationRequest request = new ReservationRequest();
		Reservation reservation = new Reservation();
		when(reservationService.getReservationFor(anyLong(), anyLong(), any())).thenReturn(Optional.of(reservation));
		when(stopService.getStop(anyLong())).thenReturn(Optional.empty());

		ResponseEntity<Reservation> result = reservationController.updateReservation(request, 1, date, 2);

		verify(reservationService).getReservationFor(anyLong(), anyLong(), any());
		verify(stopService).getStop(anyLong());
		verify(routeService, never()).getRoute(anyLong());
		verify(reservationService, never()).editReservation(any());
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

	@Test
	public void shouldNotUpdateTheReservationBecauseOfRouteNotFound() {
		LocalDate date = now();
		ReservationRequest request = new ReservationRequest();
		Reservation reservation = new Reservation();
		Stop stop = new Stop();
		when(reservationService.getReservationFor(anyLong(), anyLong(), any())).thenReturn(Optional.of(reservation));
		when(stopService.getStop(anyLong())).thenReturn(Optional.of(stop));
		when(routeService.getRoute(anyLong())).thenReturn(Optional.empty());

		ResponseEntity<Reservation> result = reservationController.updateReservation(request, 1, date, 2);

		verify(reservationService).getReservationFor(anyLong(), anyLong(), any());
		verify(stopService).getStop(anyLong());
		verify(routeService).getRoute(anyLong());
		verify(reservationService, never()).editReservation(any());
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

	@Test
	public void shouldDeleteReservation() {
		LocalDate date = now();
		Reservation reservation = new Reservation();
		when(reservationService.getReservationFor(anyLong(), anyLong(), any())).thenReturn(Optional.of(reservation));

		ResponseEntity<Reservation> result = reservationController.deleteReservation(1, date, 3);

		verify(reservationService).getReservationFor(anyLong(), anyLong(), any());
		verify(reservationService).deleteReservation(anyLong());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(reservation, result.getBody());
	}

	@Test
	public void shouldNotDeleteReservationBecauseOfReservationNotFound() {
		LocalDate date = now();
		when(reservationService.getReservationFor(anyLong(), anyLong(), any())).thenReturn(Optional.empty());

		ResponseEntity<Reservation> result = reservationController.deleteReservation(1, date, 3);

		verify(reservationService).getReservationFor(anyLong(), anyLong(), any());
		verify(reservationService, never()).deleteReservation(anyLong());
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

}
