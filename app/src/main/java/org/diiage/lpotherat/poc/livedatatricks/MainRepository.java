package org.diiage.lpotherat.poc.livedatatricks;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Repository d'exemple,
 *
 * Les données sont stockées dans une Hashmap, la tâche lente est simulée avec thread.sleep.
 */
public class MainRepository {

    // Stockage local
    private HashMap<Integer,String> noms = new HashMap<>();

    /**
     * On peut garder une référence à un executor ici pour éviter d'avoir à créer un nouveau à chaque fois
     */
    private Executor executor = Executors.newFixedThreadPool(5);


    public MainRepository() {
        this.noms.put(1,"Léonard");
        this.noms.put(2,"Mathieu");
    }

    /**
     * 
     * @param id
     * @return
     */
    public LiveData<String> getById(Integer id){
        MutableLiveData<String> ret = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ret.postValue(this.noms.get(id));
        });

        return ret;
    }
}
