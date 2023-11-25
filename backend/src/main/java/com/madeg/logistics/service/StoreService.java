package com.madeg.logistics.service;

import com.madeg.logistics.domain.StoreInput;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.repository.StoreRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StoreService {

  @Autowired
  private StoreRepository storeRepository;

  public void createStore(StoreInput storeInput) {
    Store existStore = storeRepository.findByName(storeInput.getName());

    if (existStore != null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        String.format("STORE %s ALREADY EXIST", storeInput.getName())
      );
    }

    Store store = Store
      .builder()
      .name(storeInput.getName())
      .address(storeInput.getAddress())
      .type(storeInput.getType())
      .fixedCost(storeInput.getFixedCost())
      .commissionRate(storeInput.getCommissionRate())
      .description(storeInput.getDescription())
      .build();

    storeRepository.save(store);
  }

  public List<Store> getStores() {
    return storeRepository.findAll();
  }
}
