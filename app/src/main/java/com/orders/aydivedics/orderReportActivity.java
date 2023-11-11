package com.orders.aydivedics;

import static com.orders.aydivedics.Utility.BASE_URL;
import static com.orders.aydivedics.Utility.reportlist;
import static com.orders.aydivedics.Utility.show_toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.orders.aydivedics.adapters.reportAdapter;
import com.orders.aydivedics.models.reportModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class orderReportActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    reportAdapter reportadapter;
    Button submit,date;
    ArrayAdapter<String> adapter;
    ProgressBar progressBar;
    AutoCompleteTextView agents, offices;
    String[] agents_array;
    String agent_name="";
    String office = "";
    String date_string = "";
    LinearLayoutManager linearLayoutManager;
    final Calendar myCalendar= Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);

        mRecyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);
        agents = findViewById(R.id.agents);
        offices = findViewById(R.id.offices);
        submit =  findViewById(R.id.button);
        date = findViewById(R.id.date);
        linearLayoutManager = new LinearLayoutManager(orderReportActivity.this, RecyclerView.VERTICAL, false);


        datePicker =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);


                String myFormat="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                date_string = dateFormat.format(myCalendar.getTime());
                date.setText(date_string);
                date.setError(null);
            }
        };


        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        get_data();
        load_agents();
        load_products();

        List<String> list = new ArrayList<String>();
