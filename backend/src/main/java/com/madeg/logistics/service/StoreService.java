package com.madeg.logistics.service;

import com.madeg.logistics.domain.StoreInput;
import com.madeg.logistics.domain.StorePatch;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.enums.StoreType;
import com.madeg.logistics.repository.StoreRepository;
import java.util.List;
import java.util.Objects;
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

  public void patchStore(String code, StorePatch patchInput) {
    Store previousStore = storeRepository.findByStoreCode(code);

    if (previousStore == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "STORE NOT FOUND"
      );
    }

    boolean isUpdated = false;

    if (!Objects.equals(patchInput.getName(), previousStore.getName())) {
      previousStore.updateName(patchInput.getName());
      isUpdated = true;
    }

    if (!Objects.equals(patchInput.getAddress(), previousStore.getAddress())) {
      previousStore.updateAddress(patchInput.getAddress());
      isUpdated = true;
    }

    if (patchInput.getFixedCost() != previousStore.getFixedCost()) {
      previousStore.updateFixedCost(patchInput.getFixedCost());
      isUpdated = true;
    }

    if (patchInput.getCommissionRate() != previousStore.getCommissionRate()) {
      previousStore.updateCommissionRate(patchInput.getCommissionRate());
      isUpdated = true;
    }

    updateStoreType(previousStore);

    if (
      !Objects.equals(
        patchInput.getDescription(),
        previousStore.getDescription()
      )
    ) {
      previousStore.updateDescription(patchInput.getDescription());
      isUpdated = true;
    }

    if (isUpdated) {
      storeRepository.save(previousStore);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NO_CONTENT,
        "STORE IS NOT UPDATED"
      );
    }
  }

  private void updateStoreType(Store store) {
    if (store.getFixedCost() > 0 && store.getCommissionRate() > 0) {
      store.updateType(StoreType.BOTH);
    } else if (store.getFixedCost() == 0 && store.getCommissionRate() > 0) {
      store.updateType(StoreType.RATE);
    } else if (store.getFixedCost() > 0 && store.getCommissionRate() == 0) {
      store.updateType(StoreType.FIX);
    } else {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "AT LEAST ONE OF COMMISSION RATE AND FIXED COST MUST BE GREATER THAN ZERO"
      );
    }
  }

  public void deleteStore(String code) {
    Store previousStore = storeRepository.findByStoreCode(code);

    if (previousStore == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "STORE NOT FOUND"
      );
    }
    storeRepository.delete(previousStore);
  }
}
