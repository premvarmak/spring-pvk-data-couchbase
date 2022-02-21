package com.nephos.cb.dao;

import com.nephos.cb.model.TravelSample;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelSampleRepository extends CrudRepository<TravelSample, String> {
}
