package com.coffee.saber.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.coffee.saber.R;
import com.coffee.saber.model.Order;

import java.util.List;

/**
 * Created by Simo on 2018/12/12.
 */
public class OrderAdapter extends ArrayAdapter {
    private int resourceId;
    private List<Order> orders = null;
    private Context context = null;
    private OnItemBtnClickListener mListener;

    public OrderAdapter(Context context, int resourceId, List<Order> orders, OnItemBtnClickListener listener) {
        super(context, resourceId, orders);
        this.context = context;
        this.resourceId = resourceId;
        this.orders = orders;
        this.mListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Order order = orders.get(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

//        LinearLayout orderItemLL = view.findViewById(R.id.order_item_ll);
        Button confirmBtn = view.findViewById(R.id.confirm_btn);

//        orderItemLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onItemClick(position);
//            }
//        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirmBtnClick(position);
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return orders.size();
    }



    public interface OnItemBtnClickListener {
        void onItemClick(int position);
        void onConfirmBtnClick(int position);
    }
}
