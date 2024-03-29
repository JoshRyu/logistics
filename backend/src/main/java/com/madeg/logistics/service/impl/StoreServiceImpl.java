package com.madeg.logistics.service.impl;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.madeg.logistics.domain.StoreInput;
import com.madeg.logistics.domain.StorePatch;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.enums.StoreType;
import com.madeg.logistics.repository.StoreRepository;
import com.madeg.logistics.service.StoreService;
import com.madeg.logistics.util.CommonUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreServiceImpl implements StoreService {

    private StoreRepository storeRepository;
    private CommonUtil commonUtil;

    public StoreServiceImpl(StoreRepository storeRepository, CommonUtil commonUtil) {
        this.storeRepository = storeRepository;
        this.commonUtil = commonUtil;
    }

    @Override
    @Transactional
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

    @Override
    public List<Store> getStores() {
        return storeRepository.findAll();
    }

    @Override
    @Transactional
    public void patchStore(String storeCode, StorePatch patchInput) {
        Store previousStore = commonUtil.findStoreByCode(storeCode);

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
                    ResponseCode.BAD_REQUEST.getStatus(),
                    ResponseCode.BAD_REQUEST.getMessage("수수료과 매대비(고정비) 중 적어도 하나는 0보다 커야 합니다"));
        }
    }

    @Override
    @Transactional
    public void deleteStore(String storeCode) {
        Store previousStore = commonUtil.findStoreByCode(storeCode);
        storeRepository.delete(previousStore);
    }
}
