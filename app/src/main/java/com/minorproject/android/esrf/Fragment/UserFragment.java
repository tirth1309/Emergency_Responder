package com.minorproject.android.esrf.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minorproject.android.esrf.LoginActivity;
import com.minorproject.android.esrf.MainActivity;
import com.minorproject.android.esrf.Models.User;
import com.minorproject.android.esrf.R;
import com.minorproject.android.esrf.register;

public class UserFragment extends Fragment {

    User user;
    ImageView iv,edit;
    TextView blodgroup,name,number,ename1,ename2;
    User currUser;
    Button logout;
    private GoogleSignInAccount acct;
    private GoogleSignInClient mclient;


    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currUser = (User) getArguments().getSerializable("curruser");
            Log.d("User in Home Frag",currUser.name);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mclient = GoogleSignIn.getClient(getActivity(), gso);
        acct = GoogleSignIn.getLastSignedInAccount(getActivity());




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        iv = (ImageView) view.findViewById(R.id.profileScreen);
        blodgroup = (TextView)view.findViewById(R.id.bg);
        name = view.findViewById(R.id.name);
        ename1 = view.findViewById(R.id.name1);
        ename2 = view.findViewById(R.id.name2);
        number = view.findViewById(R.id.number);
        edit = view.findViewById(R.id.edit);
        logout = view.findViewById(R.id.logout);
        //setDatachange();

        try {
            Glide.with(getActivity())
                    .load(acct.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv);

        }catch (NullPointerException e){
            Toast.makeText(getActivity().getApplication(),"image not found",Toast.LENGTH_LONG).show();
        }

        blodgroup.setText(currUser.bloodgroup);
        name.setText(currUser.name);
        ename1.setText(currUser.er.name1);
        ename2.setText(currUser.er.name2);
        number.setText(currUser.number);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), register.class));
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                signOut();

            }
        });

        return view;
    }
    private void signOut(){

            mclient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finishAffinity();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
