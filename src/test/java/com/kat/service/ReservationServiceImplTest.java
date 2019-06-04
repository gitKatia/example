package com.kat.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kat.model.Reservation;
import com.kat.repository.ReservationRepository;

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
