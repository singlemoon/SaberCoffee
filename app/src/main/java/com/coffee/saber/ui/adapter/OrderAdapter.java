package com.coffee.saber.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coffee.saber.R;
import com.coffee.saber.model.Order;
import com.coffee.saber.utils.FormatUtils;

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
        TextView orderCodeTv = view.findViewById(R.id.order_code_tv);
        TextView statusTv = view.findViewById(R.id.status_tv);
        TextView productNameTv = view.findViewById(R.id.product_name_tv);
        TextView orderNumTv = view.findViewById(R.id.order_num_tv);
        TextView sumPriceTv = view.findViewById(R.id.sum_price_tv);
        TextView createTime = view.findViewById(R.id.create_time_tv);

        String statusText = "";
        if (order.getStatus() == 0) {
            statusText = "待送达";
        } else {
            statusText = "已完成";
        }

        orderCodeTv.setText(order.getOrderCode());
        statusTv.setText(statusText);
        productNameTv.setText(order.getProductName());
        orderNumTv.setText("数量："+order.getNum());
        sumPriceTv.setText("￥"+order.getProductPrice()*order.getNum());
        createTime.setText(FormatUtils.stampToDate(order.getCreateTime()));

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
