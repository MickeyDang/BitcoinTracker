package com.londonappbrewery.bitcointicker;

import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.onClick;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currencycode = (String) parent.getItemAtPosition(position);
                letsDoSomeNetworking(currencycode);
                Log.d("Bitcoin", "Something selected: " + currencycode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Bitcoin", "No currency selected");
            }
        });

    }


    private void letsDoSomeNetworking(String currencyCode) {

        //parameter object. Supplies the Bitcoin Average API with necessary data
        RequestParams params = new RequestParams();
        params.put("market", "global");
        params.put("symbol", currencyCode);
        //Object for communication
        AsyncHttpClient client = new AsyncHttpClient();
//        Log.d("Bitcoin", "I am calling from " + BASE_URL + currencyCode);


        //Calls client for information and gets a response if successful as a JSON object
        client.get(BASE_URL + convertURL(currencyCode), params, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
//                Log.d("Bitcoin", "Successful call!");
//                Log.d("Clima", "JSON: " + response.toString());

                //calls bitcoinmodel class to handle data
                BitCoinModel newData = new BitCoinModel(response);
                updateUI(newData.getPrice());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Clima", "Request fail! Status code: " + statusCode);
                Log.d("Clima", "Fail response: " + response);
                Log.e("ERROR", e.toString());
                Toast.makeText(MainActivity.this, "Request For Currency Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String convertURL(String providedURL) {
        //adds BTC (or whatever prefix is necessary) to currency selected in slider for API
        final String PREFIX = "BTC";
        String newURL = PREFIX + providedURL;
        return newURL;
    }

    public void updateUI (Double newPrice) {
        //updates UI
        mPriceTextView.setText(newPrice.toString());
    }

}
