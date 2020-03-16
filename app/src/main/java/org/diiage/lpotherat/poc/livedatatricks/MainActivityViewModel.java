package org.diiage.lpotherat.poc.livedatatricks;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Le viewmodel de l'activité principale
 */
public class MainActivityViewModel extends ViewModel {

    /**
     * Déclaration d'une Factory pour permettre de créer ce viewModel avec la dépendance au repository
     */
    public static class MainActivityViewModelFactory implements ViewModelProvider.Factory {

        MainRepositoryInterface mainRepository;

        public MainActivityViewModelFactory(MainRepositoryInterface mainRepository) {
            this.mainRepository = mainRepository;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainActivityViewModel(mainRepository);
        }
    }

    // -------------------------------------------------------
    // Démo simple des différents types de livedata
    // - LiveData, permet de fournir une information observable en lecture seule
    LiveData<String> humanReadableResult;
    // - MutableLiveData, permet de fournir une information observable en lecture / écriture
    MutableLiveData<Integer> value1 = new MutableLiveData<>();
    MutableLiveData<Integer> value2 = new MutableLiveData<>();
    // - MediatorLiveData, ce LiveData particulier peut s'abonner à plusieurs LiveData.
    // Dans notre cas, il sera abonné à value1 et value2 pour ensuite héberger le résultat de l'opération
    MediatorLiveData<Integer> result = new MediatorLiveData<>();
    // -------------------------------------------------------


    // -------------------------------------------------------
    // Démo de l'usage avec un repository, et notamment Transformations.switchMap
    LiveData<String> welcomeMessage;
    MutableLiveData<Integer> uuserId = new MutableLiveData<>();
    MainRepositoryInterface mainRepository;
    // -------------------------------------------------------

    /**
     * constructeur paramétré du viewmodel, on y a ajouté une dépendance au Repository
     * @param mainRepository
     */
    public MainActivityViewModel(MainRepositoryInterface mainRepository) {
        this.mainRepository = mainRepository;

        // ----------------------------------------
        // Démo des types de LiveData + Transmations.map

        // On paramètre ici result pour l'abonner aux modifications de value1
        result.addSource(value1,integer -> {
            if (value2.getValue() != null) {
                result.setValue(integer * value2.getValue());
            }
        });
        // On paramètre ici result pour l'abonner aux modifications de value2
        result.addSource(value2,integer -> {
            if (value1.getValue() != null) {
                result.setValue(integer * value1.getValue());
            }
        });

        humanReadableResult = Transformations.map(result,input -> input != null ? "Le résultat est " + input.toString() : "0");
        // ----------------------------------------



        welcomeMessage = Transformations.switchMap(uuserId,input -> {
            if(input != null) {
                return mainRepository.getById(input);
            }
            return new MutableLiveData<String>(){{setValue("");}};
        });


    }

    public MutableLiveData<Integer> getValue1() {
        return value1;
    }

    public MutableLiveData<Integer> getValue2() {
        return value2;
    }

    public LiveData<String> getHumanReadableResult() {
        return humanReadableResult;
    }

    public LiveData<String> getWelcomeMessage() {
        return welcomeMessage;
    }

    public MutableLiveData<Integer> getUuserId() {
        return uuserId;
    }
}
