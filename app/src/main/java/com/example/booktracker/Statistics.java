package com.example.booktracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {
    FirebaseAuth fAuth;
    String userID;
    FirebaseFirestore fStore;
    TextView calc;
    TextView reader;
    Button backer;
    String TAG = "DatabaseUpload";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        calc = findViewById(R.id.calculationRating);
        reader = findViewById(R.id.booksRead);
        backer = findViewById(R.id.backer);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        String[] arraySpinner = new String[] {
                "Completed", "Reading", "Plan to Read", "Dropped"
        };
        Spinner s2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter);

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String spinnerText = s2.getSelectedItem().toString();
                CollectionReference books = fStore.collection("users").document(userID).collection(spinnerText);
                books.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int totalBooks = 0;
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                totalBooks += 1;
                            }
                            reader.setText("" + totalBooks);
                            double total = 0;
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                if(document.getDouble("Rating") != null) {
                                    total += document.getDouble("Rating");
                                }
                            }
                            calc.setText("" + (total)/totalBooks);
                        }

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        //First count how many books there are total

       /* books.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    double total = 0;
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if(document.getDouble("Rating") != null) {
                            total += document.getDouble("Rating");
                        }
                    }
                    calc.setText("" + (total)/totalBooks);
                }

            }
        });*/
       backer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
         }
        });
    }
}