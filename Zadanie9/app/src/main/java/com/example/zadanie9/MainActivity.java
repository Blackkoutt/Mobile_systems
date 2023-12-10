package com.example.zadanie9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String BOOK_EXTRA = "BOOK_EXTRA";
    public static final String IMAGE_URL_BASE  = "http://covers.openlibrary.org/b/id/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/

    }

    // Metoda tworząca menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // Nasłuchiwać zdarzenia wprowadzania danych do pola search
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBooksData(query); // Metoda pobierająca z serwera dane wprowadzonej książki
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // Metoda pobierająca z serwera dane pasujące do wpisanej książki
    // query jest wprowadzonym ciągiem znaków
    private void fetchBooksData(String query){
        // postać zapytania jest odpowiednio formatowana np clean+code
        String finalQuery = prepareQuery(query);

        // Pobranie instancji retrofit - do połączeń z api
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);

        //Utworzenie pełnego zapytania HTTPGet
        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        // Dodanie zapytania do kolejki
        booksApiCall.enqueue(new Callback<BookContainer>() {

            // Jeśli otrzymano odpowiedź z serwera wywołaj metodę przypisującą pobrana odpowiedź do listy RecyclerView
            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                if(response.body()!=null){
                    setupBookListView(response.body().getBookList());
                }
            }

            // Jeśli nie otrzymano odpowiedzi wyświetl odpowiedni komuniakt
            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                Snackbar.make(findViewById(R.id.main_view), getString(R.string.error),
                        BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    // Metoda przypisująca odpowiednią odpowiedź do listy RecyclerView
    private void setupBookListView(List<Book> bookList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        adapter.setBooks(bookList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Metoda formatująca zapytanie
    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    // Metoda pomocnicza do sprawdzenia czy otrzymany tekst zawiera cokolwiek
    public boolean checkNullOrEmpty(String text){
        return text !=null && !TextUtils.isEmpty(text);
    }

    private class BookHolder extends RecyclerView.ViewHolder{
        private final TextView bookTitleTextView;
        private final TextView bookAuthorTextView;
        private final TextView bookNumberPagesTextView;
        private final ImageView bookImageView;
        private Book book;
        public BookHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.book_list_item, parent, false));

            // Po kliknięciu w element listy tworzona jest nowa aktywność do wyświetlenia szczegółów ksiązki
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("BOOK_EXTRA", book);
                    startActivity(intent);
                }
            });

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            bookNumberPagesTextView = itemView.findViewById(R.id.number_of_pages);
            bookImageView = itemView.findViewById(R.id.img_cover);
        }
        public void bind(Book book){
            this.book = book;
            if(book != null && checkNullOrEmpty(book.getTitle()) && book.getAuthors() != null){
                bookTitleTextView.setText(book.getTitle());
                bookAuthorTextView.setText(TextUtils.join(", ", book.getAuthors()));
                bookNumberPagesTextView.setText(book.getNumberOfPages());

                if(book.getCover() != null){
                    Picasso.with(itemView.getContext())
                            // Obrazek pobierany jest w formacie Small
                            .load(IMAGE_URL_BASE+ book.getCover() + "-S.jpg")
                            .placeholder(R.drawable.book_img).into(bookImageView);
                }
                else{
                    bookImageView.setImageResource(R.drawable.book_img);
                }

            }
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
            else{
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
}