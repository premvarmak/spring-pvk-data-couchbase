package com.nephos.cb.dao;

import com.nephos.cb.model.GamesSample;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesSampleRepository extends CrudRepository<GamesSample, String> {


}
