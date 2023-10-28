package com.example.zadanie3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class TaskFragment extends Fragment {

    private Task task;

    // Deklaracja elementów widoku
    private EditText nameField;
    private Button dateButton;
    private CheckBox doneCheckBox;
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

        // Pobranie przycisku z widoku
        dateButton = view.findViewById(R.id.task_date);
        dateButton.setText(task.getDate().toString());  // Ustawienie daty wykonania zadania jako tekst przycisku
        dateButton.setEnabled(false);   // Przycisk nie jest aktywny

        // Pobranie checkboxa z widoku
        doneCheckBox = view.findViewById(R.id.task_done);
        // Automatyczne ustawienie checkboxa jako zaznaczony lub nie w zależności od tego czy zadanie zostało wykonane
        doneCheckBox.setChecked(task.isDone());
        // Metoda nasłuchująca zmianę wartości checkboxa
        doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> task.setDone(isChecked));


        return view;
    }
}
