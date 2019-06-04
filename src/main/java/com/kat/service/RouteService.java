package com.kat.service;

import java.util.List;
import java.util.Optional;

import com.kat.model.Route;

public interface RouteService {

	Route save(Route route);

	void loadRoutes();
	
	List<Route> getAllRoutes();
	
	Optional<Route> getRoute(long id);

}
