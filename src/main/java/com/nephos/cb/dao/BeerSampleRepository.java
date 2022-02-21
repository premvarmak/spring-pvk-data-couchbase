package com.nephos.cb.dao;

import com.nephos.cb.model.BeerSample;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerSampleRepository extends CrudRepository<BeerSample, String> {

}
