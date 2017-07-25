package com.londonappbrewery.bitcointicker;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mickeydang on 2017-07-24.
 */
//Parses the JSON to find the daily price
public class BitCoinModel {
    //Member Variable
    private Double mPrice;

    public BitCoinModel (JSONObject priceResults) {

        try {
            mPrice = priceResults.getJSONObject("averages").getDouble("day");

        } catch (JSONException e) {

        }
    }

    //getter method
    public Double getPrice() {
        return mPrice;
    }

}
