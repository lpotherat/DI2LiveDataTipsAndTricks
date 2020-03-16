package org.diiage.lpotherat.poc.livedatatricks;

import androidx.lifecycle.LiveData;

public interface MainRepositoryInterface {
    LiveData<String> getById(Integer id);
}
