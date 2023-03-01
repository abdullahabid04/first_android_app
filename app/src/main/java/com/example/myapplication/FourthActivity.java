package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourthActivity extends AppCompatActivity {
    GraphView graphView1;
    LineGraphSeries<DataPoint> series;

    String get_all_pressures_url = "https://care.iub.edu.pk/sngpl/api/allpressures";

    public ArrayList<String> timestamp_in = new ArrayList<String>();
    public ArrayList<String> pressure_in = new ArrayList<String>();
    public ArrayList<String> timestamp_out = new ArrayList<String>();
    public ArrayList<String> pressure_out = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        graphView1 = (GraphView) findViewById(R.id.graph1);

        Intent intent = getIntent();

        String tbs_id = intent.getStringExtra(ThirdActivity.EXTRA_ID);
        String start_date = intent.getStringExtra(ThirdActivity.EXTRA_START_DATE);
        String end_date = intent.getStringExtra(ThirdActivity.EXTRA_END_DATE);

        getAllPressures(tbs_id, start_date, end_date, graphView1);
    }

    private void getAllPressures(String sid, String start_date, String end_date, GraphView graphView1) {
        StringRequest req = new StringRequest(Request.Method.POST, get_all_pressures_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Toast.makeText(FourthActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.getString("flag").equals("0")) {
                            String timestamp = jsonObject.getString("timestamp");
                            String pressure = jsonObject.getString("pressure_value");

                            timestamp_in.add(timestamp);
                            pressure_in.add(pressure);
                        }
                        if (jsonObject.getString("flag").equals("1")) {
                            String timestamp = jsonObject.getString("timestamp");
                            String pressure = jsonObject.getString("pressure_value");

                            timestamp_out.add(timestamp);
                            pressure_out.add(pressure);
                        }
                    }
                    plotGraph(graphView1);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FourthActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("sid", sid);
                params.put("start", start_date);
                params.put("end", end_date);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FourthActivity.this);
        requestQueue.add(req);
    }

    private void plotGraph(GraphView graphView1) throws ParseException {
        List<DataPoint> dataPoints1 = new ArrayList<>();

        for(int i = 0; i < timestamp_in.size(); i++){
            float pressure = Float.parseFloat(pressure_in.get(i));

            String strDate = timestamp_in.get(i);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(strDate);

            assert date != null;
            dataPoints1.add(new DataPoint(date, pressure));
        }

        DataPoint[] pointsArray1 = new DataPoint[dataPoints1.size()];
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(dataPoints1.toArray(pointsArray1));

        graphView1.addSeries(series1);

        List<DataPoint> dataPoints2 = new ArrayList<>();

        for(int i = 0; i < timestamp_out.size(); i++){
            float pressure = Float.parseFloat(pressure_out.get(i));

            String strDate = timestamp_out.get(i);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(strDate);

            assert date != null;
            dataPoints2.add(new DataPoint(date, pressure));
        }

        DataPoint[] pointsArray2 = new DataPoint[dataPoints2.size()];
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(dataPoints2.toArray(pointsArray2));

        graphView1.addSeries(series2);

        timestamp_in.clear();
        pressure_in.clear();
        timestamp_out.clear();
        pressure_out.clear();
    }
}
