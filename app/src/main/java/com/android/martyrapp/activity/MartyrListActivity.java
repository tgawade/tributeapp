package com.android.martyrapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.martyrapp.model.Martyr;
import com.android.martyrapp.util.Constants;
import com.android.martyrapp.util.DividerItemDecoration;
import com.android.martyrapp.util.Prefs;
import com.android.martyrapp.util.RecyclerTouchListener;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class MartyrListActivity extends AppCompatActivity {
    private List<Martyr> martyrList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MartyrAdapter mAdapter;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private RequestQueue queue=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_martyr_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MartyrAdapter(this, martyrList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Bundle extras = getIntent().getExtras();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Martyr martyr = martyrList.get(position);
                //Toast.makeText(getApplicationContext(), martyr.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        prepareMartyrList(extras);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog(martyrList);
            }
        });
    }
    public void showInputDialog(final List<Martyr> martyrList) {
        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final EditText newSearchEdt = (EditText) view.findViewById(R.id.searchEdt);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);
        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();
        if(queue != null){
            queue.stop();
        }
        queue = Volley.newRequestQueue(this);
        queue.start();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs(MartyrListActivity.this);
                if (!newSearchEdt.getText().toString().isEmpty()) {
                    String search = newSearchEdt.getText().toString();
                    prefs.setSearch(search);
                    dialog.dismiss();
                    try {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                Constants.API_URL_SEARCH + search,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Log.d("showInputDialog" + new Date().getTime(),"in request");
                                            JSONObject martyrObj1 = response.getJSONObject("martyr");
                                            JSONArray martyrArray = martyrObj1.getJSONArray("martyr");

                                            if(martyrArray!= null && martyrArray.length() == 0) {
                                                Toast.makeText(getApplicationContext(), " Search string not found", Toast.LENGTH_SHORT).show();
                                            } else {
                                                martyrList.clear();
                                                for (int i = 0; i < martyrArray.length(); i++) {
                                                    JSONObject martyrObj = martyrArray.getJSONObject(i);
                                                    String martyrId = Integer.valueOf(martyrObj.getInt("martyrId")).toString();
                                                    String martyrName = martyrObj.get("martyrName").toString();
                                                    String martyrYear = martyrObj.get("martyrYear").toString();
                                                    String martyrImage = martyrObj.get("martyrImageName").toString();
                                                    Martyr martyr = new Martyr();
                                                    //martyr.setGenre(martyrYear);
                                                    martyr.setTitle(martyrName);
//                                                    martyr.setYear(martyrYear);
                                                    martyr.setImageName(martyrImage);
                                                    martyrList.add(martyr);
                                                }
                                                mAdapter.notifyDataSetChanged();
                                            }
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
        });
    }

    private void prepareMartyrList(Bundle extras) {
            Set keySet = extras.keySet();

            for (int i = 0; i < keySet.size();i++) {
                Martyr martyr = (Martyr) extras.get(Integer.toString(i));
                martyrList.add(martyr);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
