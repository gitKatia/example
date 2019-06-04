package com.kat.service;

import static org.mockito.ArgumentMatchers.any;
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

import com.kat.model.Route;
import com.kat.repository.RouteRepository;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceImplTest {

	@Mock
	private RouteRepository routeRepository;

	@InjectMocks
	private RouteServiceImpl routeService;

	@Test
	public void shouldRetrieveTheRouteGivenTheId() {

		Route route = new Route();
		Optional<Route> routeOpt = Optional.of(route);
		when(routeRepository.findById(anyLong())).thenReturn(routeOpt);
		routeService.getRoute(1);
		verify(routeRepository).findById(1L);
	}

	@Test
	public void shouldRetrieveAllRoutes() {

		when(routeRepository.findAll()).thenReturn(new ArrayList<>());
		routeService.getAllRoutes();
		verify(routeRepository).findAll();
	}

	@Test
	public void shouldSaveTheRoute() {

		Route route = new Route();
		when(routeRepository.save(any())).thenReturn(route);
		routeService.save(route);
		verify(routeRepository).save(route);
	}

	@Test
	public void shouldSaveAllTheRoutes() {

		List<Route> routes = new ArrayList<>();
		when(routeRepository.saveAll(any())).thenReturn(routes);
		routeService.loadRoutes();
		verify(routeRepository).saveAll(any());
	}

}
