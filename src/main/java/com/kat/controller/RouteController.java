package com.kat.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kat.model.Route;
import com.kat.service.RouteService;

@RestController
public class RouteController {
	
	private RouteService routeService;

	@Autowired
	public RouteController(RouteService routeService) {
		this.routeService = routeService;
	}

	@PostConstruct
	public void init() {
		routeService.loadRoutes();
	}

	@RequestMapping(value = "routes", method = RequestMethod.GET)
	public ResponseEntity<List<Route>> getAllRoutes() {
		List<Route> routes = routeService.getAllRoutes();
		if (routes == null || routes.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.ok().body(routes);
		}
	}

	@RequestMapping(value = "routes/{id}", method = RequestMethod.GET)
	public ResponseEntity<Route> getRoute(@PathVariable("id") long id) {
		Optional<Route> routeOptional = routeService.getRoute(id);
		if (!routeOptional.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(routeOptional.get(), HttpStatus.OK);
		}
	}

}
