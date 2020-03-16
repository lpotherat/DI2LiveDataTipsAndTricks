package org.diiage.lpotherat.poc.livedatatricks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import org.diiage.lpotherat.poc.livedatatricks.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    /**
     * On héberge ici une instance de notre viewmodel
     */
    MainActivityViewModel mainActivityViewModel;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Je charge ici le binding pour l'activité principale.
        // - Notez que cette méthode de récupération du binding n'est appliquable qu'aux activités,
        //   et pas aux fragments
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        //On affecte le livecycleOwner au binding, dans ce cas c'est l'activité, dans le cas d'un
        //fragment, il faut plutôt le récupérer via getViewLifecycleOwner().
        binding.setLifecycleOwner(this);


        // Demande de crétion / récupération d'un viewmodel
        // - On utilise ici le deuxième paramètre pour fournir la Factory au ViewModelProvider
        MainActivityViewModel.MainActivityViewModelFactory mainActivityViewModelFactory =
                new MainActivityViewModel.MainActivityViewModelFactory(new MainRepository());
        mainActivityViewModel = new ViewModelProvider(this,mainActivityViewModelFactory)
                .get(MainActivityViewModel.class);

        //J'affecte ici le viewmodel au binding pour lui donner accès aux contenus
        binding.setViewModel(mainActivityViewModel);


        //On déclare ensuite nos observers du côté de la vue.
        // Note: Lorsque l'on utilise le databinding, il n'est pas nécessaire d'observer
        // les LiveData depuis le Fragment ou l'activité, le système de Databinding le fait tout seul.

        //On observe le résultat de l'opération.
        // - Notez que le simple fait d'observer le résultat, déclenche toute la chaîne d'observation
        // qui est déclarée dans le viewmodel. Par effet domino, les LiveData value1 et value2 sont donc
        // bien observés
        mainActivityViewModel.getHumanReadableResult().observe(this, string -> {
            if (string != null) {
                Log.d("DEBUG", string);
            }
        });
        // Démo : On affecte les valeurs 1 et 2
        mainActivityViewModel.getValue1().setValue(2);
        mainActivityViewModel.getValue2().setValue(5);


        // J'affecte ici la valeur de l'identifiant d'utilisateur que je veux voir affiché.
        mainActivityViewModel.getUuserId().setValue(1);
        // La valeur qui sera affichée est directemennt observée dans le binding



    }
}
