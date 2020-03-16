package org.diiage.lpotherat.poc.livedatatricks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainActivityViewModel = new ViewModelProvider(this,
                new MainActivityViewModel.MainActivityViewModelFactory(new MainRepository()))
                .get(MainActivityViewModel.class);

        mainActivityViewModel.getHumanReadableResult().observe(this, string -> {
            if (string != null) {
                Log.d("DEBUG", string);
            }
        });


        mainActivityViewModel.getWelcomeMessage().observe(this,s -> {
            if (s != null) {
                Log.d("DEBUG", s);
            }
        });

        mainActivityViewModel.getUuserId().setValue(1);
        mainActivityViewModel.getUuserId().setValue(2);



    }
}
