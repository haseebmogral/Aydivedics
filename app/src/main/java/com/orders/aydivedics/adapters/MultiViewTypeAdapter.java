package com.orders.aydivedics.adapters;

import android.content.Context;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.orders.aydivedics.R;
import com.orders.aydivedics.Utility;
import com.orders.aydivedics.models.product;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<product> dataSet;
    public Context mContext;
    private int unit_count;
    TextView textView;




    public class AudioTypeViewHolder extends RecyclerView.ViewHolder {
        TextView txtType,count_text;
        EditText unit_price;
        Button plus,minus,add_button;

        public AudioTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.prod_name);
            this.unit_price = itemView.findViewById(R.id.unit_price);
            this.count_text = itemView.findViewById(R.id.count);
            this.unit_price.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

            this.plus = (Button) itemView.findViewById(R.id.plus);
            this.minus = (Button) itemView.findViewById(R.id.minus);
            this.add_button = (Button) itemView.findViewById(R.id.add_button);


        }

    }

    public MultiViewTypeAdapter(ArrayList<product> data, Context context, TextView total_amount_text) {
        this.dataSet = data;
        this.mContext = context;
        this.textView = total_amount_text;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view, parent, false);
        return new AudioTypeViewHolder(view);


    }




    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        product object = dataSet.get(listPosition);


                    ((AudioTypeViewHolder) holder).txtType.setText(object.getProd_name());
                    ((AudioTypeViewHolder) holder).count_text.setText(String.valueOf(object.getItem_count()));
                    if (object.getProd_price()==0){
                        ((AudioTypeViewHolder) holder).unit_price.setText("");
                    }
                    else{
                        ((AudioTypeViewHolder) holder).unit_price.setText(String.valueOf(object.getProd_price()));
                    }

                    if (object.prod_price>0){
                        ((AudioTypeViewHolder) holder).add_button.setVisibility(View.GONE);
                        ((AudioTypeViewHolder) holder).unit_price.setText(String.valueOf(object.getProd_price() ));
                        textView.setText(String.valueOf(calculate_total(dataSet)));
                    }


                    ((AudioTypeViewHolder) holder).plus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            object.item_count = object.item_count+1;
                            object.product_total = object.prod_price * object.item_count;
                            ((AudioTypeViewHolder) holder).count_text.setText(String.valueOf(object.item_count));
                            textView.setText(String.valueOf(calculate_total(dataSet)));

                        }
                    });
                    ((AudioTypeViewHolder) holder).minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (object.item_count == 0){
                                ((AudioTypeViewHolder) holder).add_button.setVisibility(View.VISIBLE);
                            }
                            else{
                                object.item_count = object.item_count-1;
                                object.product_total = object.prod_price * object.item_count;
                                ((AudioTypeViewHolder) holder).count_text.setText(String.valueOf(object.item_count));
                                textView.setText(String.valueOf(calculate_total(dataSet)));
                                if (object.item_count == 0){
                                    ((AudioTypeViewHolder) holder).add_button.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    });
                    ((AudioTypeViewHolder) holder).add_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (object.prod_price==0){
                                ((AudioTypeViewHolder) holder).unit_price.setError("enter amount");
                            }
                            else{
                                view.setVisibility(View.GONE);
                                object.item_count = object.item_count+1;
                                object.product_total = object.prod_price * object.item_count;
                                ((AudioTypeViewHolder) holder).count_text.setText(String.valueOf(object.item_count));
                                textView.setText(String.valueOf(calculate_total(dataSet)));
                            }

                        }
                    });

        ((AudioTypeViewHolder) holder).unit_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable==null || editable.toString().equals("")){

                }
                else{
                    object.prod_price= Float.parseFloat(editable.toString());
                    textView.setText(String.valueOf(calculate_total(dataSet)));
                }


            }
        });

            }

    @Override
    public int getItemCount() {
         return dataSet.size();
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

    public float calculate_total(ArrayList dataSet){
        float total = 0;
        for (product product : Utility.list){
            total = total + product.prod_price*product.item_count;
        }

        return total;
    }

}






