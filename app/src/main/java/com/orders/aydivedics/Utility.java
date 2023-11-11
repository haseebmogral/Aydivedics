package com.orders.aydivedics;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orders.aydivedics.models.product;
import com.orders.aydivedics.models.reportModel;

import org.json.JSONArray;

import java.util.ArrayList;

public class Utility {
    public static ArrayList<product> list= new ArrayList();
    public static ArrayList<reportModel> reportlist= new ArrayList();
    public static String BASE_URL = "https://orders.aydivedics.com/";
//    public static String BASE_URL = "https://192.168.1.83/ftune/";
    public static float total_amount = 0;
    public static JSONArray cart;
    public static Activity activity;
    public static void show_toast(String message, Context context){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        TextView textView = new TextView(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.your_custom_layout, null);
        textView = (TextView)view.findViewById(R.id.toast_message);
        textView.setText(message);


        toast.setView(view);
        toast.setGravity(Gravity.CENTER| Gravity.TOP, 0, 150);
        toast.show();
    }


}
