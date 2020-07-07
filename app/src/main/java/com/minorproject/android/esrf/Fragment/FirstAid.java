package com.minorproject.android.esrf.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minorproject.android.esrf.FirstAidList;
import com.minorproject.android.esrf.Models.User;
import com.minorproject.android.esrf.Models.firstAidTech;
import com.minorproject.android.esrf.R;
import com.minorproject.android.esrf.firstAidAdapter;
import com.minorproject.android.esrf.firstAidTechAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstAid.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstAid#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstAid extends Fragment {

    User currUser;
    private RecyclerView recyclerView;
    private firstAidTechAdapter adapter;
    private ArrayList<firstAidTech> firstAidTechArrayList;
    private DatabaseReference dbref;

    private OnFragmentInteractionListener mListener;

    public FirstAid() {
        // Required empty public constructor
    }


    public static FirstAid newInstance(String param1, String param2) {
        FirstAid fragment = new FirstAid();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currUser = (User)getArguments().getSerializable("curruser");
            Log.d("user in FirstAidFrag",currUser.name);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_aid, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);
        firstAidTechArrayList = new ArrayList<>();
        setFirstAidTechList();

        return view;
    }

    public void setFirstAidTechList(){
        dbref = FirebaseDatabase.getInstance().getReference().child("firstAidTechnique");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot temp : dataSnapshot.getChildren()){
                    firstAidTech ftech = temp.getValue(firstAidTech.class);
                    firstAidTechArrayList.add(ftech);
                }
                setCustomAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setCustomAdapter(){
        adapter = new firstAidTechAdapter(getActivity(),firstAidTechArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
