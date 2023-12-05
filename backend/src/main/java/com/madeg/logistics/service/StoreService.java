package com.madeg.logistics.service;

import com.madeg.logistics.domain.StoreInput;
import com.madeg.logistics.domain.StorePatch;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.enums.StoreType;
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

  public void patchStore(String code, StorePatch patchInput) {
    Store previousStore = storeRepository.findByStoreCode(code);

    if (previousStore == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "STORE NOT FOUND"
      );
    }

    boolean isUpdated = false;

    if (!patchInput.getName().equals(previousStore.getName())) {
      previousStore.updateName(patchInput.getName());
      isUpdated = true;
    }

    if (patchInput.getAddress() != null) {
      if (!patchInput.getAddress().equals(previousStore.getAddress())) {
        previousStore.updateAddress(patchInput.getAddress());
        isUpdated = true;
      }
    } else {
      if (previousStore.getAddress() != null) {
        previousStore.updateAddress(null);
        isUpdated = true;
      }
    }

    boolean isFixedCostUpdated = false;
    boolean isCommissionRateUpdated = false;

    if (patchInput.getFixedCost() != previousStore.getFixedCost()) {
      previousStore.updateFixedCost(patchInput.getFixedCost());
      isUpdated = true;
      isFixedCostUpdated = true;
    }

    if (patchInput.getCommissionRate() != previousStore.getCommissionRate()) {
      previousStore.updateComissionRate(patchInput.getCommissionRate());
      isUpdated = true;
      isCommissionRateUpdated = true;
    }

    if (isFixedCostUpdated || isCommissionRateUpdated) {
      if (
        previousStore.getFixedCost() > 0 &&
        previousStore.getCommissionRate() > 0
      ) {
        previousStore.updateType(StoreType.BOTH);
      } else if (
        previousStore.getFixedCost() == 0 &&
        previousStore.getCommissionRate() > 0
      ) {
        previousStore.updateType(StoreType.RATE);
      } else if (
        previousStore.getFixedCost() > 0 &&
        previousStore.getCommissionRate() == 0
      ) {
        previousStore.updateType(StoreType.FIX);
      } else {
        throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "AT LEAST ONE OF COMISSION RATE AND FIXED COST MUST BE GREATER THAN ZERO"
        );
      }
    }

    if (patchInput.getDescription() != null) {
      if (!patchInput.getDescription().equals(previousStore.getDescription())) {
        previousStore.updateDescription(patchInput.getDescription());
        isUpdated = true;
      }
    } else {
      if (previousStore.getDescription() != null) {
        previousStore.updateDescription(null);
        isUpdated = true;
      }
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
