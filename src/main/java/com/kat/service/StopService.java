package com.kat.service;

import java.util.Optional;

import com.kat.model.Stop;

public interface StopService {
	
	Optional<Stop> getStop(long id);

}
