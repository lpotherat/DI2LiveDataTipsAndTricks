package org.diiage.lpotherat.poc.livedatatricks;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public interface MainRepositoryInterface {
    LiveData<String> getById(Integer id);
    LiveData<String> getByNullableId(@Nullable Integer id);
}
