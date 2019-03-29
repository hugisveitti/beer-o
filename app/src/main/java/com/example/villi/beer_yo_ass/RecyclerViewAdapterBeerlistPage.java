/**
 * The RecyclerViewAdapter classes are used to display a list of some items.
 * This class is mainly used to dislay beers in the search page, later it might
 * be used to display list of beers on some other page.
 * The RecyclerViewAdapter takes in beer data and sets it up in a list like shown
 * in its corresponding layout.
 */

package com.example.villi.beer_yo_ass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerViewAdapterBeerlistPage extends RecyclerView.Adapter<RecyclerViewAdapterBeerlistPage.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterBeerlistPage";

    //URL and Request parameters
    private static final String HOST_URL = "https://beer-yo-ass-backend.herokuapp.com/";
    private static final String CHECK_BEER_URL = HOST_URL + "markBeerOnDrinklist/";

    private ArrayList<String> mbeer_id_unchecked = new ArrayList<>();
    private ArrayList<String> mbeer_name_unchecked  = new ArrayList<>();
    private ArrayList<String> mbeer_volume_unchecked  = new ArrayList<>();
    private ArrayList<String> mbeer_price_unchecked  = new ArrayList<>();
    private ArrayList<String> mbeer_alcohol_unchecked  = new ArrayList<>();

    private ArrayList<String> mbeer_id_checked = new ArrayList<>();
    private ArrayList<String> mbeer_name_checked  = new ArrayList<>();
    private ArrayList<String> mbeer_volume_checked  = new ArrayList<>();
    private ArrayList<String> mbeer_price_checked  = new ArrayList<>();
    private ArrayList<String> mbeer_alcohol_checked  = new ArrayList<>();
    private Context mContext;

    private ArrayList<JSONObject> mUncheckedData = new ArrayList<>();
    private ArrayList<JSONObject> mCheckedData = new ArrayList<>();

    private ArrayList<Boolean> checkedList = new ArrayList<>();

    private String beerlistId;

    public RecyclerViewAdapterBeerlistPage(Context mContext, ArrayList<JSONObject> mUncheckedData, ArrayList<JSONObject> mCheckedData, String beerlistId) throws JSONException {
        this.mUncheckedData = mUncheckedData;
        this.mCheckedData = mCheckedData;
        this.beerlistId = beerlistId;
        this.mContext = mContext;
        putData();
    }

    private void putData() throws JSONException {
        checkedList = new ArrayList<>();
        for (int i = 0; i < mUncheckedData.size(); i++) {
            mbeer_id_unchecked.add(mUncheckedData.get(i).get("beerId") + "");
            mbeer_name_unchecked.add(mUncheckedData.get(i).get("name") + "");
            mbeer_volume_unchecked.add("Magn " + mUncheckedData.get(i).get("volume") + " ml.");
            mbeer_price_unchecked.add(mUncheckedData.get(i).get("price") + " kr.");
            mbeer_alcohol_unchecked.add(mUncheckedData.get(i).get("alcohol") + "%");
            checkedList.add(false);
        }
        for (int i = 0; i < mCheckedData.size(); i++) {
            mbeer_id_unchecked.add(mCheckedData.get(i).get("beerId") + "");
            mbeer_name_unchecked.add(mCheckedData.get(i).get("name") + "");
            mbeer_volume_unchecked.add("Magn " + mCheckedData.get(i).get("volume") + " ml.");
            mbeer_price_unchecked.add(mCheckedData.get(i).get("price") + " kr.");
            mbeer_alcohol_unchecked.add(mCheckedData.get(i).get("alcohol") + "%");
            checkedList.add(true);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_beerlist_beers, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String beerId;
        if (mbeer_id_unchecked.get(position).length() == 4) {
            beerId = "0"+mbeer_id_unchecked.get(position);
            Picasso.get().load("https://www.vinbudin.is/Portaldata/1/Resources/vorumyndir/medium/0"+mbeer_id_unchecked.get(position)+"_r.jpg").placeholder(R.drawable.logo).into(holder.beer_image);
        } else {
            beerId = "" + mbeer_id_unchecked.get(position);
            Picasso.get().load("https://www.vinbudin.is/Portaldata/1/Resources/vorumyndir/medium/"+mbeer_id_unchecked.get(position)+"_r.jpg").placeholder(R.drawable.logo).into(holder.beer_image);
        }

        holder.beer_name.setText(mbeer_name_unchecked.get(position));
        holder.beer_volume.setText(mbeer_volume_unchecked.get(position));
        holder.beer_price.setText(mbeer_price_unchecked.get(position));
        holder.beer_alcohol.setText(mbeer_alcohol_unchecked.get(position));

        setCheckedImage(checkedList.get(position), holder.drunkBeer);

        holder.list_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, mbeer_name_unchecked.get(position), Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(mContext, BeerActivity.class);
                intent1.putExtra("BEER_ID", beerId);
                mContext.startActivities(new Intent[]{intent1});

            }
        });

        holder.drunkBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBeer(checkedList.get(position), holder.drunkBeer, beerId, position);
            }
        });
    }

    private void checkBeer(final Boolean checked, final ImageView image, String beerId, final int position) {
        String url = CHECK_BEER_URL +
                     UserActivity.user + "/" +
                     beerlistId + "/" +
                     beerId + "/" +
                     !checked;

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Updating your beerlist...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Boolean success = Boolean.valueOf(response);
                        if(success){
                            checkedList.set(position, !checked);
                            setCheckedImage(!checked, image);
                        }
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

    private void setCheckedImage(Boolean checked, ImageView image) {
        if(checked){
            image.setImageResource(R.drawable.empty_beer);
        }
        else{
            image.setImageResource(R.drawable.full_beer);
        }
    }


    @Override
    public int getItemCount() {
        return mbeer_name_unchecked.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView beer_image;
        private TextView beer_name;
        private TextView beer_volume;
        private TextView beer_price;
        private TextView beer_alcohol;
        private RelativeLayout list_item_layout;
        private RelativeLayout list_item_content;
        private ImageView drunkBeer;

        public ViewHolder(View itemView) {
            super(itemView);
            beer_image = itemView.findViewById(R.id.beer_image);
            beer_name = itemView.findViewById(R.id.beer_name);
            beer_volume = itemView.findViewById(R.id.beer_volume);
            beer_price = itemView.findViewById(R.id.beer_price);
            beer_alcohol = itemView.findViewById(R.id.beer_alcohol);
            list_item_layout = itemView.findViewById(R.id.list_item_layout);
            list_item_content = itemView.findViewById(R.id.list_item_content);
            drunkBeer = itemView.findViewById(R.id.drunkBeer);
        }
    }

}
