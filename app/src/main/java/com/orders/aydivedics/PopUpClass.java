package com.orders.aydivedics;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.orders.aydivedics.adapters.MultiViewTypeAdapter;

import java.util.ArrayList;

public class PopUpClass {
    int item_count = 0;
    TextView count;
    Context context;
    ArrayList list;
    TextView total_amount_text;
    MultiViewTypeAdapter adapter;

    public PopUpClass(MainActivity mainActivity) {
        context = mainActivity;

    }

    //PopupWindow display method

    public void showPopupWindow(final View view) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up_layout, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;


        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        total_amount_text = popupView.findViewById(R.id.total_amount_text);


        adapter = new MultiViewTypeAdapter(Utility. list,context,total_amount_text);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
//
        RecyclerView mRecyclerView = popupView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);


        Button buttonEdit = popupView.findViewById(R.id.messageButton);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String data = gson.toJson(Utility.list);
                JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                Log.d("cart", jsonArray.toString());
                Log.d("cartstring", data);

                for (com.orders.aydivedics.models.product product : Utility.list){
                    Log.d("cartitems",product.prod_name+"-"+product.item_count+String.valueOf(product.prod_price)+":"+String.valueOf(product.getItem_count()*product.prod_price));
                }
                //As an example, display the message
                popupWindow.dismiss();
            }
        });



        //Handler for clicking on the inactive zone of the window


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
//                popupWindow.dismiss();
                return true;
            }
        });
    }



//    public ArrayList get_data(){
//
//        Utility.list.add(new product("001","facetune cream",0,0));
//        Utility.list.add(new product("001","facetune dress",0,0));
//        Utility.list.add(new product("001","facetune pants",0,0));
//        Utility.list.add(new product("001","facetune shirt",0,0));
//        Utility.list.add(new product("001","facetune cream",0,0));
//
//        return Utility. list;
//    }

}
