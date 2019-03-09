package com.example.villi.beer_yo_ass;

import android.content.Context;
import android.content.Intent;
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

public class RecyclerViewAdapterComments extends RecyclerView.Adapter<RecyclerViewAdapterComments.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private ArrayList<String> mcommenter_name = new ArrayList<>();
    private ArrayList<String> mcomment = new ArrayList<>();
    private ArrayList<String> mcommenter_id = new ArrayList<>();
    private ArrayList<String> mcomment_time = new ArrayList<>();
    private ArrayList<String> mcomment_title = new ArrayList<>();


    public RecyclerViewAdapterComments(Context mContext, ArrayList<String> commenter_id, ArrayList<String> commenter_name, ArrayList<String> comment, ArrayList<String> comment_time, ArrayList<String> comment_title ) {
        this.mcommenter_id = commenter_id;
        this.mcommenter_name = commenter_name;
        this.mcomment = comment;
        this.mcomment_time = comment_time;
        this.mcomment_title = comment_title;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder: called.");

        final String userId;
        /*
        if (mbeer_id.get(position).length() == 4) {
            beerId = "0"+mbeer_id.get(position);
            Picasso.get().load("https://www.vinbudin.is/Portaldata/1/Resources/vorumyndir/medium/0"+mbeer_id.get(position)+"_r.jpg").into(holder.beer_image);
        } else {
            beerId = "" + mbeer_id.get(position);
            Picasso.get().load("https://www.vinbudin.is/Portaldata/1/Resources/vorumyndir/medium/"+mbeer_id.get(position)+"_r.jpg").into(holder.beer_image);
        }
        */

        holder.commenter_name.setText(mcommenter_name.get(position));
        holder.comment.setText(mcomment.get(position));
        holder.comment_time.setText(mcomment_time.get(position));

        /*
        holder.list_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mbeer_id.get(position));

                Toast.makeText(mContext, mbeer_name.get(position), Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(mContext, BeerActivity.class);
                intent1.putExtra("BEER_ID", "01009");
                //intent1.putExtra("BEER_ID", beerId);
                mContext.startActivities(new Intent[]{intent1});

            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return mcommenter_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView commenter_image;
        private TextView commenter_name;
        private TextView comment_time;
        private TextView comment;
        private RelativeLayout list_item_layout_comment;

        public ViewHolder(View itemView) {
            super(itemView);
            commenter_image = itemView.findViewById(R.id.commenter_image);
            commenter_name = itemView.findViewById(R.id.commenter_name);
            comment_time = itemView.findViewById(R.id.time);
            comment = itemView.findViewById(R.id.comment);
            list_item_layout_comment = itemView.findViewById(R.id.list_item_layout_comment);
        }
    }

}
