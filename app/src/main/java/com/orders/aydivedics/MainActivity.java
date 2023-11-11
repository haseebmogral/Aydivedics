package com.orders.aydivedics;

import static com.orders.aydivedics.Utility.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.orders.aydivedics.models.product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    public static TextInputEditText address, date, phone, pin, transaction_id,from_address,name;
    ScrollView scrollView;
    TableLayout ll;
    AppCompatButton button,cancel_button;
    AppCompatButton popup;
    AutoCompleteTextView agents, banks;
    RadioGroup radioGroup,paymentModes;
    RadioButton radioButton;
    CheckBox checkBox;
    Boolean is_update = false;
    Boolean isPinvalid = false;
    public String order_id, default_from_address, state, district;
    public static String agent_name,phone_number,date_string, bank_str, deliveryChannel, paymentMode;
//    public static String BASE_URL = "https://facetuneapp.facetune.in/";

//    public static String BASE_URL = "https://toptenaccounts.in/";
    TextView report_id, report_date, report_qty,report_phone,report_pin,report_orderDetails,report_address,report_from_address,report_transaction_id,report_agent,reportCustomerName, report_bank;
    TextView pinDetails;
    RelativeLayout previous_section;
    public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
    RelativeLayout progressBar;
    TextView progressbar_text, agentName;
    String[] agents_array;
    ArrayAdapter<String> adapter;
    public static final int MAX_RETIES = 3;
    public static final float MAX_BACKOFF_MULT = 1f;
    ConnectionDetector cd;
    public String requestTag;
    final Calendar myCalendar= Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datePicker;
    PopUpClass popUpClass;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VolleyLog.DEBUG = true;
        SharedPreferences sp1= getSharedPreferences ("Login", MODE_PRIVATE);

        String username = sp1.getString("username", null);
        String userType = sp1.getString("userType", null);
        agent_name =  username;

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
            }
        };







        if(!getSharedPreferences("facetune", Activity.MODE_PRIVATE).getBoolean("IS_ICON_CREATED", false)){
            addShortcut();
            getSharedPreferences("facetune", Activity.MODE_PRIVATE).edit().putBoolean("IS_ICON_CREATED", true);
        }

         cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Utility.show_toast("No active internet ",MainActivity.this);

            // stop executing code by return
            return;
        }
//
//        default_from_address = "Facetune\n" +
//                "Unit No 203, 2nd Floor Suite No.599,\n" +
//                "SBR CV Towers, Sector-I, Sy No 64,\n" +
//                "HUDA Techno Enclave,\n" +
//                "Madhapur, Hyderabad - 500081";




        address = findViewById(R.id.address);
        address.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("linecount","juscame");

                if (i == KeyEvent.KEYCODE_ENTER
                        && keyEvent.getAction() == KeyEvent.ACTION_UP) {

                    // get EditText text
                    String text = ((EditText) view).getText().toString();

                    // find how many rows it cointains
                    int editTextRowCount = text.split("\\n").length;
                    Log.d("linecount",String.valueOf(editTextRowCount));

                    // user has input more than limited - lets do something
                    // about that
                    if (editTextRowCount >= 10) {

                        // find the last break
                        int lastBreakIndex = text.lastIndexOf("\n");

                        // compose new text
                        String newText = text.substring(0, lastBreakIndex);

                        // add new text - delete old one and append new one
                        // (append because I want the cursor to be at the end)
                        ((EditText) view).setText("");
                        ((EditText) view).append(newText);

                    }
                }
                return false;
            }
        });
        date = findViewById(R.id.date);
        date_string = getCurrentDate();
        date.setText(date_string);

        phone = findViewById(R.id.phone);
        pin = findViewById(R.id.pin);
        button = findViewById(R.id.button);
        popup = findViewById(R.id.popupButton);
        cancel_button = findViewById(R.id.cancel_button);
//        agents = findViewById(R.id.agents);
        banks = findViewById(R.id.banks);
        transaction_id = findViewById(R.id.transaction_id);
        previous_section = findViewById(R.id.previouse_section);
        scrollView = findViewById(R.id.scrollView);
        ll = findViewById(R.id.tablelayout);
        name = findViewById(R.id.name);
        progressBar =findViewById(R.id.progressbar);
        progressbar_text =findViewById(R.id.progressbar_text);
        from_address = findViewById(R.id.from_address);
        from_address .setText(default_from_address);
        checkBox = findViewById(R.id.checkbox);
        pinDetails  =findViewById(R.id.pinDetails);
        radioGroup = findViewById(R.id.deliveryChannels);
        paymentModes = findViewById(R.id.paymentmodes);
        agentName = findViewById(R.id.agent_name);

        load_agents();
        load_products();

        List<String> list = new ArrayList<String>();
