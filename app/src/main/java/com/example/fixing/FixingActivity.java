package com.example.fixing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Connection;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.invoke.VolatileCallSite;
import java.util.HashMap;
import java.util.Map;

public class FixingActivity extends AppCompatActivity implements ScrapingResponse {
    private ScrapingTask scrapingTask = new ScrapingTask();

    private String abbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixing);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        abbr = intent.getStringExtra(MainActivity.EXTRA_ABBR).toUpperCase();

        prepareView();

        //this to set delegate/listener back to this class
        scrapingTask.delegate = this;
        //execute the async task
        scrapingTask.execute();
        parseJSON();
    }

    private void parseJSON() {
        try {
            String URL = "https://fixing-topchange.herokuapp.com/api//get_buy_sell_one";

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("abbreviation", abbr);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(response.has("message")) {
                        TextView buySellOne = findViewById(R.id.fieldBuySellOne);
                        String content = "";
                        try {
                            content = response.getString("message");
                        } catch(Exception e) {
                            Toast.makeText(FixingActivity.this, "Server response format error", Toast.LENGTH_SHORT).show();
                        }

                        buySellOne.setText(content);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } catch(Exception e) {
            Toast.makeText(FixingActivity.this, "Server connection error", Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareView() {
        TextView abbrField = findViewById(R.id.currencyAbbrText);
        abbrField.setText(abbr);

        //Get ID of the image that is relevant to this currency abbreviation
        int resID = getResources().getIdentifier(abbr.toLowerCase() + "_flag", "drawable", this.getPackageName());

        ImageView flagImage = findViewById(R.id.currencyFlagImage);
        flagImage.setImageResource(resID);
    }

    //Execute when scraping is over
    @Override
    public void processFinish() {
        String currencyDataString = scrapingTask.searchCurrency(abbr);
        String[] data = currencyDataString.split("\\s+");

        if(abbr.equals(data[0])) {
            loadFields(data[1], data[2]);
            parseJSON();
        } else {
            Toast.makeText(FixingActivity.this, "Currency not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFields(String per, String fixing) {
        ((TextView)findViewById(R.id.fieldPer)).setText(per);

        ((TextView)findViewById(R.id.fieldFixing)).setText(fixing);

        double fixingValue = 0.0d;

        try {
            fixingValue = Double.parseDouble(fixing);
        }
        catch(Exception e){
            Toast.makeText(FixingActivity.this, "Format problem", Toast.LENGTH_SHORT).show();
        }

        TextView buyTwo = findViewById(R.id.fieldBuyTwo);
        buyTwo.setText(String.valueOf(Math.ceil((fixingValue - 0.05*fixingValue)*1000) / 1000));

        TextView sellTwo = findViewById(R.id.fieldSellTwo);
        sellTwo.setText(String.valueOf(Math.floor((fixingValue + 0.05*fixingValue)*1000) / 1000));
    }
}
