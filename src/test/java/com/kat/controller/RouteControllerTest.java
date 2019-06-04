package com.kat.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kat.model.Route;
import com.kat.service.RouteService;

@RunWith(MockitoJUnitRunner.class)
public class RouteControllerTest {

	@Mock
	private RouteService routeService;

	@InjectMocks
	private RouteController routeController;

	@Test
	public void shouldLoadRoutes() {
		routeController.init();
		verify(routeService).loadRoutes();
	}

	@Test
	public void shouldGetAllRoutes() {
		List<Route> routes = new ArrayList<>();
		Route route1 = new Route();
		Route route2 = new Route();
		routes.add(route1);
		routes.add(route2);
		when(routeService.getAllRoutes()).thenReturn(routes);
		ResponseEntity<List<Route>> result = routeController.getAllRoutes();

		verify(routeService).getAllRoutes();
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(routes, result.getBody());
	}

	@Test
	public void shouldReturnNoRoutes() {
		List<Route> routes = new ArrayList<>();
		when(routeService.getAllRoutes()).thenReturn(routes);
		ResponseEntity<List<Route>> result = routeController.getAllRoutes();

		verify(routeService).getAllRoutes();
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
		assertNull(result.getBody());
	}

	@Test
	public void shouldGetTheRouteGivenTheId() {
		Route route = new Route();
		when(routeService.getRoute(anyLong())).thenReturn(Optional.of(route));
		ResponseEntity<Route> result = routeController.getRoute(1);

		verify(routeService).getRoute(anyLong());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(route, result.getBody());
	}

	@Test
	public void shouldReturnNoRouteForTheGivenId() {
		when(routeService.getRoute(anyLong())).thenReturn(Optional.empty());
		ResponseEntity<Route> result = routeController.getRoute(1);

		verify(routeService).getRoute(anyLong());
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

}
