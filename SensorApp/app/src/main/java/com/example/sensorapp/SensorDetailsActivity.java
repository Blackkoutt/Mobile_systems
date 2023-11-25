package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        // Pobranie SensorMagagera
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Pobranie TextView dla odczytów z sensora
        sensorTextView = findViewById(R.id.sensor_label);

        // Pobranie przekazanego parametru - indeksu sensora na liście
        int sensor_id = getIntent().getIntExtra(SensorActivity.KEY_EXTRA_SENSOR, -1);

        // Pobranie sensora o danym id z listy sensorów
        sensor = sensorManager.getSensorList(Sensor.TYPE_ALL).get(sensor_id);

        // Jeśli nie ma takiego sensora wyświetl stosowny komunikat
       if (sensor==null){
            sensorTextView.setText(R.string.missing_sensor);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Dlaczego rejestracja działania sensora nie powinna odbywać się w metodzie onCreate ?
        // Ponieważ w ten sposób zużywane jest mniej zasobów systemowych - sensor działa tylko wtedy gdy aktywność jest widoczna dla użytkownika
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Wstrzymuje działanie czujnika gdy aplikacja nie jest używana
    // W przeciwnym wypadku zużycie baterii może wzrosnąc
    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    // Metoda wywoływana w momencie zmiany wartości odczytanych z sensora
    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float[] currentValues = event.values; // wartości odczytane z sensora

        switch(sensorType){
            case Sensor.TYPE_GRAVITY:{
                sensorTextView.setText(getResources().getString(R.string.light_sensor_label,
                        currentValues[0], // oś X
                        currentValues[1], // oś Y
                        currentValues[2])); // oś Z
                break;
            }
            case Sensor.TYPE_ACCELEROMETER:{
                sensorTextView.setText(getResources().getString(R.string.accelerometr_sensor_label,
                        currentValues[0], // oś X
                        currentValues[1], // oś Y
                        currentValues[2] // oś Z
                        ));
                break;
            }
            default:{}
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Wywoływana gdy dokładność sensora ulega zmianie
        // Reaguje na zmiany dokładności sensora w czasie rzeczywistym

        // Metoda wywołuje się przy uruchomieniu aktywności i po obrocie telefonu
        Log.d(SensorActivity.ACCURANCY_TAG, "Wywołano metode on AccurancyChanged(). Wartość accurancy: "+accuracy);
    }
}