package com.example.zadanie3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    private Button addTaskButton, subtitleVisabilityButton;
    private TextView overdueTasksTextView;

    // Metoda onCreate analogiczna dla obu Acitvity - tworzone są jedynie inne obiekty fragmentów
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // W layoucie activity_main znajduje się kontener na fragmenty z którego korzystają obie aktywności
        setContentView(R.layout.activity_main);

        // Przycisk do dodania nowego zadania
        addTaskButton = findViewById(R.id.add_task_button);

        // Przycisk do pokazywania i chowania podtytułu
        subtitleVisabilityButton = findViewById(R.id.show_hide_subtitle);

        // Podtytuł
        overdueTasksTextView = findViewById(R.id.overdue_tasks);

        // Dodanie nowego zadania - uruchomienie fragmentu MainActivity z pustym zadaniem
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(SingleFragmentActivity.this, MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getID());
                startActivity(intent);
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();

        // Powiązanie fragmentu z jego kontenerem (w activity_main.xml)
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = createFragment(); // Tworzenie fragmentu w zależności od activity

            // Sprawdzenie klasy fragmentu
            if(fragment instanceof TaskFragment){
                // Zablokowanie przycisków i usunięcie podtytułu
                addTaskButton.setEnabled(false);
                overdueTasksTextView.setVisibility(View.GONE);
                subtitleVisabilityButton.setEnabled(false);
            }
            else{
                // Odblokowanie przycisków
                subtitleVisabilityButton.setEnabled(true);
                addTaskButton.setEnabled(true);
            }

            // Transakcja dodająca fragment do listy
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
    protected abstract Fragment createFragment(); // Abstrakcyjna metoda którą implementują obie Aktywności
}
