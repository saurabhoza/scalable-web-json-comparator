package com.assingment.scalableweb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.assingment.scalableweb.domainobject.JsonDataDO;

@Repository
public interface JsonDataRepository extends CrudRepository<JsonDataDO, Long>  {


}