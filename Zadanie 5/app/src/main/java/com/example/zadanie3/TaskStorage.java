package com.example.zadanie3;

import static android.provider.Settings.System.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TaskStorage jest singletonem
public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage(); // Wczesna inicjalizacja obiektu TaskStorage

    private final List<Task> tasks;

    // Metoda dostępowa do obiektu TaskStorage
    public static TaskStorage getInstance(){
        return taskStorage;
    }

    // Prywatny konstruktor tworzący listę zadań
    private TaskStorage(){
        tasks = new ArrayList<>();
        for(int i=1;i<=150;i++){
            Task task = new Task();
            task.setName("Pilne zadanie numer "+i);
            task.setDone(i%3==0);
            if(i%3 == 0){
                task.setCategory(Category.STUDIES);
            }
            else{
                task.setCategory(Category.HOME);
            }
            tasks.add(task);
        }
    }
    public void addTask(Task task){
        tasks.add(task);
    }
    // Pozostałe metody dostępowe
    public List<Task> getTasks(){
        return this.tasks;
    }
    public Task getTask(UUID id){
        for (Task task : tasks) {
            if (task.getID().equals(id))
                return task;
        }
        return null;
    }
}
