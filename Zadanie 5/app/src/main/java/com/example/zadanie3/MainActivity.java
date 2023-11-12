package com.example.zadanie3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {

    // Main Activity - szczegóły pojedynczego zadania (Task Fragment)
    @Override
    protected Fragment createFragment(){
        // Pobranie id wybranego zadania
        UUID taskID = (UUID)getIntent().getSerializableExtra(TaskListFragment.KEY_EXTRA_TASK_ID);
        return TaskFragment.newInstance(taskID);    // Zwrócenie obiektu danego zadania
    }
}