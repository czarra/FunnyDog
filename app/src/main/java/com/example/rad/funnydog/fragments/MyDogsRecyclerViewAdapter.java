package com.example.rad.funnydog.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rad.funnydog.R;
import com.example.rad.funnydog.data.Dogs;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Rad on 2017-11-06.
 */

public class MyDogsRecyclerViewAdapter extends RecyclerView.Adapter<MyDogsRecyclerViewAdapter.ViewHolder> {

    public final List<Dogs> dogs;
    private final DogsFragments.OnFragmentInteractionListener listener;
    private Context context;

    public MyDogsRecyclerViewAdapter(List<Dogs> dogs,
                                     DogsFragments.OnFragmentInteractionListener listener) {
        this.dogs = dogs;
        this.listener = listener;
    }

    @Override
    public MyDogsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        Picasso.with(context)
                .load(dogs.get(position).url)
                .into(holder.imageDog);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onFragmentInteraction(dogs.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dogs.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        final ImageView imageDog;


        ViewHolder(View view) {
            super(view);
            this.view = view;
            imageDog = (ImageView) view.findViewById(R.id.imageDog);


        }
    }
}
