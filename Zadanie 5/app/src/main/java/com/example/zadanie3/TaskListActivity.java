package com.example.zadanie3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class TaskListActivity extends SingleFragmentActivity {

    // TaskListActivity - Lista wszystkich zadań
    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}