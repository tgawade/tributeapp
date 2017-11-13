package com.android.martyrapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.martyrapp.model.MartyrPojo;
import com.android.martyrapp.model.MartyrResponse;
import com.android.martyrapp.util.ApiClient;
import com.android.martyrapp.util.ApiInterface;
import com.android.martyrapp.util.Constants;
import com.android.martyrapp.util.Utility;
import com.android.volley.RequestQueue;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private Intent intent;
    private List<MartyrPojo> martyrsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.nextBtn);
//        queue = Volley.newRequestQueue(this);
        intent = new Intent(MainActivity.this, MartyrListActivity.class);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.showProgressDailog(MainActivity.this);
                getMartyrDetails();
//                Utility.dismissProgressDailog();
            }
        });
    }
    public void getMartyrDetails() {
        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<MartyrResponse> call = apiService.getMartyrDetails(Constants.API_KEY_VAL);
            call.enqueue(new Callback<MartyrResponse>() {
                @Override
                public void onResponse(Call<MartyrResponse> call, Response<MartyrResponse> response) {

                    int statusCode = response.code();
                    martyrsList = response.body().getMartyrs().getMartyr();
                    for(int i=0; i< martyrsList.size(); i++) {
                        MartyrPojo martyrData =(MartyrPojo)martyrsList.get(i);
                        Log.i(" test %%%%% ", martyrData.getMartyrImageName());
                        intent.putExtra(martyrData.getMartyrId().toString(), martyrData);
                    }
                    startActivityForResult(intent,2);
                }
                @Override
                public void onFailure(Call<MartyrResponse> call, Throwable t) {
                    // Log error here since request failed
                    Utility.dismissProgressDailog();
                    Log.e("test", t.toString());
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}