package com.example.zadanie3;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskFragment extends Fragment {

    private Task task;

    // Deklaracja elementów widoku
    private EditText nameField;
    private EditText dateField;
    private Button saveButton;
    private Spinner categorySpinner;
    private CheckBox doneCheckBox;
    private TextView overdueTasksTextView;
    private Button subtitleVisibilityButton;
    private Button addTaskButton;
    private final Calendar calendar = Calendar.getInstance();
    public static final String ARG_TASK_ID = "ARG_TASK_ID"; // Klucz dodstępowy do parametru przekazywanego innej aktywności

    public TaskFragment(){

    }

    // Metoda dostępowa do obieku fragmentu zadania
    public static TaskFragment newInstance(UUID taskId){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TASK_ID , taskId);
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setArguments(bundle);
        return taskFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID); // Pobranie id zadania otrzymanego jako parametr
        task = TaskStorage.getInstance().getTask(taskId);   // Pobranie zadania i zapisanie go w prywatnej zmiennej
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container,false); // Przypisanie widoku fragmentu

        // Pobranie elementów widoku nadrzędnego Activity
        View rootView = getActivity().getWindow().getDecorView().getRootView();
        overdueTasksTextView = rootView.findViewById(R.id.overdue_tasks);
        subtitleVisibilityButton = rootView.findViewById(R.id.show_hide_subtitle);
        addTaskButton = rootView.findViewById(R.id.add_task_button);

        // Ukrycie podytutłu i zablokowanie przycisków
        overdueTasksTextView.setVisibility(View.GONE);
        subtitleVisibilityButton.setEnabled(false);
        addTaskButton.setEnabled(false);

        // Pobranie elementów widoku
        nameField = view.findViewById(R.id.task_name);
        nameField.setText(task.getName());

        // Metoda nasłuchująca zmianę tekstu (nazwy zadania)
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            // Po dokonaniu zmiany ustaw nową nazwę zadania
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                task.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // Pola daty z widoku
        dateField = view.findViewById(R.id.task_date);
        DatePickerDialog.OnDateSetListener date = (view12, year, month, day) ->{
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            setupDateFieldValue(calendar.getTime());
            task.setDate(calendar.getTime());
        };
        dateField.setOnClickListener(view1->
                new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
        setupDateFieldValue(task.getDate());


        // Pobranie listy wyboru kategorii z widoku
        categorySpinner = view.findViewById(R.id.task_category);
        categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, Category.values()));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.setCategory(Category.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setSelection(task.getCategory().ordinal());

        // Pobranie checkboxa z widoku
        doneCheckBox = view.findViewById(R.id.task_done);
        // Automatyczne ustawienie checkboxa jako zaznaczony lub nie w zależności od tego czy zadanie zostało wykonane
        doneCheckBox.setChecked(task.isDone());
        // Metoda nasłuchująca zmianę wartości checkboxa
        doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> task.setDone(isChecked));

        // Pobranie przycisku zapisz z widoku
        saveButton=view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jeśli fragment jest dodany i posiada nadrzędne acitvity
                if (isAdded() && getActivity() != null) {
                    getActivity().finish(); // Zakończ nadrzędne activity w tym też fragment
                }
            }
        });

        return view;
    }

    // Ustawienie nowej wybranej daty w kontrolce widoku
    private void setupDateFieldValue(Date date){
        Locale locale = new Locale("pl", "PL");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);
        dateField.setText(dateFormat.format(date));
    }
}
