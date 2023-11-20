package com.example.zadanie3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    public static final String KEY_EXTRA_TASK_ID = "TASK_ID"; // Klucz dostępowy do id
    private static final String KEY_SUBTITLE_VISIBLE = "subtitleVisible";
    private RecyclerView recyclerView; // Deklaracja obiektu RecycylerView - kontener zarządzający elementami listy
    private TaskAdapter adapter;    // Deklaracja obiektu TaskAdapter - odpowiedzialny za tworzenie obiektów ViewHolder
    private boolean subtitleVisible; // Zmienna sprawdzająca czy podtytuł jest aktualnie wyświetlany
    private TextView overdueTasksTextView;  // Podtytuł
    private Button subtitleVisibilityButton;    // Przycisk do ukrycia/pokazania podtytułu

    public TaskListFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);

        // Jeśli zapisano stan aktywności pobierz widoczność podtytułu
        if(savedInstanceState != null){
            subtitleVisible = savedInstanceState.getBoolean(KEY_SUBTITLE_VISIBLE);
        }
    }

    // Zapisz stan aktywności - widoczność podtytułu
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SUBTITLE_VISIBLE, subtitleVisible);
    }

    /*@Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_task_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }*/

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_task){
            Task task = new Task();
            TaskStorage.getInstance().addTask(task);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getID());
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.show_subtitle){
            subtitleVisible = !subtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }*/
    /*
    private void setToolbarMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.fragment_task_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.new_task){
                    Task task = new Task();
                    TaskStorage.getInstance().addTask(task);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getID());
                    startActivity(intent);
                    return true;
                }
                else if (menuItem.getItemId() == R.id.show_subtitle){
                    subtitleVisible = !subtitleVisible;
                    getActivity().invalidateOptionsMenu();
                    updateSubtitle();
                    return true;
                }
                else {
                    return false;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }*/

    // Metoda aktualizująca podtytuł - zliczenie niewykonanych zadań
    public void updateSubtitle(){
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        int todoTasksCount=0;
        for(Task task:tasks){
            if(!task.isDone()){
                todoTasksCount++;
            }
        }
        String subtitle = getString(R.string.subtitle_format, todoTasksCount);

        /*if(!subtitleVisible){
            subtitle=null;
        }*/

        overdueTasksTextView.setText(subtitle);

        /*AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        ActionBar ab = appCompatActivity.getSupportActionBar();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);*/
    }

    // Metoda ustawiająca widoczność podtytułu oraz ikony i tekstu przycisku w zależności od zmiennej
    private void SetSubtitleVisibility(){
        if(subtitleVisible){
            // Zmiana obrazka w przycisku
            Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.hide_subtitle);
            subtitleVisibilityButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

            overdueTasksTextView.setVisibility(View.VISIBLE); // Podtytuł jest widoczny
            subtitleVisibilityButton.setText(R.string.hide_subtitle);   // Zmiana tekstu w przycisku
        }
        else{
            // Zmiana obrazka w przycisku
            Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.show_subtitle);
            subtitleVisibilityButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

            overdueTasksTextView.setVisibility(View.GONE);  // Podytutł nie jest widoczny
            subtitleVisibilityButton.setText(R.string.show_subtitle);   // Zmiena tekstu w przycisku
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false); // Powiązanie z layoutem (konwertowanie na obiekt View)
        recyclerView = view.findViewById(R.id.task_recycler_view);  // Pobranie elementu RecycleView z widoku
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // Elementy bedą wyświetlane w jednej osi

        // Pobranie elementów z layoutu nadrzędnego (layoutu Activity)
        View rootView = getActivity().getWindow().getDecorView().getRootView();
        overdueTasksTextView = rootView.findViewById(R.id.overdue_tasks);
        subtitleVisibilityButton = rootView.findViewById(R.id.show_hide_subtitle);

        SetSubtitleVisibility();    // Wstępne ustawienie widoczności podtytułu

        subtitleVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtitleVisible = !subtitleVisible; // Zmiana widoczności podtytułu
                SetSubtitleVisibility();    // Ustawienie widoczności podtytułu
            }
        });

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
        updateSubtitle(); // Aktualizacja podtytułu
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(); // Aktualizacja widoku listy RecyclerView
    }

    // TaskHolder przechowuje elementy widoku (elementu listy)
    private class TaskHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        // Maksymalna długość nazwy zadania
        private final int MAX_NAME_LENGTH=24;

        // Deklaracja elementów widoku
        private TextView nameTextView;
        private TextView dateTextView;
        private ImageView iconImageView;
        private TextView doneTextView;
        private CheckBox doneCheckBox;
        private Task task;
        public TaskHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_task, parent, false)); // Powiązanie z widokiem

            itemView.setOnClickListener(this); // Każdy element listy ReyclerView posiada nasłuchiwacz zdarzenia onClick

            // Pobranie elementów widoku
            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            doneTextView = itemView.findViewById(R.id.is_done);
            iconImageView = itemView.findViewById(R.id.category_icon);
            doneCheckBox = itemView.findViewById(R.id.is_task_done_checkbox);
        }
        public CheckBox getCheckBox(){
            return doneCheckBox;
        }
        // Metoda ustawiająca widok danego elementu listy (zadania)
        public void bind(Task task){
            this.task = task;

            String taskName = task.getName();

            // Jeśli tytuł jest zbyt długi ustaw na końcu 3 kropki
            if (taskName.length() > MAX_NAME_LENGTH) {
                taskName = taskName.substring(0, MAX_NAME_LENGTH - 3) + "...";
            }

            // Jeśli zadanie zostało wykonane przekreśl je
            SpannableString spannableName = new SpannableString(taskName);
            if (task.isDone()) {
                spannableName.setSpan(new StrikethroughSpan(), 0, spannableName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            nameTextView.setText(spannableName); // Ustaw nazwę zadania

            dateTextView.setText(task.getDate().toString());

            // Ustaw odpowiednią ikonę
            if(task.getCategory().equals(Category.HOME)){
                iconImageView.setImageResource(R.drawable.ic_house);
            }
            else{
                iconImageView.setImageResource(R.drawable.ic_study);
            }

            // W zależności od statusu wykonania zadania zmienia się kolor daty i informacji
            int is_task_done = task.isDone() ? R.string.done : R.string.undone;
            int color = task.isDone() ? R.color.green : R.color.red;
            doneTextView.setText(is_task_done);
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
            CheckBox checkBox = holder.getCheckBox(); // Pobierz checkBox z viewHoldera
            checkBox.setChecked(tasks.get(position).isDone());

            // Jeśli checkbox został wciśnięty
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->{
                tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked);
                updateSubtitle(); // Zaktualizuj podtytuł
                holder.bind(task);  // Wywołaj metodę bind ViewHoldera (zaktuualizuj informację o zadaniu)
            });

            holder.bind(task); // Wiązanie elementów widoku elementu listy (viewHolder) z danymi
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
}
