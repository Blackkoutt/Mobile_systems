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

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private static final String KEY_SUBTITLE_VISIBLE = "subtitleVisible";
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
        subtitleVisibilityButton = findViewById(R.id.show_hide_subtitle);
        subtitleTextView = findViewById(R.id.subtitle);

        // Pobranie listy wszystkich sensorów
        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SetSubtitleVisibility();
        subtitleVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtitleVisible = !subtitleVisible; // Zmiana widoczności podtytułu
                SetSubtitleVisibility();    // Ustawienie widoczności podtytułu
            }
        });


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if(adapter == null){
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }
        else{
            adapter.notifyDataSetChanged();
        }
        String subtitle = getString(R.string.sensors_count, adapter.getItemCount());
        subtitleTextView.setText(subtitle);
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SUBTITLE_VISIBLE, subtitleVisible);
    }

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

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    String message = String.format("Producent: %s\nMax wartość: %s", sensor.getVendor(), sensor.getMaximumRange());
                    AlertDialog.Builder builder = new AlertDialog.Builder(SensorActivity.this);
                    builder.setTitle(R.string.sensor_details)
                            .setMessage(message);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // something
                        }
                    });
                    builder.create();
                    builder.show();
                    return true;
                }
            });

            sensorIconImageView = itemView.findViewById(R.id.sensor_icon);
            sensorNameTextView = itemView.findViewById(R.id.sensor_name);
        }
        void bind (Sensor sensor){
            this.sensor=sensor;
            sensorIconImageView.setImageResource(R.drawable.ic_sensor);
            sensorNameTextView.setText(sensor.getName());
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