//        list.add("Munees - BOB");
        list.add("Office 1 - SIB");
        list.add("Office 1 - BOB");
        list.add("Office 2");
        list.add("Office 3");
        list.add("Office 4");

        agentName.setText(agent_name);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, list);
        adapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        banks.setAdapter(adapter);

        banks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.println(Log.DEBUG, "test", String.valueOf(i));
                Log.println(Log.DEBUG, "selected_value", list.get(i).toString());
                bank_str = list.get(i).toString();

            }
        });


        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpClass = new PopUpClass(MainActivity.this);
                popUpClass.showPopupWindow(view);
            }
        });
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("dateclick","clicked");
                    DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, datePicker, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));

                    //following line to restrict future date selection
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show();


                }
            });




        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true){
                    from_address.setEnabled(true);
                    from_address.setFocusableInTouchMode(true);
                    from_address.setFocusable(true);
                    from_address.setClickable(true);
                    from_address.setActivated(true);

                }
                else{
                    from_address.setEnabled(false);
                    from_address.setFocusable(false);
                    from_address.setClickable(false);
                }
            }
        });



        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Toast.makeText(MainActivity.this, editable.toString(), Toast.LENGTH_SHORT).show();
                if (editable.toString().length()==10){
                    ll.removeAllViews();
                    phone_number = editable.toString();
                    init_tableLayout(phone_number);
                }

            }
        });

//        agents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.println(Log.DEBUG, "test", String.valueOf(i));
//                Log.println(Log.DEBUG, "selected_value", agents_array[i]);
//                agent_name = agents_array[i];
//                agents.setError(null);
//            }
//        });

        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()==6){
                    load_pinDetails(editable.toString());
                }
            }
        });

 


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroup.getCheckedRadioButtonId()==-1){
                    Utility.show_toast("select delivery channel",MainActivity.this);
                }
                else if (paymentModes.getCheckedRadioButtonId()==-1){
                    Utility.show_toast("select payment mode",MainActivity.this);
                }
                else{
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(selectedId);
                    deliveryChannel = radioButton.getText().toString();

                    int selectedIdpaymentMode = paymentModes.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(selectedIdpaymentMode);
                    paymentMode = radioButton.getText().toString();
                    if (check_values()== true){
                        if (!cd.isConnectingToInternet()) {
                            // Internet Connection is not present
                            Utility.show_toast("No active internet ",MainActivity.this);

                            // stop executing code by return
                            return;
                        }
                        else{

                            insert(MainActivity.this);

                        }
                    }
                    else{
                        Utility.show_toast("enter all values",MainActivity.this);
                    }
                }

            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear_fields();
