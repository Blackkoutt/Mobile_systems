package com.example.zadanie9;

import static com.example.zadanie9.MainActivity.IMAGE_URL_BASE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private TextView titleDetails;
    private ImageView imageDetails;
    private TextView authorDetails;
    private TextView pagesDetails;
    private TextView publisherDetails;
    private TextView languageDetails;
    private TextView ratingDetails;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Pobranie elementów widoku
        titleDetails = findViewById(R.id.title_details);
        imageDetails = findViewById(R.id.img_details);
        authorDetails = findViewById(R.id.author_details);
        pagesDetails = findViewById(R.id.pages_details);
        publisherDetails = findViewById(R.id.publisher_details);
        languageDetails = findViewById(R.id.language_details);
        ratingDetails = findViewById(R.id.rating_details);


        // Pobranie przekazanego parametru - książki
        Intent intent = getIntent();
        if (intent.hasExtra("BOOK_EXTRA")) {
            book = (Book) intent.getSerializableExtra("BOOK_EXTRA");
        }

        // Ustawienie tekstu w TextView
        SetStringTextInView(book.getTitle(), titleDetails);
        SetListTextInView(book.getAuthors(), authorDetails);
        SetStringTextInView(book.getNumberOfPages(), pagesDetails);
        SetListTextInView(book.getPublishers(), publisherDetails);
        SetListTextInView(book.getLanguages(), languageDetails);
        SetStringTextInView(book.getRating(), ratingDetails);

        // Ustawienie obrazka
        if(book.getCover() != null){
            Picasso.with(this)
                    // -M - rozmiar obrazka jako medium
                    .load(IMAGE_URL_BASE+ book.getCover() + "-M.jpg")
                    .placeholder(R.drawable.book_img).into(imageDetails);
        }
        else{
            imageDetails.setImageResource(R.drawable.book_img);
        }

    }
    public boolean checkNullOrEmpty(String text){
        return text !=null && !TextUtils.isEmpty(text);
    }
    private void SetStringTextInView(String text, TextView view){
        if(checkNullOrEmpty(text)){
            view.setText(" "+text);
        }
        else{
            view.setText(" " + getString(R.string.NotFound));
        }
    }
    private void SetListTextInView(List<String> textList, TextView view){
        if(textList.size()!=0){
            view.setText(" "+TextUtils.join(", ", textList));
        }
        else{
            view.setText(" " + getString(R.string.NotFound));
        }
    }
}