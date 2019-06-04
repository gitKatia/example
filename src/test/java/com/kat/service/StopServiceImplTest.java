package com.kat.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kat.model.Stop;
import com.kat.repository.StopRepository;

@RunWith(MockitoJUnitRunner.class)
public class StopServiceImplTest {

	@Mock
	private StopRepository stopRepository;

	@InjectMocks
	private StopServiceImpl stopService;

	@Test
	public void shouldRetrieveTheStopGivenTheId() {

		Stop stop = new Stop();
		Optional<Stop> stopOpt = Optional.of(stop);
		when(stopRepository.findById(anyLong())).thenReturn(stopOpt);
		stopService.getStop(1);
		verify(stopRepository).findById(anyLong());
	}

}
