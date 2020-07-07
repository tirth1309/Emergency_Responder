package com.minorproject.android.esrf;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.minorproject.android.esrf.Models.firstAidTech;
import com.minorproject.android.esrf.firstAidAdapter;

import java.net.URL;
import java.util.ArrayList;

public class firstAidTechAdapter extends RecyclerView.Adapter<firstAidTechAdapter.MyViewHolder>{
    public ArrayList<firstAidTech> firstAidTechArrayList;
    public Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tech,desc;
        public ImageView image;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view){
            super(view);
            tech = (TextView)view.findViewById(R.id.tech);
            desc = (TextView)view.findViewById(R.id.desc);
            image = (ImageView)view.findViewById(R.id.image);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.RelativeL);
        }

    }

    public firstAidTechAdapter(Context mContext,ArrayList<firstAidTech> firstAidTechArrayList){
        this.mContext = mContext;
        this.firstAidTechArrayList = firstAidTechArrayList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.firstaidtech_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        firstAidTech ftech = firstAidTechArrayList.get(position);
        holder.tech.setText(ftech.name.toUpperCase());
        holder.desc.setText(ftech.text);

        try {
                //URL url = new URL(ftech.url);
                Glide.with(mContext)
                        .load(ftech.url)
                        .into(holder.image);

            }catch (NullPointerException e){
                Toast.makeText(mContext,"image not found",Toast.LENGTH_LONG).show();
            }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.image.getVisibility()==View.GONE){
                    holder.image.setVisibility(View.VISIBLE);
                }
                else {
                    holder.image.setVisibility(View.GONE);
                }

                ObjectAnimator animation = ObjectAnimator.ofFloat(holder.image, "translationY", 0.7f);
                animation.setDuration(800).start();
            }
        });

        }


    @Override
    public int getItemCount() {
        return firstAidTechArrayList.size();
    }
}
