package com.kat.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Route {

	@Id
	@GeneratedValue
	private long id;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "stop_id")
	@OrderBy("pickupTime ASC")
	private Set<Stop> stops = new HashSet<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<Stop> getStops() {
		return stops;
	}

	public void setStops(Set<Stop> stops) {
		this.stops = stops;
	}

}
