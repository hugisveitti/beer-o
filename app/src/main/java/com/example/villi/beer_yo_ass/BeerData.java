package com.example.villi.beer_yo_ass;

import org.json.JSONObject;

import java.util.ArrayList;

public class BeerData {

    private static ArrayList<JSONObject> mbeer_data = new ArrayList<>();

    public static void insertBeer(JSONObject beer){
        mbeer_data.add(beer);
    }

    public static ArrayList<JSONObject> getBeer_data() {
        return mbeer_data;
    }

    // get a beer with certain id
    // NOT READY!!
    public static JSONObject getBeer(String id) {
        System.out.println(mbeer_data.get(0));
        return mbeer_data.get(0);
    }

    // Return a random beer
    public static JSONObject getRandomBeer(){
        int ran = (int)(Math.random()*BeerData.getBeerListSize());
        return mbeer_data.get(ran);
    }


    public static int getBeerListSize(){
        return mbeer_data.size();
    }




}
