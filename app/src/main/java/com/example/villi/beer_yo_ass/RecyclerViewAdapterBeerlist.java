/**
 * The RecyclerViewAdapter classes are used to display a list of some items.
 * This class is mainly used to dislay comments in the beer page.
 * The RecyclerViewAdapter takes in comment data and sets it up in a list like shown
 * in its corresponding layout.
 */

package com.example.villi.beer_yo_ass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerViewAdapterBeerlist extends RecyclerView.Adapter<RecyclerViewAdapterBeerlist.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private ArrayList<String> mBeerlistName = new ArrayList<>();
    private ArrayList<String> mBeerlistId = new ArrayList<>();
    private ArrayList<JSONObject> mBeerlist_data;
    private String beerId;
    private String type;
    private static final String HOST_URL = "https://beer-yo-ass-backend.herokuapp.com/";
    private static final String ADD_TO_BEERLIST_URL = HOST_URL + "addToDrinklist/";

    public RecyclerViewAdapterBeerlist(Context mContext, ArrayList<String> beerlistName, ArrayList<String> beerlistId, String beerId, String type, ArrayList<JSONObject> mBeerlist_data) {
        this.mBeerlistName = beerlistName;
        this.mBeerlistId = beerlistId;
        this.beerId = beerId;
        this.mContext = mContext;
        this.type = type;
        this.mBeerlist_data = mBeerlist_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_beerlist_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder: called.");

        final String userId;

        holder.beerlist_name.setText(mBeerlistName.get(position));

        if(type.equals("addbeerlist")){
            holder.list_item_layout_beerlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String thisId = mBeerlistId.get(position);
                    addBeerToDrinklist(thisId);

                /*
                Intent intent1 = new Intent(mContext, BeerActivity.class);
                intent1.putExtra("BEER_ID", "01009");
                //intent1.putExtra("BEER_ID", beerId);
                mContext.startActivities(new Intent[]{intent1});*/

                }
            });
        }
        else if(type.equals("userpage")){
            holder.list_item_layout_beerlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String thisId = mBeerlistId.get(position);
                    Toast.makeText(mContext, "Beerlist #" + mBeerlistName.get(position), Toast.LENGTH_SHORT).show();

                    ArrayList<String> mbeerlist_data_string = new ArrayList<String>();

                    for(int i = 0; i < mBeerlist_data.size(); i++){
                        mbeerlist_data_string.add(mBeerlist_data.get(i).toString());
                    }
                    System.out.println(mbeerlist_data_string.get(1));
                    Intent intent1 = new Intent(mContext, BeerlistActivity.class);

                    intent1.putStringArrayListExtra("BEERLIST_DATA", mbeerlist_data_string);
                    intent1.putExtra("BEERLIST_ID", thisId);
                    intent1.putExtra("BEERLIST_NAME", mBeerlistName.get(position));
                    mContext.startActivities(new Intent[]{intent1});                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mBeerlistName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView beerlist_name;
        private ImageView beerlist_icon;
        private RelativeLayout list_item_layout_beerlist;

        public ViewHolder(View itemView) {
            super(itemView);

            beerlist_icon = itemView.findViewById(R.id.beerlist_icon);
            beerlist_name = itemView.findViewById(R.id.beerlist_name);
            list_item_layout_beerlist = itemView.findViewById(R.id.list_item_layout_beerlist);
        }
    }


    private void addBeerToDrinklist(String beerlistId) {

        ///addToDrinklist/{username}/{drinklistId}/{beerId}
        String url = ADD_TO_BEERLIST_URL +
                UserActivity.user + "/" +
                beerlistId + "/" +
                beerId;

        System.out.println(url);
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Adding beer to beerlist");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(mContext, "Beer added to beerlist", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        ((Activity)mContext).finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        System.out.println("Error response");
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
}


