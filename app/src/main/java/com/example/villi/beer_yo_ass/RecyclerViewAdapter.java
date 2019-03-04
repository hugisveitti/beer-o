package com.example.villi.beer_yo_ass;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mbeer_id = new ArrayList<>();
    private ArrayList<String> mbeer_name = new ArrayList<>();
    private ArrayList<String> mbeer_volume = new ArrayList<>();
    private ArrayList<String> mbeer_price = new ArrayList<>();

    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mbeer_id, ArrayList<String> mbeer_name, ArrayList<String> mbeer_volume, ArrayList<String> mbeer_price) {
        this.mbeer_id = mbeer_id;
        this.mbeer_name = mbeer_name;
        this.mbeer_volume = mbeer_volume;
        this.mbeer_price = mbeer_price;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder: called.");


        if (mbeer_id.get(position).length() == 4) {
            Picasso.get().load("https://www.vinbudin.is/Portaldata/1/Resources/vorumyndir/medium/0"+mbeer_id.get(position)+"_r.jpg").into(holder.beer_image);
        } else {
            Picasso.get().load("https://www.vinbudin.is/Portaldata/1/Resources/vorumyndir/medium/"+mbeer_id.get(position)+"_r.jpg").into(holder.beer_image);
        }

        holder.beer_name.setText(mbeer_name.get(position));
        holder.beer_volume.setText(mbeer_volume.get(position));
        holder.beer_price.setText(mbeer_price.get(position));

        holder.list_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mbeer_id.get(position));

                Toast.makeText(mContext, mbeer_name.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mbeer_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView beer_image;
        private TextView beer_name;
        private TextView beer_volume;
        private TextView beer_price;
        private RelativeLayout list_item_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            beer_image = itemView.findViewById(R.id.beer_image);
            beer_name = itemView.findViewById(R.id.beer_name);
            beer_volume = itemView.findViewById(R.id.beer_volume);
            beer_price = itemView.findViewById(R.id.beer_price);
            list_item_layout = itemView.findViewById(R.id.list_item_layout);
        }
    }

}