//        list.add("Munees - BOB");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, list);
        adapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        offices.setAdapter(adapter);
        Log.d("size",String.valueOf(reportlist.size()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isVerified = true;
                if (date_string ==null || date_string.equals("")){
                    show_toast("Select a date",orderReportActivity.this);
                    date.setError("select date");
                    if (agent_name == null || agent_name.equals("")){
                        show_toast("Select an agent",orderReportActivity.this);
                        agents.setError("Select agent");
                        if (office == null || office.equals("")){
                            show_toast("Select an office",orderReportActivity.this);
                            offices.setError("Set office");
                        }
                    }

                }
                else{
                    networkCall();
                }






//              RecyclerView.ViewHolder holder =   mRecyclerView.findViewHolderForLayoutPosition(0);
//              EditText prepaidOrders = holder.itemView.findViewById(R.id)
//              EditText prepaidPieces = holder.itemView.findViewById(R.id.prepaidPieces);
//              EditText codOrders = holder.itemView.findViewById(R.id.codOrders);
//              EditText codPieces = holder.itemView.findViewById(R.id.codPieces);
//                TextView prod_name = holder.itemView.findViewById(R.id.prod_name);
//              Log.d("prod_name",prod_name.getText().toString());
//              Log.d("prepaidOrders",prepaidOrders.getText().toString());
//              Log.d("prepaidPieces",prepaidPieces.getText().toString());
//              Log.d("codOrders",codOrders.getText().toString());
//              Log.d("codPieces",codPieces.getText().toString());
            }
        });

        agents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.println(Log.DEBUG, "test", String.valueOf(i));
                Log.println(Log.DEBUG, "selected_value", agents_array[i]);
                agent_name = agents_array[i];
                Toast.makeText(orderReportActivity.this, agent_name, Toast.LENGTH_SHORT).show();
                agents.setError(null);
            }
        });

        offices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.println(Log.DEBUG, "test", String.valueOf(i));
                Log.println(Log.DEBUG, "selected_value", list.get(i).toString());
                office = list.get(i).toString();
                offices.setError(null);

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dateclick","clicked");
                DatePickerDialog datePickerDialog=new DatePickerDialog(orderReportActivity.this, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                //following line to restrict future date selection
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


            }
        });




    }





    public void load_products(){
        progressBar.setVisibility(View.VISIBLE);
        reportlist.clear();
//        progressbar_text.setText("loading products");
        Volley.newRequestQueue(this).add(
                new JsonRequest<JSONArray>(Request.Method.GET, BASE_URL+"load_products.php", null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progressBar.setVisibility(View.GONE);
                                Log.d(" vresposnse",response.toString());
                                JSONArray products_array = response;

                                for (int i=0; i<products_array.length(); i++) {
                                    try {
                                        JSONObject object = products_array.getJSONObject(i);
                                        String product_id = object.getString("product_id");
                                        String product_name = object.getString("product_name");
                                        Utility.reportlist.add(new reportModel(product_id,product_name,0,0,0,0));


//                                        Utility.list.add(new product(product_id,product_name,0,product_weight,0));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                reportadapter = new reportAdapter(reportlist,orderReportActivity.this);
                                mRecyclerView.setAdapter(reportadapter);
//                                reportadapter.notifyDataSetChanged();
                                Log.d("count", String.valueOf(reportadapter.getItemCount() ));



                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("agentslog",error.toString());
                        if (error.toString().contains("TimeoutError")){
//                        load_agents();
//                            show_toast("TimeoutError");
                        }
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        return params;
                    }

                    @Override
                    protected Response<JSONArray> parseNetworkResponse(
                            NetworkResponse response) {
                        try {
                            String jsonString = new String(response.data,
                                    HttpHeaderParser
                                            .parseCharset(response.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                }).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        }).setShouldCache(false);
    }
    public void load_agents(){
        progressBar.setVisibility(View.VISIBLE);
//        progressbar_text.setText("loading agents");
        Volley.newRequestQueue(this).add(
                new JsonRequest<JSONArray>(Request.Method.GET, BASE_URL+"load_agents.php", null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progressBar.setVisibility(View.GONE);
                                Log.d("resposnse",response.toString());
                                Log.d("agents",response.toString());
                                JSONArray array = null;
                                JSONObject agents_object;

                                try {
                                    agents_object = response.getJSONObject(0);
//                                    default_from_address =   response.getJSONObject(0).getString("from_address");
//                                    from_address.setText(default_from_address);
                                    array = new JSONArray(agents_object.getString("agents"));
                                    Log.d("agentsarray",array.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                               agents_array = new String[array.length()];
                                for (int i=0; i<array.length(); i++) {
                                    try {
                                        JSONObject object = array.getJSONObject(i);
                                        Log.d("name",object.getString("name"));
                                        agents_array[i] = object.getString("name");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                adapter =
                                        new ArrayAdapter<>(
                                                orderReportActivity.this,
                                                R.layout.dropdown_menu_popup_item,
                                                agents_array);


                                agents.setAdapter(adapter);

//                                //Creating the ArrayAdapter instance having the agents_array list
//                                ArrayAdapter aa = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, agents_array);
//                                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                //Setting the ArrayAdapter data on the Spinner
//                                agents.setAdapter(aa);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("agentslog",error.toString());
                        if (error.toString().contains("TimeoutError")){
//                        load_agents();
//                            show_toast("TimeoutError");
                        }
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("param1", "one");
//                        params.put("param2", "two");
                        return params;
                    }

                    @Override
                    protected Response<JSONArray> parseNetworkResponse(
                            NetworkResponse response) {
                        try {
                            String jsonString = new String(response.data,
                                    HttpHeaderParser
                                            .parseCharset(response.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                }).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        }).setShouldCache(false);
    }

    public void networkCall() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"postReport.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("reposne",response);
                        progressBar.setVisibility(View.GONE);
                        if (response.equals("success")){
                            show_toast("Success",orderReportActivity.this);
//                            Toast.makeText(orderReportActivity.this, "Success", Toast.LENGTH_SHORT).show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("reposne",error.toString());
//                        Toast.makeText(Utility.activity,error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                String json = new Gson().toJson(reportlist);
//                Log.d("jsonreport",json);
                params.put("agent",agent_name);
                params.put("office",office);
                params.put("date",date_string);
                params.put("reportData",json);
                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        stringRequest.setShouldCache(false);


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        Log.d("request_body",stringRequest.toString());

    }

}