//                if (clear_requests()){
//                    if (isCancelled == false){
//                        clear_fields();
//                        isCancelled = true;
//                    }
//                }

            }
        });



    }

    private int checkCount(ArrayList<product> list) {
        int count = 0;
        ArrayList<product> filteredList = new ArrayList();
        for (product p : Utility.list){
          count = count+p.item_count;
        }
            return count;
    }


    public void init_tableLayout(String contact_number) {
        Log.d("ontextchange", contact_number);
        progressBar.setVisibility(View.VISIBLE);
        progressbar_text.setText("checking previous orders");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, BASE_URL + "check_order.php?contact=" + contact_number.trim(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        ll.removeAllViews();
                        progressBar.setVisibility(View.GONE);

//                        Toast.makeText(MainActivity.this, "found previous records", Toast.LENGTH_SHORT).show();
                        Utility.show_toast("found previous records",MainActivity.this);

                        for (int i = 0; i < response.length(); i++) {
                            // creating a new json object and
                            // getting each object from our json array.
                            try {
                                // we are getting each json object.
                                previous_section.setVisibility(View.VISIBLE);
                                JSONObject responseObj = response.getJSONObject(String.valueOf(i));

                                String id = responseObj.getString("order_id");
                                String order_date = responseObj.getString("order_date");
                                String agent_str = responseObj.getString("agent");
                                String phone_number = responseObj.getString("phone_number");
                                String quantity = responseObj.getString("quantity");
                                String from_address_str = responseObj.getString("from_address");
                                String address_str = responseObj.getString("address");
                                String transaction_id_str = responseObj.getString("transaction_id");
                                String pincode_str = responseObj.getString("pincode");
                                String order_details = responseObj.getString("order_details");
                                String customer_name = responseObj.getString("name");
                                String bank = responseObj.getString("bank");
                                String deliveryChannel = responseObj.getString("delivery_channel");

                                TableRow row = new TableRow(MainActivity.this);
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);
                                row.setBackgroundColor(Color.parseColor("#dddddd"));
                                row.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        is_update = true;


                                        TableRow t = (TableRow) view;
                                        TextView report_id = (TextView) t.getChildAt(0);
                                        order_id= report_id.getText().toString();

                                        TextView report_date = (TextView) t.getChildAt(1);
                                        date_string = report_date.getText().toString();
                                        date.setText(date_string);
                                        date.setEnabled(true);
                                        date.requestFocus();
                                        date.setClickable(true);
                                        phone.setFocusable(true);
                                        date.setFocusableInTouchMode(true);





                                        TextView report_qty = (TextView) t.getChildAt(2);

                                        TextView report_phone = (TextView) t.getChildAt(3);
                                        phone.setText(report_phone.getText().toString());
                                        phone.setEnabled(false);
                                        phone.clearFocus();


                                        TextView report_address = (TextView) t.getChildAt(4);
                                        address.setText(report_address.getText().toString());

                                        TextView report_transaction_id = (TextView) t.getChildAt(5);
                                        transaction_id.setText(report_transaction_id.getText().toString());
                                        transaction_id.setEnabled(false);
                                        transaction_id.clearFocus();

                                        TextView report_pincode = (TextView) t.getChildAt(6);
                                        pin.setText(report_pincode.getText().toString());


                                        TextView order_details = (TextView) t.getChildAt(7);
                                        String orderDetailsStr = order_details.getText().toString();

                                        Utility.list.clear();

                                        Log.d("orderD" , orderDetailsStr);
                                        JSONArray products_array = null;
                                        try {
                                            products_array = new JSONArray(orderDetailsStr);
                                            for (int i=0; i<products_array.length(); i++) {

                                                JSONObject object = products_array.getJSONObject(i);
                                                Log.d("name",object.getString("prod_name"));
                                                String product_id = object.getString("prod_id");
                                                String product_name = object.getString("prod_name");
                                                int item_count = object.getInt("item_count");
                                                float product_weight = Float.parseFloat(object.getString("prod_weight"));
                                                float product_price = Float.parseFloat(object.getString("prod_price"));

                                                Utility.list.add(new product(product_id,product_name,product_price,product_weight,item_count));
                                                Log.d("list",Utility.list.toString());

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        TextView report_agent = (TextView) t.getChildAt(8);
//                                        agents.setText(report_agent.getText().toString(),false);
                                        agent_name = report_agent.getText().toString();
//                                        agents.setFocusable(false);
//                                        agents.clearFocus();
//                                        agents.setEnabled(false);
//                                        agents.setFocusableInTouchMode(false);
//                                        adapter = null;
//                                        agents.setAdapter(adapter);

                                        TextView order_customerName = (TextView) t.getChildAt(10);
                                        String order_customerNameStr = order_customerName.getText().toString();
                                        name.setText(order_customerNameStr);

                                        TextView report_bank = (TextView) t.getChildAt(11);
                                        banks.setText(report_bank.getText().toString(),false);
                                        bank_str = report_bank.getText().toString();

                                        TextView report_delivery_channel = (TextView)t.getChildAt(12);
                                        if (report_delivery_channel.getText().toString().equals("Indian Postal")){
                                            radioGroup.check(R.id.IP);
                                        }
                                        else if(report_delivery_channel.getText().toString().equals("Delhivery")){
                                            radioGroup.check(R.id.delhivery);
                                        }
                                        else{
                                            radioGroup.clearCheck();
                                        }


//                                        agent_name = report_agent.getText().toString();
//                                        agents.setFocusable(false);
//                                        agents.clearFocus();
//                                        agents.setEnabled(false);
//                                        agents.setFocusableInTouchMode(false);
//                                        adapter = null;
//                                        agents.setAdapter(adapter);




                                        from_address.setText(from_address_str);


                                        button.setText("Update");


                                    }
                                });

                                report_phone = new TextView(MainActivity.this);
                                report_phone.setText(phone_number);
                                report_phone.setVisibility(View.GONE);

                                report_address = new TextView(MainActivity.this);
                                report_address.setText(address_str);
                                report_address.setVisibility(View.GONE);

                                report_from_address = new TextView(MainActivity.this);
                                report_from_address.setText(from_address_str);
                                report_from_address.setVisibility(View.GONE);

                                reportCustomerName = new TextView(MainActivity.this);
                                reportCustomerName.setText(customer_name);
                                reportCustomerName.setVisibility(View.GONE);

                                report_transaction_id = new TextView(MainActivity.this);
                                report_transaction_id.setText(transaction_id_str);
                                report_transaction_id.setVisibility(View.GONE);

                                report_pin = new TextView(MainActivity.this);
                                report_pin.setText(pincode_str);
                                report_pin.setVisibility(View.GONE);

                                report_orderDetails = new TextView(MainActivity.this);
                                report_orderDetails.setText(order_details);
                                report_orderDetails.setVisibility(View.GONE);


                                report_id = new TextView(MainActivity.this);
                                report_id.setText(id);
                                report_id.setTextColor(Color.BLACK);

                                report_agent = new TextView(MainActivity.this);
                                report_agent.setText(agent_str);
                                report_agent.setVisibility(View.GONE);

                                report_date = new TextView(MainActivity.this);
                                report_date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                report_date.setText(order_date);
                                report_date.setTextColor(Color.BLACK);

                                report_qty = new TextView(MainActivity.this);
                                report_qty.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                                report_qty.setText(quantity);
                                report_qty.setTextColor(Color.BLACK);
//                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
//                                        RelativeLayout.LayoutParams.WRAP_CONTENT
//                                );
//                                params.setMargins(5, 0, 5, 0);
//                                report_qty.setLayoutParams(params);

                                report_bank = new TextView(MainActivity.this);
                                report_bank.setText(bank);
                                report_bank.setVisibility(View.GONE);

                                TextView report_delivery_channel = new TextView(MainActivity.this);
                                report_delivery_channel.setText(deliveryChannel);
                                report_delivery_channel.setVisibility(View.GONE);

                                TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                                report_date.setLayoutParams(params);
                                report_id.setLayoutParams(params);
                                report_qty.setLayoutParams(params);
                                row.setPadding(10,20,30,20);

                                row.addView(report_id);
                                row.addView(report_date);
                                row.addView(report_qty);
                                row.addView(report_phone);
                                row.addView(report_address);
                                row.addView(report_transaction_id);
                                row.addView(report_pin);
                                row.addView(report_orderDetails);
                                row.addView(report_agent);
                                row.addView(report_from_address);
                                row.addView(reportCustomerName);
                                row.addView(report_bank);
                                row.addView(report_delivery_channel);




                                ll.addView(row, i);

//                                Log.d("parsed_values", id + " " + order_date + " " + agent + " " + phone_number);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("error", error.toString());
                        progressBar.setVisibility(View.GONE);
                        if (error.toString().contains("TimeoutError")){
//                            init_tableLayout(phone_number);
                            Utility.show_toast("TimeoutError",MainActivity.this);

                        }



                    }
                });

