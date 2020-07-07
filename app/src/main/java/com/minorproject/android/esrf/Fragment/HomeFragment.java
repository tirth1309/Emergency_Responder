package com.minorproject.android.esrf.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minorproject.android.esrf.HelpActivity;
import com.minorproject.android.esrf.Models.User;
import com.minorproject.android.esrf.R;
import com.skyfishjy.library.RippleBackground;


public class HomeFragment extends Fragment {

    final static String TAG="Home Fragment";
    private OnFragmentInteractionListener mListener;
    private User currUser;
    private DatabaseReference dbref;
    Intent intent;

    public HomeFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currUser = (User) getArguments().getSerializable("curruser");
            Log.d("User in Home Frag",currUser.name);
        }

        //getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RippleBackground rippleBackground=(RippleBackground)view.findViewById(R.id.content);
        intent = new Intent(getActivity(),HelpActivity.class);
        ImageView iv = (ImageView)view.findViewById(R.id.centerImage);

            rippleBackground.startRippleAnimation();
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("currUser", currUser);
                    confirmDailog();
                    //startActivity(intent);
                }
            });

        return view;
    }


    public void confirmDailog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
        builder.setTitle("Alert");
        builder.setMessage("You are about to alert all the people in your vicinity and Emergency Services !! Are you sure you want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

    }
    /*public void getUser(){


        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbref = FirebaseDatabase.getInstance().getReference("users");
        dbref = dbref.child(uid);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

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
