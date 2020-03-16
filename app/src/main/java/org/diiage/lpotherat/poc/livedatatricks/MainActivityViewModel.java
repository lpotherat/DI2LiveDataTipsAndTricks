package org.diiage.lpotherat.poc.livedatatricks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {



    MutableLiveData<Integer> value1 = new MutableLiveData<>();
    MutableLiveData<Integer> value2 = new MutableLiveData<>();

    MediatorLiveData<Integer> result = new MediatorLiveData<>();

    LiveData<String> humanReadableResult;

    LiveData<String> welcomeMessage;

    MutableLiveData<Integer> uuserId = new MutableLiveData<>();

    MainRepository mainRepository = new MainRepository();

    public MainActivityViewModel() {

        humanReadableResult = Transformations.map(result,input -> input != null ? "Le résultat est " + input.toString() : "0");

        result.addSource(value1,integer -> {
            if (value2.getValue() != null) {
                result.setValue(integer * value2.getValue());
            }
        });
        result.addSource(value2,integer -> {
            if (value1.getValue() != null) {
                result.setValue(integer * value1.getValue());
            }
        });


        welcomeMessage = Transformations.switchMap(uuserId,input -> {
            if(input != null) {
                return mainRepository.getById(input);
            }
            return new MutableLiveData<String>(){{setValue("");}};
        });


    }

    /* public LiveData<Integer> getResult() {
        return result;
    } */

    public LiveData<String> getHumanReadableResult() {
        return humanReadableResult;
    }

    public MutableLiveData<Integer> getValue1() {
        return value1;
    }

    public MutableLiveData<Integer> getValue2() {
        return value2;
    }

    public LiveData<String> getWelcomeMessage() {
        return welcomeMessage;
    }

    public MutableLiveData<Integer> getUuserId() {
        return uuserId;
    }
}
