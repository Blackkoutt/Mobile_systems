package com.example.zadanie3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    // Metoda onCreate analogiczna dla obu Acitvity - tworzone są jedynie inne obiekty fragmentów
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // W layoucie activity_main znajduje się kontener na fragmenty z którego korzystają obie aktywności
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Powiązanie fragmentu z jego kontenerem (w activity_main.xml)
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = createFragment(); // Tworzenie fragmentu w zależności od activity

            // Transakcja dodająca fragment do listy
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
    protected abstract Fragment createFragment(); // Abstrakcyjna metoda którą implementują obie Aktywności
}
