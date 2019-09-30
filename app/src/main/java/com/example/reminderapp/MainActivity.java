package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.eltohamy.materialhijricalendarview.CalendarDay;
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;
import com.github.eltohamy.materialhijricalendarview.OnDateSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;
    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private AdapterViewHolder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = findViewById(R.id.list);

        linearLayoutManager = new LinearLayoutManager(this);

        final MaterialHijriCalendarView calendarView=findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialHijriCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                getData(dateFormat(date.getDate().toString()));

            }
        });

        Button reminderList = findViewById(R.id.reminderList);
        reminderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SetRemainder.class);
                intent.putExtra("flag",false);
                intent.putExtra("date","");
                intent.putExtra("time","");
                intent.putExtra("title","");
                startActivity(intent);
            }
        });
    }

    private void getData(final String date) {
        pDialog=new ProgressDialog(this);
        pDialog.setTitle("Loding...");
        pDialog.show();

        String url="https://muslimsalat.com/london/weekly/"+date+"/true/5.json?key=938047ecabaa311353ea1341f324d847";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String mFajar = response.getJSONArray("items").getJSONObject(0).get("fajr").toString();
                            String mDhur = response.getJSONArray("items").getJSONObject(0).get("dhuhr").toString();
                            String mAsr = response.getJSONArray("items").getJSONObject(0).get("asr").toString();
                            String mMagrib = response.getJSONArray("items").getJSONObject(0).get("maghrib").toString();
                            String mIsha = response.getJSONArray("items").getJSONObject(0).get("isha").toString();

                            List<DataModel> models = new ArrayList<>();
                            models.add(new DataModel(date,changeTime(timeFormat(mFajar)),"04:30","FAJR"));
                            models.add(new DataModel(date,changeTime(timeFormat(mDhur)),"01:00","DHUHR"));
                            models.add(new DataModel(date,changeTime(timeFormat(mAsr)),"04:20","ASR"));
                            models.add(new DataModel(date,changeTime(timeFormat(mMagrib)),"06:00","MAGHRIB"));
                            models.add(new DataModel(date,changeTime(timeFormat(mIsha)),"05:50","ISHA"));

                            adapter = new AdapterViewHolder(getApplicationContext(),models);
                            mList.setAdapter(adapter);//set adapter in recycler view
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            mList.setLayoutManager(linearLayoutManager);
                            mList.setItemAnimator(new DefaultItemAnimator());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            pDialog.dismiss();
                        }

                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Tag", "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    private String dateFormat(String value)
    {

        Date date = new Date(value);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(date);
    }

    private String timeFormat(String value)
    {
        String time = null;
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
            Date date = simpleDateFormat.parse(value);
            time = simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    private String changeTime(String time)
    {
        String mTime;

        String[] splitTime = time.split(":");
        int hourOfDay = Integer.parseInt(splitTime[0]);
        int mMinute = Integer.parseInt(splitTime[1]);

        if (mMinute < 10) {
            mTime = hourOfDay + ":" + "0" + mMinute;
        } else {
            mTime = hourOfDay + ":" + mMinute;
        }

        return mTime;
    }

}
