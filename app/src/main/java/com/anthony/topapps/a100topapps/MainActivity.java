package com.anthony.topapps.a100topapps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String TAG = "MainActivity";
    private ListView itemsListView;
    private ListViewAdapter adapter;
    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLayout();
    }

    private void setupLayout(){
        itemsListView = (ListView)findViewById(R.id.itemsListView);
        items = new ArrayList<>();
        adapter = new ListViewAdapter(this);
        itemsListView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(setupRequest());
    }

    private StringRequest setupRequest(){
        String url = "https://itunes.apple.com/hk/rss/topfreeapplications/limit=100/json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " +response);
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    JSONObject feedJSON = responseJSON.getJSONObject("feed");
                    JSONArray entryJSONArray = feedJSON.getJSONArray("entry");
                    for(int i = 0, p = entryJSONArray.length(); i < p ; i++){
                        Item item = new Item();
                        item.setNumber(i);
                        item.setName(entryJSONArray.getJSONObject(i).getJSONObject("im:name").getString("label"));
                        item.setType(entryJSONArray.getJSONObject(i).getJSONObject("category").getJSONObject("attributes").getString("label"));
                        item.setIconLink(entryJSONArray.getJSONObject(i).getJSONArray("im:image").getJSONObject(1).getString("label"));
                        items.add(item);
                    }
                    adapter.addAllItems(items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
            }
        });
        return stringRequest;
    }
}
