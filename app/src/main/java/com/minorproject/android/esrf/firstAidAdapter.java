package com.minorproject.android.esrf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.minorproject.android.esrf.Helping_Classes.statics;
import com.minorproject.android.esrf.Models.firstAidLoc;
import java.util.ArrayList;

public class firstAidAdapter extends RecyclerView.Adapter<firstAidAdapter.MyViewHolder> {
    public ArrayList<firstAidLoc> firstAidLocArrayList;
    public Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name,distance,type;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.name);
            distance = (TextView)view.findViewById(R.id.distance);
            type = (TextView)view.findViewById(R.id.type);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.RelativeL);
        }

    }

    public firstAidAdapter(Context mContext, ArrayList<firstAidLoc> firstAidLocArrayList) {
        this.mContext = mContext;
        this.firstAidLocArrayList = firstAidLocArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.firstaid_card, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final firstAidLoc floc = firstAidLocArrayList.get(position);
        holder.name.setText(floc.name);
        holder.type.setText(floc.type.toUpperCase());
        holder.distance.setText(Float.toString(floc.distance)+"m");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDailog(holder,floc);
            }
        });



    }


    public void confirmationDailog(final MyViewHolder holder,final firstAidLoc floc){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.AlertDialogCustom);
        builder.setTitle("Confirmation Box");
        builder.setMessage("Click YES to Open Google Maps For Directions Or Else Click No");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+floc.latitude+","+floc.longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        mContext.startActivity(mapIntent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        builder.show();

    }




    /*public String calcDistance(Double lat,Double lon){
        String distance;
        float distances[] = new float[1];
        Location.distanceBetween(statics.currLat,
                statics.currLong,
                lat,
                lon, distances);
        distance = Float.toString(distances[0]);
        return distance;

    }*/

    @Override
    public int getItemCount() {
        return firstAidLocArrayList.size();
    }


}
