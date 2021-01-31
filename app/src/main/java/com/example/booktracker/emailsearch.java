package com.example.booktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class emailsearch extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button backBack;
    ImageView searching;
    FirebaseAuth fAuth;
    TextView searchBar;
    String userID;
    FirebaseFirestore fStore;
    public static final String TAG = "DatabaseUpload";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailsearch);
        backBack = findViewById(R.id.back3);
        fAuth = FirebaseAuth.getInstance();
        searchBar = findViewById(R.id.searcher);
        fStore = FirebaseFirestore.getInstance();
        searching = findViewById(R.id.imageView3);
        userID = fAuth.getCurrentUser().getUid();

        String[] arraySpinner = new String[] {
                "Completed", "Reading", "Plan to Read", "Dropped"
        };
        Spinner s = (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        searching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
                searchBar.getText().toString().trim();
            }
        });
    }

}