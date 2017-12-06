package com.example.rad.funnydog.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rad.funnydog.R;
import com.example.rad.funnydog.data.Dogs;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rad on 2017-11-06.
 */

public class MyDogsRecyclerViewAdapter extends RecyclerView.Adapter<MyDogsRecyclerViewAdapter.ViewHolder> {

    public final List<Dogs> dogs;
    private final DogsFragments.OnFragmentInteractionListener listener;
    private Context context;
    private SQLiteDatabase myDataBase;
    private Picasso mPicasso;

    public MyDogsRecyclerViewAdapter(List<Dogs> dogs,
                                     DogsFragments.OnFragmentInteractionListener listener) {
        this.dogs = dogs;
        this.listener = listener;
    }

    @Override
    public MyDogsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dog, parent, false);
        // Size in bytes (10 MB)
        long PICASSO_DISK_CACHE_SIZE = 1024 * 1024 * 5;

        // Use OkHttp as downloader
        Downloader downloader = new OkHttpDownloader(context,
                PICASSO_DISK_CACHE_SIZE);

        // Create memory cache
        Cache memoryCache = new LruCache(1024);

        mPicasso = new Picasso.Builder(context)
                .downloader(downloader).memoryCache(memoryCache).build();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        myDataBase = context.openOrCreateDatabase("Dogs", MODE_PRIVATE, null);
        try {
            String query = "SELECT * FROM Dog WHERE ID_DOG LIKE '"+dogs.get(position).id+"'";
            Cursor cursor = myDataBase.rawQuery(query, null);
            Log.d("in base",""+cursor.getCount());
            CharSequence text ="";
            if(cursor.getCount()>0){
               // holder.imageStar.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(R.mipmap.star)
                        .into(holder.imageStar);
                dogs.get(position).setStar(true);
            }else {
                Picasso.with(context)
                        .load(R.mipmap.empty_star)
                        .into(holder.imageStar);
               // holder.imageStar.setVisibility(View.GONE);
                dogs.get(position).setStar(false);
            }

            // while (cursor.moveToNext()) {
            //cursor.getString(cursor.getColumnIndex("allFriends"));
            //}
        }catch(Exception ex){
            Log.e("select","Erro in geting id "+ex.toString());
        }
        myDataBase.close();


        mPicasso.load(dogs.get(position).url)
                .resize(700,700)
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.imageDog);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    Log.d("action Listener","click"+dogs.get(position).getStar());
                    if(!dogs.get(position).getStar()){
                        Picasso.with(context)
                                .load(R.mipmap.star)
                                .into(holder.imageStar);
                        //holder.imageStar.setVisibility(View.VISIBLE);
                        dogs.get(position).setStar(true);
                    } else {
                        Picasso.with(context)
                                .load(R.mipmap.empty_star)
                                .into(holder.imageStar);
                       // holder.imageStar.setVisibility(View.GONE);
                        dogs.get(position).setStar(false);
                    }
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
        final ImageView imageStar;


        ViewHolder(View view) {
            super(view);
            this.view = view;
            imageDog = (ImageView) view.findViewById(R.id.imageDog);
            imageStar = (ImageView) view.findViewById(R.id.imageStar);

        }
    }
}
