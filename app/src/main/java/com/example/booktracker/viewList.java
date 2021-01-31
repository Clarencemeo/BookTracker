package com.example.booktracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class viewList extends AppCompatActivity {

    Button addBook;
    Button goBack;
    TextView inputTitle;
    TextView inputPages;
    TextView inputGenre;
    TextView inputAuthor;
    RatingBar mBar;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        addBook = findViewById(R.id.add);
        goBack = findViewById(R.id.back);
        fAuth = FirebaseAuth.getInstance();
        inputGenre = findViewById(R.id.titleInput3);
        inputTitle = findViewById(R.id.titleInput);
        inputPages = findViewById(R.id.titleInput2);
        inputAuthor = findViewById(R.id.titleInput4);
        mBar = (RatingBar) findViewById(R.id.ratingBar);
        fStore = FirebaseFirestore.getInstance();

        String[] arraySpinner = new String[] {
                "Completed", "Reading", "Plan to Read", "Dropped"
        };
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookTitle = inputTitle.getText().toString().trim();
                if( TextUtils.isEmpty(bookTitle)) {
                    Toast.makeText(viewList.this, "Enter a title.", Toast.LENGTH_SHORT).show();
                    inputTitle.setError( "Title is required!" );
                }
                String pages = inputPages.getText().toString().trim();
                String genreTitle = inputGenre.getText().toString().trim();
                String author = inputAuthor.getText().toString().trim();
                double numStars = mBar.getRating();

                userID = fAuth.getCurrentUser().getUid();
                String spinnerText = s.getSelectedItem().toString();
                DocumentReference documentReference = fStore.collection("users").document(userID).collection(spinnerText).document(bookTitle);
                Map<String, Object> book = new HashMap<>();
                book.put("Title", bookTitle);
                book.put("Author", author);
                book.put("Genre", genreTitle);
                book.put("Pages", pages);
                book.put("Rating", numStars);
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                Date newDate = new Date(date.getTime());
                String stringdate = dt.format(newDate);
                book.put("Date", stringdate);
                documentReference.set(book);
                inputTitle.setText("");
                inputPages.setText("");
                inputGenre.setText("");
                inputAuthor.setText("");
                mBar.setRating(0);
                Toast.makeText(viewList.this, "Book Successfully Created!!!", Toast.LENGTH_SHORT).show();

            }
        });




        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
}