// Access the RequestQueue through your singleton class.
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
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
        jsonObjectRequest.setShouldCache(false);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);




    }

    @NonNull
    public static String getCurrentDate() {
        Date d = new Date(System.currentTimeMillis());
        return yearFormat.format(d);
    }

    public void insert(Context context) {
        progressBar.setVisibility(View.VISIBLE);
        if (is_update)progressbar_text.setText("updating");
        if(!is_update) progressbar_text.setText("submitting new order");

//        button.setEnabled(false);
        String inserturl = BASE_URL + "insert.php";


        StringRequest sr = new StringRequest(Request.Method.POST, inserturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if (response.contains("insert success")) {
                    Utility.show_toast("Order created",MainActivity.this);
                    progressBar.setVisibility(View.GONE);
                    button.setEnabled(true);
                    call_alert("order created");
                }
                else if(response.equals("update success")){
                    button.setEnabled(true);
                    agents.setAdapter(null);
                    call_alert("update success");
                }
                else if (response.equals("transaction ID already exists")){
                    Utility.show_toast("transaction ID already exists",MainActivity.this);
                    transaction_id.setError("transaction ID already exists");
                    transaction_id.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    button.setEnabled(true);
                }
                else if(response.contains("TimeoutError")){
                    Utility.show_toast("TimeoutError , try again",MainActivity.this);
                    button.setEnabled(true);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response", error.toString());
                if (error.toString().contains("TimeoutError")){
//                    insert(MainActivity.this);
                    Utility.show_toast("TimeoutError",MainActivity.this);

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                ArrayList<product> filteredList = new ArrayList();
                for (product p : Utility.list){
                    if (p.item_count==0){

                    }
                    else{
                        filteredList.add(p);
                    }
                }
                Gson gson = new Gson();
                String order_details = gson.toJson(filteredList);
                Log.d("cartjson",order_details);
                Map<String, String> params = new HashMap<String, String>();
                params.put("agent", agent_name);
                params.put("name", name.getText().toString());
                params.put("phone_number", phone.getText().toString());
                params.put("pincode", pin.getText().toString());
                params.put("from_address", from_address.getText().toString());
                params.put("address", address.getText().toString());
                params.put("transaction_id", transaction_id.getText().toString());
                params.put("bank", bank_str);
                params.put("date", date_string);
                params.put("order_details",order_details);
                params.put("delivery_channel",deliveryChannel);
                params.put("state",state);
                params.put("district",district);
                params.put("payment_mode", paymentMode);


                if (order_id == null){
                    params.put("order_id", "null");
                }
                else{
                    params.put("order_id", order_id);
                }

                if (is_update){
                    params.put("action_type", "update");
                }
                else{
                    params.put("action_type", "insert");
                }
                Log.d("params",params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        sr.setRetryPolicy(new RetryPolicy() {
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
        sr.setShouldCache(false);
        requestTag = "POST_REQUEST";
        sr.setTag(requestTag);
        VolleyLog.DEBUG = true;

        Log.d("request_body",sr.toString());

        MySingleton.getInstance(this).addToRequestQueue(sr);
        Log.d("request_body",sr.toString());

    }

    public void call_alert(String message) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);


        //Setting message manually and performing action on button click
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        //Find the currently focused view, so we can grab the correct window token from it.
                        View view = MainActivity.this.getCurrentFocus();
                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                        if (view == null) {
                            view = new View(MainActivity.this);
                        }
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        clear_fields();


                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(message);
        alert.show();


    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
    public boolean check_values(){
        boolean isvalid =true;
//        if (agent_name==null || agent_name.equals("") ){
//            agents.setError("select an agent");
//            isvalid = false;
//        }
//        else
            if ((phone.getText().toString() ==null || phone.getText().toString().equals("")) || phone.getText().toString().length()<10 ){
            phone.setError("enter valid phone number");
            isvalid = false;
        }
        else if((pin.getText().toString() ==null || pin.getText().toString().equals("")) || pin.getText().toString().length() < 6){
            pin.setError("enter valid pin");
            isvalid = false;        }
        else if (transaction_id.getText().toString() == null || transaction_id.getText().toString().equals("")){
            transaction_id.setError("enter value");
            isvalid = false;        }
        else if (address.getText().toString() == null || address.getText().toString().equals("")){
            address.setError("enter value");
            isvalid = false;
        }
        else if (address.getText().toString().length()>88){
            Utility.show_toast("Address must be under 88 charecters", MainActivity.this);
            isvalid = false;
            }
        else if (checkCount(Utility.list)<=0){
            Utility.show_toast("Add items",this);
            popup.setError("Add items");
            isvalid = false;
        }
        else if (!isPinvalid){
            Utility.show_toast("Invalid Pin",this);
            isvalid = false;
        }
        else if (bank_str == null || bank_str.equals("")){
            Utility.show_toast("Select a bank",this);
            banks.setError("Select a bank");
            isvalid = false;
        }
        else if(deliveryChannel.equals("")){
            Utility.show_toast("Select a delivery channel",this);
            isvalid = false;
        }
        return isvalid;
    }


    public void load_agents(){
        progressBar.setVisibility(View.VISIBLE);
        progressbar_text.setText("loading agents");
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
                                  default_from_address =   response.getJSONObject(0).getString("from_address");
                                  from_address.setText(default_from_address);
                                     array = new JSONArray(agents_object.getString("agents"));
                                    Log.d("agentsarray",array.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

//                                    agents_array = new String[array.length()];
//                                for (int i=0; i<array.length(); i++) {
//                                    try {
//                                        JSONObject object = array.getJSONObject(i);
//                                        Log.d("name",object.getString("name"));
//                                        agents_array[i] = object.getString("name");
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                                 adapter =
//                                        new ArrayAdapter<>(
//                                                MainActivity.this,
//                                                R.layout.dropdown_menu_popup_item,
//                                                agents_array);


//                                agents.setAdapter(adapter);

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
                        Utility.show_toast("TimeoutError",MainActivity.this);
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
    public void load_products(){
        progressBar.setVisibility(View.VISIBLE);
        progressbar_text.setText("loading products");
        Volley.newRequestQueue(this).add(
                new JsonRequest<JSONArray>(Request.Method.GET, BASE_URL+"load_products.php", null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progressBar.setVisibility(View.GONE);
                                Log.d("resposnse",response.toString());
                                Log.d("agents",response.toString());
                                JSONArray array = null;
                                JSONArray products_array = response;

                                for (int i=0; i<products_array.length(); i++) {
                                    try {
                                        JSONObject object = products_array.getJSONObject(i);
                                        String product_id = object.getString("product_id");
                                        String product_name = object.getString("product_name");
                                        float product_weight = Float.parseFloat(object.getString("product_weight"));

                                        Utility.list.add(new product(product_id,product_name,0,product_weight,0));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }



                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d("agentslog",error.toString());
                    if (error.toString().contains("TimeoutError")){
//                        load_agents();
                        Utility.show_toast("TimeoutError",MainActivity.this);
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
    public void load_pinDetails(String pin){
        pinDetails.setText("");
        progressBar.setVisibility(View.VISIBLE);
        progressbar_text.setText("loading pin details");
        Volley.newRequestQueue(this).add(
                new JsonRequest<JSONArray>(Request.Method.GET, "https://api.postalpincode.in/pincode/"+pin, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progressBar.setVisibility(View.GONE);
                                Log.d("resposnse",response.toString());
                                JSONArray pindetails_array = response;
                                try {
                                     state =  pindetails_array.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0).getString("State");
                                     district =  pindetails_array.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0).getString("District");
                                    pinDetails.setText(state+", "+district+", "+district);
                                    Log.d("pinstate" , state);
                                    isPinvalid = true;
                                    pinDetails.setError(null);
                                    pinDetails.setTextColor(Color.GRAY);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    pinDetails.setText("invalid pin");
                                    pinDetails.setError("Invalid pin");
                                    pinDetails.setTextColor(Color.RED);
                                }



                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d("agentslog",error.toString());
                    if (error.toString().contains("TimeoutError")){

                        Utility.show_toast("TimeoutError",MainActivity.this);
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

    public void clear_fields(){

//        date.setEnabled(false);
//        date.clearFocus();
//        date.setClickable(false);
//        date.setFocusable(false);
//        date.setFocusableInTouchMode(false);

        Utility.list.clear();

        progressBar.setVisibility(View.GONE);
        progressbar_text.setVisibility(View.GONE);
        date_string = getCurrentDate();
        date.setText(date_string);

        address.setText("");
        address.setError(null);
        address.setEnabled(true);
        address.setFocusable(true);
        address.setClickable(true);
        address.setActivated(true);

        name.setText("");
        name.setError(null);
        name.setEnabled(true);
        name.setFocusable(true);
        name.setClickable(true);
        name.setActivated(true);


        pin.setText("");
        pin.setError(null);
        pin.setEnabled(true);
        pin.setFocusable(true);
        pin.setClickable(true);
        pin.setActivated(true);



        transaction_id.setText("");
        transaction_id.setError(null);
        transaction_id.setEnabled(true);
        transaction_id.setFocusable(true);
        transaction_id.setClickable(true);
        transaction_id.setActivated(true);

        from_address.setText(default_from_address);
        from_address.setEnabled(false);
        from_address.setFocusable(false);
        from_address.setClickable(false);
        from_address.setActivated(true);
        checkBox.setChecked(false);



        phone.setText("");
        phone.setError(null);
        phone.setEnabled(true);
        phone.setFocusable(true);
        phone.setClickable(true);
        phone.setActivated(true);
        phone.requestFocus();

        radioGroup.clearCheck();


        button.setText("submit");
        is_update = false;

        previous_section.setVisibility(View.INVISIBLE);
        load_products();

    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
//        show_toast("created");

        Intent shortcutIntent = new Intent(getApplicationContext(),
                MainActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "HelloWorldShortcut");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.mipmap.ic_launcher));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        getApplicationContext().sendBroadcast(addIntent);

    }
    public boolean clear_requests(){
         MySingleton.getInstance(this).getRequestQueue().cancelAll(this);
        progressBar.setVisibility(View.GONE);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (popUpClass != null) {

        } else {
            super.onBackPressed();
        }

    }
}











