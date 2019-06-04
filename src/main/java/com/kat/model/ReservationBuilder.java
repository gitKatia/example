package com.kat.model;

import java.time.LocalDate;

public class ReservationBuilder {

	private long id;
	private String childname;
	private Stop stop;
	private Route route;
	private LocalDate date;

	public static ReservationBuilder reservationBuilder() {
		return new ReservationBuilder();
	}

	public Reservation build() {
		Reservation reservation = new Reservation();
		reservation.setId(id);
		reservation.setChildname(childname);
		reservation.setRoute(route);
		reservation.setStop(stop);
		reservation.setDate(date);
		return reservation;
	}
	
	public ReservationBuilder id(long id) {
		this.id = id;
		return this;
	}
	
	public ReservationBuilder childname(String childname) {
		this.childname = childname;
		return this;
	}
	
	public ReservationBuilder stop(Stop stop) {
		this.stop = stop;
		return this;
	}
	
	public ReservationBuilder route(Route route) {
		this.route = route;
		return this;
	}
	
	public ReservationBuilder date(LocalDate date) {
		this.date = date;
		return this;
	}

}
