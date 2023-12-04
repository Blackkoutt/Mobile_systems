package com.example.libraryapp;

import static androidx.recyclerview.widget.ItemTouchHelper.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.SnackbarContentLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.EventListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final String KEY_EXTRA_BOOK_TITLE = "BOOK_TITLE";
    public static final String KEY_EXTRA_BOOK_AUTHOR = "BOOK_AUTHOR";
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;
    private BookViewModel bookViewModel;
    private Book editedBook;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Jeśli książka była edytowana
        if(requestCode == EDIT_BOOK_ACTIVITY_REQUEST_CODE && resultCode ==RESULT_OK){
            // Ustawiamy nowy tytuł i autora ksiązki
            editedBook.setTitle(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE));
            editedBook.setAuthor(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
            bookViewModel.update(editedBook); // Aktualizacja bazy
            // Powiadomienie snackbar
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_modified),
                    Snackbar.LENGTH_LONG).show();
        }

        // Jeśli książka była dodawana
        else if(requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE && resultCode ==RESULT_OK){

            // Tworzymy nową książkę
            Book book = new Book(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE),
                    data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
            bookViewModel.insert(book); // Dodajemy ją do bazy
            // Powiadomienie snackbar
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_added),
                    Snackbar.LENGTH_LONG).show();
        }
        // Jeśli nie udało się poprawnie zmodyfikować lub dodac ksiązki wyświetl informację o błędzie
        else{
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.empty_not_saved),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Dodanie recyclerview i adaptera
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Utworzenie ViewModelu
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel.findAll().observe(this, adapter::setBooks); // Pobranie wszystkich książek z bazy

        FloatingActionButton addBookButton = findViewById(R.id.add_button);
        // Kliknięcie floating button uruchamia nową aktyność do dodawania ksiązki
        addBookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
            }
        });

    }
    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bookTitleTextView;
        private Book book;
        private ImageView bookImageView;
        private TextView bookAuthorTextView;
        public BookHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.book_list_item, parent, false));

            itemView.setOnClickListener(this); // Ustawienie onClickListenera na każdym elemencie listy

            // Przy długim naciścnięciu na element
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Pokaż okno dialogowe z informacją czy napewno chcesz usunąć książkę
                    String message = getResources().getString(R.string.formatted_book_info, book.getAuthor(), book.getTitle());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.book_delete_question)
                            .setMessage(message);

                    // Dodanie onclick listenera na pozytywnym przycisku (YES)
                    builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Usunięcie książki z bazy
                            bookViewModel.delete(book);
                            // Wyświetlenie informacji o usunieciu książki
                            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_delete),
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create();
                    builder.show();
                    return true;
                }
            });

            // Przy geście "swipe"
            itemView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            String message = getResources().getString(R.string.archived_message, book.getTitle());
                            Snackbar.make(findViewById(R.id.coordinator_layout), message,
                                    Snackbar.LENGTH_LONG).show();
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Bez tego nie obsługiwany jest event click i long click
                    if (gestureDetector.onTouchEvent(event)) {
                        return true;
                    }
                    return false;
                }
            });

            bookTitleTextView = itemView.findViewById(R.id.title);
            bookAuthorTextView = itemView.findViewById(R.id.author);
            bookImageView = itemView.findViewById(R.id.book_img);

        }
        public void bind(Book book){
            this.book = book;
            bookTitleTextView.setText(book.getTitle());
            bookAuthorTextView.setText(book.getAuthor());
            bookImageView.setImageResource(R.drawable.ic_book);
        }

        // Obsługa eventu on click na elemencie listy
        @Override
        public void onClick(View v) {
            editedBook = book;
            Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
            Bundle extras = new Bundle();
            extras.putString(KEY_EXTRA_BOOK_TITLE, book.getTitle());
            extras.putString(KEY_EXTRA_BOOK_AUTHOR, book.getAuthor());
            intent.putExtras(extras);

            startActivityForResult(intent, EDIT_BOOK_ACTIVITY_REQUEST_CODE);
        }
    }
    private class BookAdapter extends RecyclerView.Adapter<BookHolder>{
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if(books != null){
                Book book = books.get(position);
                holder.bind(book);
            }
            else {
                Log.d("MainActivity", "No books");
            }
        }

        @Override
        public int getItemCount() {
            if(books != null){
                return books.size();
            }
            else{
                return 0;
            }
        }
        void setBooks(List<Book> books){
            this.books = books;
            notifyDataSetChanged();
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
