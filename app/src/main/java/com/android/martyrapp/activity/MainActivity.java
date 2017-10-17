package com.android.martyrapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.martyrapp.model.Martyr;
import com.android.martyrapp.util.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.nextBtn);
        queue = Volley.newRequestQueue(this);
        intent = new Intent(MainActivity.this, MartyrListActivity.class);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getMartyrDetails();
            }
        });
    }
    public void getMartyrDetails() {
        try {
            JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(Request.Method.GET,
                    Constants.API_URL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject martyrObj1 = response.getJSONObject("martyr");
                                JSONArray martyrArray = martyrObj1.getJSONArray("martyr");
                                for (int i = 0; i < martyrArray.length(); i++) {
                                    JSONObject martyrObj = martyrArray.getJSONObject(i);
                                    String martyrId = Integer.valueOf(martyrObj.getInt("martyrId")).toString();
                                    String martyrName = martyrObj.get("martyrName").toString();
                                    String martyrYear = martyrObj.get("martyrYear").toString();
                                    String martyrImageName = martyrObj.get("martyrImageName").toString();
                                    Martyr martyr = new Martyr();
//                                    martyr.setGenre(martyrYear);
//                                    martyr.setYear("");
                                    martyr.setTitle(martyrName);
                                    martyr.setImageName(martyrImageName);
                                    intent.putExtra(martyrId,martyr);
                                }
                                startActivityForResult(intent,2);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error:", error.getMessage());
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}