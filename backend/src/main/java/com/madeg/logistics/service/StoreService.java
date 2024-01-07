package com.madeg.logistics.service;

import com.madeg.logistics.domain.StoreInput;
import com.madeg.logistics.domain.StorePatch;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.enums.StoreType;
import com.madeg.logistics.repository.StoreRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StoreService extends CommonService {

  @Autowired
  private StoreRepository storeRepository;

  public void createStore(StoreInput storeInput) {
    Store existStore = storeRepository.findByName(storeInput.getName());

    if (existStore != null) {
      throw new ResponseStatusException(
          ResponseCode.CONFLICT.getStatus(),
          ResponseCode.CONFLICT.getMessage("매장"));

    }

    Store store = Store
        .builder()
        .name(storeInput.getName())
        .address(storeInput.getAddress())
        .fixedCost(storeInput.getFixedCost())
        .commissionRate(storeInput.getCommissionRate())
        .description(storeInput.getDescription())
        .build();

    updateStoreType(store);

    storeRepository.save(store);
  }

  public List<Store> getStores() {
    return storeRepository.findAll();
  }

  public void patchStore(String storeCode, StorePatch patchInput) {
    Store previousStore = findStoreByCode(storeCode);

    if (previousStore.isStateChanged(patchInput)) {
      if (patchInput.getName() != null)
        previousStore.updateName(
            patchInput.getName());
      if (patchInput.getAddress() != null)
        previousStore.updateAddress(
            patchInput.getAddress());
      if (patchInput.getFixedCost() != null)
        previousStore.updateFixedCost(
            patchInput.getFixedCost());
      if (patchInput.getCommissionRate() != null)
        previousStore.updateCommissionRate(patchInput.getCommissionRate());
      if (patchInput.getDescription() != null)
        previousStore.updateDescription(
            patchInput.getDescription());

      updateStoreType(previousStore);

      storeRepository.save(previousStore);
    } else {
      throw new ResponseStatusException(
          ResponseCode.UNCHANGED.getStatus(),
          ResponseCode.UNCHANGED.getMessage("매장"));
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
          ResponseCode.BADREQUEST.getStatus(),
          ResponseCode.BADREQUEST.getMessage("수수료과 매대비(고정비) 중 적어도 하나는 0보다 커야 합니다"));
    }
  }

  public void deleteStore(String storeCode) {
    Store previousStore = findStoreByCode(storeCode);
    storeRepository.delete(previousStore);
  }
}
