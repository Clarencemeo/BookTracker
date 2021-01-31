package com.example.booktracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;

import kotlin.reflect.KFunction;

public class listView extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button backBack;
    FirebaseAuth fAuth;
    String userID;
    FirebaseFirestore fStore;
    public static final String TAG = "DatabaseUpload";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        backBack = findViewById(R.id.back3);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        //get the specific collection we want, which is the list of books!
        //Later, we might have more collections (such as Completed Books, Plan to Read, etc).

        //Spinner stuff
        String[] arraySpinner = new String[] {
                "Completed", "Reading", "Plan to Read", "Dropped"
        };
        Spinner s = (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        //Spinner stuff
        String[] sortSpinner = new String[] {
                "Title", "Author", "Date", "Rating", "Genre", "Pages"
        };
        Spinner s2 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sortSpinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter2);

        //ArrayList<ExampleItem> exampleList = new ArrayList<>();
        //Every time an item is selected in the drop down, do this.
        //Applies at initialization as well.
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<ExampleItem> generatedList = new ArrayList<>();
                String spinnerText = s.getSelectedItem().toString();
                String sortText = s2.getSelectedItem().toString();
                CollectionReference books = fStore.collection("users").document(userID).collection(spinnerText);
                books.orderBy(sortText).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                generatedList.add(new ExampleItem(R.drawable.book, document.getString("Title"), "Author: " + document.getString("Author") + ", Date: " + document.getString("Date"),
                                        "Pages: " + document.getString("Pages") + ", Genre: " + document.getString("Genre") + ", Rating:" + document.getDouble("Rating")));
                            }
                        }
                        mRecyclerView = (RecyclerView)findViewById(R.id.listview);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mAdapter = new ExampleAdapter(generatedList);

                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);


                        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                //Get the title of the book at the position (get mText1)
                                //Pass in the title of the document (aka book) into the edit activity
                                //The title of the document will be used to reference the document
                                //The other activity will delete the document under the passed title, and create a new
                                generatedList.get(position).changeText1("Clicked");
                                mAdapter.notifyItemChanged(position);
                            }

                            @Override
                            public void onDeleteClick(int position) {
                                DocumentReference book = fStore.collection("users").document(userID).collection(spinnerText).document(generatedList.get(position).getText1());
                                book.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                });
                                generatedList.remove(position);
                                mAdapter.notifyItemRemoved(position);



                            }
                        });

                    }



                });

            }



            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<ExampleItem> generatedList = new ArrayList<>();
                String spinnerText = s.getSelectedItem().toString();
                String sortText = s2.getSelectedItem().toString();
                CollectionReference books = fStore.collection("users").document(userID).collection(spinnerText);
                books.orderBy(sortText).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                generatedList.add(new ExampleItem(R.drawable.book, document.getString("Title"), "Author: " + document.getString("Author") + ", Date: " + document.getString("Date"),
                                        "Pages: " + document.getString("Pages") + ", Genre: " + document.getString("Genre") + ", Rating:" + document.getDouble("Rating")));
                            }
                        }
                        mRecyclerView = (RecyclerView)findViewById(R.id.listview);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mAdapter = new ExampleAdapter(generatedList);

                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);


                        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                //Get the title of the book at the position (get mText1)
                                //Pass in the title of the document (aka book) into the edit activity
                                //The title of the document will be used to reference the document
                                //The other activity will delete the document under the passed title, and create a new
                                generatedList.get(position).changeText1("Clicked");
                                mAdapter.notifyItemChanged(position);
                            }

                            @Override
                            public void onDeleteClick(int position) {
                                DocumentReference book = fStore.collection("users").document(userID).collection(spinnerText).document(generatedList.get(position).getText1());
                                book.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                });
                                generatedList.remove(position);
                                mAdapter.notifyItemRemoved(position);



                            }
                        });

                    }



                });

            }



            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });
       /* String spinnerText = s.getSelectedItem().toString();
        CollectionReference books = fStore.collection("users").document(userID).collection("Completed");
        books.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        exampleList.add(new ExampleItem(R.drawable.book, "Title: " + document.getString("Title"),
                                "Pages: " + document.getString("Pages") + ", Genre: " + document.getString("Genre") + ", Rating:" + document.getDouble("Rating")));
                    }
                }
                mRecyclerView = (RecyclerView)findViewById(R.id.listview);
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mAdapter = new ExampleAdapter(exampleList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

            }
        });
        */
       backBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), MainActivity.class));
           }
       });

       /*refresher.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ArrayList<ExampleItem> generatedList = new ArrayList<>();
               String spinnerText = s.getSelectedItem().toString();
               CollectionReference books = fStore.collection("users").document(userID).collection(spinnerText);
               books.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document: task.getResult()) {
                               Log.d(TAG, document.getId() + " => " + document.getData());
                               generatedList.add(new ExampleItem(R.drawable.book, "Title: " + document.getString("Title"),
                                       "Pages: " + document.getString("Pages") + ", Genre: " + document.getString("Genre") + ", Rating:" + document.getDouble("Rating")));
                           }
                       }
                       mRecyclerView = (RecyclerView)findViewById(R.id.listview);
                       mLayoutManager = new LinearLayoutManager(getApplicationContext());
                       mAdapter = new ExampleAdapter(generatedList);

                       mRecyclerView.setLayoutManager(mLayoutManager);
                       mRecyclerView.setAdapter(mAdapter);

                   }
               });

           }
       });
        */






    }

}