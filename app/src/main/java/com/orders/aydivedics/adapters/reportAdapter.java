package com.orders.aydivedics.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orders.aydivedics.R;
import com.orders.aydivedics.Utility;
import com.orders.aydivedics.models.reportModel;
import com.orders.aydivedics.orderReportActivity;

import java.util.ArrayList;

public class reportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<reportModel> dataSet;
    public Context mContext;
    public int curPos;


    public class reportViewHolder extends RecyclerView.ViewHolder {
        TextView prodName;
        EditText prepaidOrders,prepaidQty,codOrders,codQty;

        public reportViewHolder(View itemView) {
            super(itemView);

            this.prodName = (TextView) itemView.findViewById(R.id.prod_name);
            this.prepaidOrders = itemView.findViewById(R.id.prepaidOrders);
            this.prepaidQty = itemView.findViewById(R.id.prepaidPieces);
            this.codOrders = itemView.findViewById(R.id.codOrders);
            this.codQty = itemView.findViewById(R.id.codPieces);


//            this.unit_price.setFilters(new InputFilter[] {new reportAdapter.DecimalDigitsInputFilter(5,2)});

//            this.plus = (Button) itemView.findViewById(R.id.plus);
//            this.minus = (Button) itemView.findViewById(R.id.minus);
//            this.add_button = (Button) itemView.findViewById(R.id.add_button);


        }

    }

    public reportAdapter(ArrayList<reportModel> reportList, orderReportActivity orderReportActivity) {
        this.dataSet = reportList;
        this.mContext = orderReportActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item_layout_adapter, parent, false);

        return new reportAdapter.reportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,  int position) {
        reportModel object = dataSet.get(position);
        ((reportViewHolder) holder).prodName.setText(object.getProductName());
//        Log.d("currentPos", String.valueOf(holder.);


        ((reportViewHolder) holder).prepaidOrders.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>=1){
                    Utility.reportlist.get(position).prepaidOrders=(Integer.parseInt(charSequence.toString()));
                    Log.d("clickedPos", Utility.reportlist.get(position).getProductName()+" "+ String.valueOf( Utility.reportlist.get(position).getPrepaidOrders()));

                }
                            }

            @Override
            public void afterTextChanged(Editable editable) {
//                    dataSet.get(ada).setPrepaidOrders(Integer.parseInt(editable.toString()) );
            }
        });
        ((reportViewHolder)holder).prepaidQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>=1){
                Utility.reportlist.get(position).prepaidOrderPieces=(Integer.parseInt(charSequence.toString()));

            }}

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((reportViewHolder)holder).codOrders.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>=1){
                Utility.reportlist.get(position).codOrders=(Integer.parseInt(charSequence.toString()));

            }}

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((reportViewHolder)holder).codQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>=1){
                Utility.reportlist.get(position).codOrderPieces=(Integer.parseInt(charSequence.toString()));

            }}

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
