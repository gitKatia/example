package com.kat.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kat.model.Stop;
import com.kat.repository.StopRepository;

@Service
public class StopServiceImpl implements StopService {
	
	private StopRepository stopRepository;
	
	@Autowired
	public StopServiceImpl(StopRepository stopRepository) {
		this.stopRepository = stopRepository;
	}

	@Override
	public Optional<Stop> getStop(long id) {
		return stopRepository.findById(id);
	}

}
