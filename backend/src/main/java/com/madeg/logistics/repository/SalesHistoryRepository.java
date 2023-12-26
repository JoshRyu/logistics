package com.madeg.logistics.repository;

import com.madeg.logistics.entity.SalesHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesHistoryRepository
  extends CrudRepository<SalesHistory, Long> {}
