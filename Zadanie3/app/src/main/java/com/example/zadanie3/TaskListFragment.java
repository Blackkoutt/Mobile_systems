package com.example.zadanie3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    public static final String KEY_EXTRA_TASK_ID = "TASK_ID"; // Klucz dostępowy do id
    private RecyclerView recyclerView; // Deklaracja obiektu RecycylerView - kontener zarządzający elementami listy
    private TaskAdapter adapter;    // Deklaracja obiektu TaskAdapter - odpowiedzialny za tworzenie obiektów ViewHolder

    public TaskListFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false); // Powiązanie z layoutem (konwertowanie na obiekt View)
        recyclerView = view.findViewById(R.id.task_recycler_view);  // Pobranie elementu RecycleView z widoku
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // Elementy bedą wyświetlane w jednej osi
        updateView(); // Aktualizacja widoku
        return view;
    }
    private void updateView(){
        TaskStorage taskStorage = TaskStorage.getInstance(); // Pobranie instacji singletona
        List<Task> tasks = taskStorage.getTasks();  // Pobranie listy zadań
        // Jeśli adapter nie został zainicjowany
        if(adapter == null){
            adapter = new TaskAdapter(tasks); // Tworzenie adaptera odpowiedzialnego za tworzenie obiektów listy RecyclerView
            recyclerView.setAdapter(adapter);
        }

        // Adapter został zainicjowany
        else{
            adapter.notifyDataSetChanged(); // Odświeżenie elementów listy RecyclerView (dane elementów uległy zmianie)
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(); // Aktualizacja widoku listy RecyclerView
    }

    // TaskHolder przechowuje elementy widoku (elementu listy)
    private class TaskHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        // Deklaracja elementów widoku
        private TextView nameTextView;
        private TextView dateTextView;
        private TextView doneTextView;
        private Task task;
        public TaskHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_task, parent, false)); // Powiązanie z widokiem

            itemView.setOnClickListener(this); // Każdy element listy ReyclerView posiada nasłuchiwacz zdarzenia onClick

            // Pobranie elementów widoku
            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            doneTextView = itemView.findViewById(R.id.is_done);
        }

        // Metoda ustawiająca widok danego elementu listy (zadania)
        public void bind(Task task){
            this.task = task;
            nameTextView.setText(task.getName());
            dateTextView.setText(task.getDate().toString());

            // W zależności od statusu wykonania zadania zmienia się kolor daty i informacji
            int is_task_done = task.isDone() ? R.string.done : R.string.undone;
            int color = task.isDone() ? R.color.green : R.color.red;
            doneTextView.setText(is_task_done); // Wyświetl odpowiedź
            doneTextView.setTextColor(getResources().getColor(color));
            dateTextView.setTextColor(getResources().getColor(color));
        }


        // Obsługa zdarzenia onCLick wywołanego na elemencie listy
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity.class);  // Deklaracja nowej aktywności MainActvity
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getID());   // Przekazanie id zadania do nowej aktywności
            startActivity(intent);  // Uruchomienie nowej aktywności
        }
    }

    // TaskAdapter jest odpowiedzialny za tworzenie obiektów ViewHolder i wiązanie tych obiektów z danymi
    private class TaskAdapter extends  RecyclerView.Adapter<TaskHolder>{
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks){
            this.tasks=tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent); // Tworzenie nowego ViewHoldera
        }

        // Przekazanie danych zadania do ViewHoldera
        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task); // wiązanie elementów widoku elementu listy (viewHolder) z danymi
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
}
