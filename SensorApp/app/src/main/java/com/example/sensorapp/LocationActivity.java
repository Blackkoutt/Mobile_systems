package com.example.sensorapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocationActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 123;
    private Location lastLocation;
    private TextView addressTextView;
    private TextView locationTextView;
    private TextView localizationInfoTextView;
    private Button addressButton;
    private FusedLocationProviderClient fusedLocationClient;
    private Button getLocalizationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Pobranie TextView zawierającego informację o przyciskach
        localizationInfoTextView = findViewById(R.id.get_localization_info);

        // Pobranie TextView zawierającego lokalizację
        locationTextView = findViewById(R.id.textview_location);

        // Pobranie TextView zawierającego adres
        addressTextView = findViewById(R.id.textview_address);

        // Pobranie przycisków "Pobierz lokalizację" oraz "Pobierz adres"
        getLocalizationButton = findViewById(R.id.get_localization_button);
        addressButton = findViewById(R.id.get_address_button);
        addressButton.setEnabled(false);

        // Uzyskanie dostępu do usługi lokalizacji
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Na początek wyświetla się tylko informacja o przyciskach
        locationTextView.setVisibility(View.GONE);
        addressTextView.setVisibility(View.GONE);


        // Ustawienie onClickListenera dla przycisku "Pobierz adres"
        addressButton.setOnClickListener(v -> {
            localizationInfoTextView.setVisibility(View.GONE);
            addressTextView.setVisibility(View.VISIBLE);
            executeGeocoding(); // Uruchomienie geokodowania odwrotnego
        });

        // Ustawienie onClickListenera dla przycisku "Pobierz lokalizację"
        getLocalizationButton.setOnClickListener( v -> {
            localizationInfoTextView.setVisibility(View.GONE);
            locationTextView.setVisibility(View.VISIBLE);
            addressButton.setEnabled(true);
            getLocation(); // Pobranie lokalizacji
        });

    }

    // Metoda pobierająca i wyświetlająca lokalizację
    // Wymaga udzielenia aplikacji dostępu do użycia lokalizacji
    private void getLocation() {
        // Jeśli aplikacja nie ma uprawnień powinna się o nie upomnieć
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        // W przeciwnym wypadku aplikacja posiada już uprawnienia i może pobrać lokalizację urządzenia
        else{
            fusedLocationClient.getLastLocation().addOnSuccessListener(
                location->{
                    if(location!=null){
                        lastLocation = location;
                        locationTextView.setText(
                                getString(R.string.location_text, location.getLatitude(), location.getLongitude(), location.getTime()));
                    }
                    else{
                        locationTextView.setText(R.string.no_location);
                    }
                });
        }
    }


    // Metoda wywoływana gdy aplikacja nie posiada odpowiednich uprawnień dostępu do lokalizacji
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_LOCATION_PERMISSION:
            {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }
                else{
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // Metoda odpowiedzialna za wykonanie odwrotnego geokodowania (zamiana wspołrzędnych na adres)
    private String locationGeocoding(Context context, Location location){

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        String resultMessage ="";

        try{
            // Uruchomienie procesu geokodowania - geokodowanie zwraca listę adresów
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        }
        // Obsługa wyjątku związanego z serwisem Geocoder
        catch(IOException ioException){
            resultMessage=context.getString(R.string.service_not_available);
            Log.e(TAG, resultMessage, ioException);
        }
        // Jeśli lista jest pusta zwróć komunikat o braku adresu
        if(addresses == null || addresses.isEmpty()){
            if(resultMessage.isEmpty()){
                resultMessage=context.getString(R.string.no_adress_found);
                Log.e(TAG,resultMessage);
            }
        }
        else{
            Address address = addresses.get(0);
            List<String> addressParts = new ArrayList<>();

            // Wszystkie linie adresu są łączone w jeden łańcuch znakowy
            for(int i=0;i<=address.getMaxAddressLineIndex();i++){
                addressParts.add(address.getAddressLine(i));
            }
            resultMessage = TextUtils.join("\n", addressParts);
        }
        return resultMessage; // zwrócenie wynikowego adresu
    }


    // Metoda wywołuje geokodowanie w odzielnym wątku
    private void executeGeocoding(){
        if(lastLocation !=null){
            // Uruchomienie nowego wątku
            ExecutorService executor = Executors.newSingleThreadExecutor();

            // Zwrócenie wartości poprzez interfejs Future
            Future<String> returnedAddress = executor.submit(()->locationGeocoding(getApplicationContext(), lastLocation));
            try{
                String result = returnedAddress.get();
                addressTextView.setText(getString(R.string.address_text, result, System.currentTimeMillis()));
            }
            catch(ExecutionException|InterruptedException e){
                Log.e(TAG, e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }


}