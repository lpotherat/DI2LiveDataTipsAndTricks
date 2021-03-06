package org.diiage.lpotherat.poc.livedatatricks;

import android.util.Log;

import androidx.annotation.Nullable;
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
public class MainRepository implements MainRepositoryInterface{

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
     * Retourne un nom à partir de son id.
     *
     *  => Cette méthode est un exemple d'implémentation de repository utilisant Livedata pour le retour de données.
     *  Notez qu'un nouveau LiveData est créé à chaque appel, en effet, le LiveData retourné est lié à l'identifiant
     *  passé en paramètre.
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

    /**
     * Alternative à getById acceptant les valeurs nulles
     * @param id
     * @return
     */
    @Override
    public LiveData<String> getByNullableId(@Nullable Integer id) {
        return id != null ? getById(id) : null;
    }
}
