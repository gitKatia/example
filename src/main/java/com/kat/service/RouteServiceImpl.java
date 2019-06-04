package com.kat.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kat.controller.RouteController;
import com.kat.model.Route;
import com.kat.repository.RouteRepository;

@Service
public class RouteServiceImpl implements RouteService {
	
	private static final Logger logger = LoggerFactory.getLogger(RouteController.class);

	private RouteRepository routeRepository;

	@Autowired
	public RouteServiceImpl(RouteRepository routeRepository) {
		this.routeRepository = routeRepository;
	}

	@Override
	public Route save(Route route) {
		return routeRepository.save(route);
	}

	@Override
	public void loadRoutes() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		TypeReference<List<Route>> typeReference = new TypeReference<List<Route>>() {
		};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/routes.json");
		try {
			List<Route> routes = mapper.readValue(inputStream, typeReference);
			routeRepository.saveAll(routes);
			logger.info("Routes Saved!");
		} catch (IOException e) {
			logger.error("Unable to save routes: {} ", e.getMessage());
		}
	}

	@Override
	public List<Route> getAllRoutes() {
		return routeRepository.findAll();
	}

	@Override
	public Optional<Route> getRoute(long id) {
		return routeRepository.findById(id);
	}

}
