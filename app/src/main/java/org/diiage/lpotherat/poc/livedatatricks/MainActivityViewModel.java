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
     *
     * La factory permet au système de créer une instance de ce ViewModel avec un paramètre dans le constructeur
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
        // Démo des types de LiveData + Transformations.map

        // Dans le cas d'un MediatorLiveData, les observer reçoivent la valeur observée, et doivent
        // affecter la valeur correspondante directement avec setValue.

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
        // On utilise ici la transformation "map" pour convertir le résultat Integer en String.
        // - map retourne un LiveData, qui est une image du LiveData source (result ici)
        // - l'observer doit retourner la nouvelle valeur à appliquer lors de la modification de la valeur d'origine
        humanReadableResult = Transformations.map(result,input -> input != null ? "Le résultat est " + input.toString() : "0");
        // ----------------------------------------

        // ------------------------------------------------------------
        // Démonstration de l'usage de switchMap
        // Cas d'usage : Nous avons l'identifiant d'un utilisateur, et souhaitons afficher son nom
        // - Dans notre repository, la méthode getById retourne un LiveData observable
        // - l'identifiant de l'utilisateur est dans un LiveData uuserId.
        // Ici, switchMap va permettre de créer un LiveData (welcomeMessage) qui va, en fonction de
        // la valeur de uuserId, prendre la valeur de getById . Le nouveau LiveData créé suit les
        // modification internes de LiveData, et peut donc être observé.
        welcomeMessage = Transformations.switchMap(uuserId,input -> {
            if(input != null) {
                return mainRepository.getById(input);
            }
            return null;
        });
        // Notez que si la méthode getById accepte les valeurs nulles, il est possible d'écrire
        // le code ci dessus de manière plus compacte, comme ci dessous.
        //welcomeMessage = Transformations.switchMap(uuserId,mainRepository::getByNullableId);

    }

    /**
     * Getter de la valeur 1, on retourne ici une version MutableLiveData, car cette valeur
     * doit pouvoir être modifiable depuis l'extérieur
     * @return
     */
    public MutableLiveData<Integer> getValue1() {
        return value1;
    }

    /**
     * Getter de la valeur 2, on retourne ici une version MutableLiveData, car cette valeur
     * doit pouvoir être modifiable depuis l'extérieur
     * @return
     */
    public MutableLiveData<Integer> getValue2() {
        return value2;
    }

    /**
     * Getter de la valeur 1, on retourne ici une version LiveData, en lecture seule, car cette valeur
     * ne doit pas pouvoir être modifiable depuis l'extérieur
     * @return
     */
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
