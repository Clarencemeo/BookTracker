package com.example.booktracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    Button bookAdd;
    Button listViewing;
    Button statsbook;
    Button logoutter;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        logoutter = findViewById(R.id.logout);
        userId = fAuth.getCurrentUser().getUid();
        username = findViewById(R.id.displayName);
        DocumentReference documentReference = fStore.collection("users").document(userId);
       documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //Need to check this to prevent a null pointer exception when the user signs out
                if (documentSnapshot == null) {
                    Log.i("Error; ", e.toString());
                } else {
                    username.setText(documentSnapshot.getString("uName"));
                }
            }
        });

        bookAdd = findViewById(R.id.AddBook);
        bookAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), viewList.class));
            }
        });
        listViewing = findViewById(R.id.viewList);
        listViewing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), listView.class));
            }
        });
        statsbook = findViewById(R.id.statisticsButton);
        statsbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Statistics.class));
            }
        });

        logoutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });
    }

    /*public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();

    }
    /*
     */
}