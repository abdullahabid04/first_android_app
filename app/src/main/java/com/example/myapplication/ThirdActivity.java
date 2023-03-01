package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ThirdActivity extends AppCompatActivity {
    ImageButton updatebtn, readbtn, plotbtn;
    EditText tbsnameinput, tbspressureinput, startdateinput, enddateinput;
    TextView tbsidtext, regtypetext, lastupdatetext, tbsstatustext, pressureintext, pressureouttext;

    String set_pressure_url = "https://care.iub.edu.pk/sngpl/api/demand";
    String get_flag_url = "https://care.iub.edu.pk/sngpl/api/demandflag";
    String get_all_stations_url = "https://care.iub.edu.pk/sngpl/api/stations";

    public final static String EXTRA_ID = "com.example.myapplication.extra.ID";
    public final static String EXTRA_START_DATE = "com.example.myapplication.extra.START_DATE";
    public final static String EXTRA_END_DATE = "com.example.myapplication.extra.END_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        updatebtn = (ImageButton) findViewById(R.id.updatebtn);
        readbtn = (ImageButton) findViewById(R.id.readbtn);
        plotbtn = (ImageButton) findViewById(R.id.plotbtn);

        tbsnameinput = (EditText) findViewById(R.id.tbsnameinput);
        tbspressureinput = (EditText) findViewById(R.id.tbspressureinput);
        startdateinput = (EditText) findViewById(R.id.startdateinput);
        enddateinput = (EditText) findViewById(R.id.enddateinput);

        tbsidtext = (TextView) findViewById(R.id.tbsidtext);
        regtypetext = (TextView) findViewById(R.id.regtypetext);
        lastupdatetext = (TextView) findViewById(R.id.lastupdatetext);
        tbsstatustext = (TextView) findViewById(R.id.tbsstatustext);
        pressureintext = (TextView) findViewById(R.id.pressureintext);
        pressureouttext = (TextView) findViewById(R.id.pressureouttext);


         updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tbs_name = tbsnameinput.getText().toString();

                StringRequest request = new StringRequest(Request.Method.GET, get_all_stations_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0;i <= jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if (jsonObject.getString("tbs_name").equals(tbs_name)) {
                                    String tbs_id = jsonObject.getString("id");

                                    String pressure = tbspressureinput.getText().toString();
                                    updatePressure(tbs_id, pressure);

                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThirdActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(ThirdActivity.this);
                requestQueue.add(request);
            }
        });

        readbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tbs_name = tbsnameinput.getText().toString();

                StringRequest request = new StringRequest(Request.Method.GET, get_all_stations_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0;i <= jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if (jsonObject.getString("tbs_name").equals(tbs_name)) {
                                    String tbs_id = jsonObject.getString("id");

                                    getSensorInPressure(tbs_id);
                                    getSensorOutPressure(tbs_id);

                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThirdActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(ThirdActivity.this);
                requestQueue.add(request);
            }
        });

        plotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tbs_name = tbsnameinput.getText().toString();

                StringRequest request = new StringRequest(Request.Method.GET, get_all_stations_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0;i <= jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if (jsonObject.getString("tbs_name").equals(tbs_name)) {
                                    String tbs_id = jsonObject.getString("id");
                                    String start_date = startdateinput.getText().toString();
                                    String end_date = enddateinput.getText().toString();

                                    Intent intent = new Intent(ThirdActivity.this, FourthActivity.class);
                                    intent.putExtra(EXTRA_ID, tbs_id);
                                    intent.putExtra(EXTRA_START_DATE, start_date);
                                    intent.putExtra(EXTRA_END_DATE, end_date);
                                    startActivity(intent);

                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThirdActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(ThirdActivity.this);
                requestQueue.add(request);
            }
        });
    }

    private void updatePressure(String sid, String pressure) {
        StringRequest req = new StringRequest(Request.Method.POST, set_pressure_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("true")) {
                        Toast.makeText(ThirdActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThirdActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("sid", sid);
                params.put("value", pressure);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ThirdActivity.this);
        requestQueue.add(req);
    }

    private void getSensorInPressure(String sid) {
        String get_in_pressure_url = "https://care.iub.edu.pk/sngpl/api/pressure?sid=" + sid + "&flag=0";

        StringRequest request = new StringRequest(Request.Method.GET, get_in_pressure_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pressureintext.setText(response);
                Toast.makeText(ThirdActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThirdActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getSensorOutPressure(String sid) {
        String get_out_pressure_url = "https://care.iub.edu.pk/sngpl/api/pressure?sid=" + sid + "&flag=1";

        StringRequest request = new StringRequest(Request.Method.GET, get_out_pressure_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pressureouttext.setText(response);
                Toast.makeText(ThirdActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThirdActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
