package com.minorproject.android.esrf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minorproject.android.esrf.Fragment.FirstAid;
import com.minorproject.android.esrf.Helping_Classes.statics;
import com.minorproject.android.esrf.Models.firstAidLoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FirstAidList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private firstAidAdapter adapter;
    private ArrayList<firstAidLoc> firstAidLocArrayList;
    private DatabaseReference dbref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_list);

        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        firstAidLocArrayList = new ArrayList<>();
        setFirstAidList();


    }

    public void setFirstAidList(){
        dbref = FirebaseDatabase.getInstance().getReference().child("firstAid");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot temp: dataSnapshot.getChildren()) {
                        firstAidLoc floc = temp.getValue(firstAidLoc.class);
                        firstAidLocArrayList.add(temp.getValue(firstAidLoc.class));
                }
                computeDistances();
                sortArray();
                setcustomAdapter();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void setcustomAdapter(){
        adapter = new firstAidAdapter(FirstAidList.this,firstAidLocArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(FirstAidList.this));
        recyclerView.setAdapter(adapter);
    }

    public void computeDistances(){
        for(int i=0;i<firstAidLocArrayList.size();i++){
            firstAidLocArrayList.get(i).distance = calcDistance(firstAidLocArrayList.get(i).latitude,firstAidLocArrayList.get(i).longitude);
        }

    }

    public void sortArray(){
        Collections.sort(firstAidLocArrayList);
    }



    public float calcDistance(Double lat,Double lon){
        float distances[] = new float[1];
        Location.distanceBetween(statics.currLat,
                statics.currLong,
                lat,
                lon, distances);

        return distances[0];

    }




}
