package com.solar.pinterest.solarmobile.storage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public abstract class SolarRepoAbstract {
    protected <T> LiveData<StatusEntity> put(PinRepo.EnsureOperator lambda) {
        MutableLiveData<StatusEntity> status = new MutableLiveData<>(new StatusEntity(StatusEntity.Status.IN_PROGRESS));
        lambda.call(status);
        return status;
    }

    interface EnsureOperator {
        void call(MutableLiveData<StatusEntity> status);
    }
}
