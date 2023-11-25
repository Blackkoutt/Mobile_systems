package com.example.sensorapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SensorActivity extends AppCompatActivity {

    public static final String ACCURANCY_TAG = "ACCURANCY";
    public static final String TAG = "LOCATION";
    private static final String KEY_SUBTITLE_VISIBLE = "subtitleVisible";
    public static final String KEY_EXTRA_SENSOR = "sensor_index";
    private boolean subtitleVisible;
    private TextView subtitleTextView;
    private RecyclerView recyclerView;
    private Button subtitleVisibilityButton;
    private SensorAdapter adapter;
    private SensorManager sensorManager;
    private List<Sensor> sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            subtitleVisible = savedInstanceState.getBoolean(KEY_SUBTITLE_VISIBLE);
        }

        setContentView(R.layout.sensor_activity);

        // Pobranie przycisku do wyświetlania podtytułu
        subtitleVisibilityButton = findViewById(R.id.show_hide_subtitle);

        // Pobranie TextView zawierającego podtytuł
        subtitleTextView = findViewById(R.id.subtitle);

        // Pobranie listy recyclerView dla wszystkich sensorów
        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SetSubtitleVisibility(); // Ustawienie widzialności podtytułu

        // Ustawienie onClickListenera dla przycisku "Pokaż/Ukryj podtytuł"
        subtitleVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtitleVisible = !subtitleVisible; // Zmiana widoczności podtytułu
                SetSubtitleVisibility();    // Ustawienie widoczności podtytułu
            }
        });

        // Pobranie serwisu SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Pobranie listy wszytskich sensorów
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // Utworzenie i ustawienie nowego adaptera
        if(adapter == null){
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }
        else{
            adapter.notifyDataSetChanged();
        }

        // Ustawienie podtytułu  - ilość sensorów
        String subtitle = getString(R.string.sensors_count, adapter.getItemCount());
        subtitleTextView.setText(subtitle);
    }

    // Metoda do zapisania stanu aplikacji przy wykonaniu obrotu ekranu - zapisanie stanu widoczności podtytułu
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SUBTITLE_VISIBLE, subtitleVisible);
    }

    // Ustawienie widoczności podtytułu
    private void SetSubtitleVisibility(){
        if(subtitleVisible){
            // Zmiana obrazka w przycisku
            Drawable drawable = ContextCompat.getDrawable(SensorActivity.this, R.drawable.ic_hide_subtitle);
            subtitleVisibilityButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

            subtitleTextView.setVisibility(View.VISIBLE); // Podtytuł jest widoczny
            subtitleVisibilityButton.setText(R.string.hide_subtitle);   // Zmiana tekstu w przycisku
        }
        else{
            // Zmiana obrazka w przycisku
            Drawable drawable = ContextCompat.getDrawable(SensorActivity.this, R.drawable.ic_add_subtitle);
            subtitleVisibilityButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

            subtitleTextView.setVisibility(View.GONE);  // Podytutł nie jest widoczny
            subtitleVisibilityButton.setText(R.string.show_subtitle);   // Zmiena tekstu w przycisku
        }
    }

    private class SensorHolder extends RecyclerView.ViewHolder{

        private ImageView sensorIconImageView;
        private TextView sensorNameTextView;
        private Sensor sensor;
        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));

            // Dla każdego elementu listy RecyclerView ustawiony jest OnLongClickListener
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    // Po naciśnięciu elementu listy wyświetla się alert z szczegółowymi informacjami dotyczącymi sensora
                    String message = String.format("Producent: %s\nMax wartość: %s", sensor.getVendor(), sensor.getMaximumRange());
                    AlertDialog.Builder builder = new AlertDialog.Builder(SensorActivity.this);
                    builder.setTitle(R.string.sensor_details)
                            .setMessage(message);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create();
                    builder.show();
                    return true;
                }
            });

            // Pobranie obrazka oraz nazwy sensora z widoku
            sensorIconImageView = itemView.findViewById(R.id.sensor_icon);
            sensorNameTextView = itemView.findViewById(R.id.sensor_name);
        }

        // bind służy do szczegółowego ustawienia wyglądu i zachowania każdego elementu listy
        void bind (Sensor sensor){
            this.sensor=sensor;

            // Ustawienie nazwy i obrazka dla danego sensora
            sensorIconImageView.setImageResource(R.drawable.ic_sensor);
            sensorNameTextView.setText(sensor.getName());

            // Jeśli jest to sensor typu magnetometr
            if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                // Wyróżnij go na liście
                sensorNameTextView.setTypeface(null, Typeface.BOLD);
                sensorNameTextView.setBackgroundColor(Color.rgb(45, 165, 235));
                sensorNameTextView.setTextColor(Color.WHITE);

                // Dodaj onClickListener - po kliknięciu uruchamiana jest aktywność LocationActivity
                sensorNameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (SensorActivity.this, LocationActivity.class);
                        startActivity(intent);
                    }
                });
            }

            // Jeśli jest to sensor typi akcelerometr lub grawitacyjny
            if(sensor.getType() == Sensor.TYPE_ACCELEROMETER || sensor.getType() == Sensor.TYPE_GRAVITY){
                // Wyróżnienie go na liście
                sensorNameTextView.setTypeface(null, Typeface.BOLD);
                sensorNameTextView.setBackgroundColor(Color.rgb(0, 133, 119));
                sensorNameTextView.setTextColor(Color.WHITE);

                // Ustawienie onClickListenera uruchamiającego nową aktywność Details
                sensorNameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (SensorActivity.this, SensorDetailsActivity.class);
                        // Do aktywności przekazywany jest index danego sensora
                        intent.putExtra(KEY_EXTRA_SENSOR, sensorList.indexOf(sensor));
                        startActivity(intent);
                    }
                });
            }
        }

    }
    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder>{

        private List<Sensor> sensorList;
        public SensorAdapter(List<Sensor> sensorList){
            this.sensorList = sensorList;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SensorActivity.this);
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            holder.bind(sensorList.get(position));
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }
    }
}