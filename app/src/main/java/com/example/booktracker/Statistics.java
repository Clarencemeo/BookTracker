package com.example.booktracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Statistics extends AppCompatActivity {
    FirebaseAuth fAuth;
    String userID;
    FirebaseFirestore fStore;
    Button backer;
    String TAG = "DatabaseUpload";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        PieChart pie_Chart = (PieChart) findViewById(R.id.piChart);
        backer = findViewById(R.id.backer);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        ArrayList<String> organizers = new ArrayList<String>();
        organizers.add("Completed");
        organizers.add("Reading");
        organizers.add("Plan to Read");
        organizers.add("Dropped");
        ArrayList<PieEntry> xaxisValues = new ArrayList<PieEntry>();
        for (String s: organizers) {
            CollectionReference books = fStore.collection("users").document(userID).collection(s);
            books.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int totalBooks = 0;
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            totalBooks += 1;
                        }
                        double total = 0;
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            if(document.getDouble("Rating") != null) {
                                total += document.getDouble("Rating");
                            }
                        }
                        DecimalFormat f = new DecimalFormat("##.00");
                        double d = total/totalBooks;


                        xaxisValues.add(new PieEntry(totalBooks,  s));
                        PieDataSet pieDataSet = new PieDataSet(xaxisValues, "Collections");
                        pieDataSet.setColors(Color.rgb(254, 200, 154), Color.rgb(239, 228, 219), Color.rgb(242, 98, 73),
                                Color.rgb(229, 152, 155));
                        pieDataSet.setValueTextSize(12f);
                        PieData pieData = new PieData((pieDataSet));
                        pie_Chart.setCenterText("Total number of books.");
                        pie_Chart.animate();
                        pie_Chart.getDescription().setEnabled(false);
                        pie_Chart.setData(pieData);
                        pie_Chart.invalidate();

                    }

                }
            });
        }



       backer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
         }
        });
    }
}