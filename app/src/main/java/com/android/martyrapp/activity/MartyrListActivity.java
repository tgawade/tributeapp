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

import com.android.martyrapp.model.MartyrPojo;
import com.android.martyrapp.model.MartyrResponse;
import com.android.martyrapp.util.ApiClient;
import com.android.martyrapp.util.ApiInterface;
import com.android.martyrapp.util.Constants;
import com.android.martyrapp.util.DividerItemDecoration;
import com.android.martyrapp.util.Prefs;
import com.android.martyrapp.util.RecyclerTouchListener;
import com.android.martyrapp.util.Utility;
import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;

public class MartyrListActivity extends AppCompatActivity {
    private List<MartyrPojo> martyrList = new ArrayList<>();
    private List<MartyrPojo> martyrsList = new ArrayList<>();
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
                MartyrPojo martyr = martyrList.get(position);
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
    public void showInputDialog(final List<MartyrPojo> martyrList) {
        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final EditText newSearchEdt = (EditText) view.findViewById(R.id.searchEdt);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);
        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs(MartyrListActivity.this);
                if (!newSearchEdt.getText().toString().isEmpty()) {
                    String search = newSearchEdt.getText().toString();
                    prefs.setSearch(search);
                    dialog.dismiss();
                    getMartyrDetailFilter(search);
                }
            }
        });
    }

    public void getMartyrDetailFilter(String searchKeyword) {
        try {
            Utility.showProgressDailog(MartyrListActivity.this);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MartyrResponse> call = apiService.getMartyrDetailFilter(searchKeyword,Constants.API_KEY_VAL);
            call.enqueue(new Callback<MartyrResponse>() {
                @Override
                public void onResponse(Call<MartyrResponse> call, retrofit2.Response<MartyrResponse> response) {

                    int statusCode = response.code();
                    martyrList.clear();
                    martyrsList = response.body().getMartyrs().getMartyr();
                    if(martyrsList != null && martyrsList.size() == 0) {
                        Toast.makeText(getApplicationContext(), " Search string not found", Toast.LENGTH_SHORT).show();
                    } else {
                        for(MartyrPojo item: martyrsList) {
                            MartyrPojo martyr = new MartyrPojo();
//                          martyr.setGenre(martyrYear);
                            martyr.setMartyrName(item.getMartyrName());
//                          martyr.setYear(martyrYear);
                            martyr.setMartyrImageName(item.getMartyrImageName());
                            martyrList.add(martyr);
                            Log.i(" test in details >>>> ", item.getMartyrName() );
                        }
                        Utility.dismissProgressDailog();
                        mAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onFailure(Call<MartyrResponse> call, Throwable t) {
                    Log.e("test", t.toString());
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void prepareMartyrList(Bundle extras) {
            Set keySet = extras.keySet();
            for (int i = 0; i < keySet.size();i++) {
                MartyrPojo martyr = (MartyrPojo) extras.get(Integer.toString(i));
                martyrList.add(martyr);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
