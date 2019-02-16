package com.coffee.saber.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.coffee.saber.R;
import com.coffee.saber.model.ShoppingCart;

import java.util.List;

/**
 * Created by Simo on 2018/12/12.
 */
public class ShoppingCartAdapter extends ArrayAdapter {
    private int resourceId;
    private List<ShoppingCart> shoppingCarts = null;
    private Context context = null;
    private OnItemBtnClickListener mListener;
    private int checkPosition = -1;

    public ShoppingCartAdapter(Context context, int resourceId, List<ShoppingCart> shoppingCarts, OnItemBtnClickListener listener) {
        super(context, resourceId, shoppingCarts);
        this.context = context;
        this.resourceId = resourceId;
        this.shoppingCarts = shoppingCarts;
        this.mListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ShoppingCart shoppingCart = shoppingCarts.get(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView productNameTv = (TextView) view.findViewById(R.id.product_name_tv);
        TextView describeTv = (TextView)view.findViewById(R.id.describe_tv);
        TextView productPriceTv = (TextView)view.findViewById(R.id.product_price_tv);
        CheckBox selRb = (CheckBox)view.findViewById(R.id.sel_rb);
        ImageView addBtn = (ImageView)view.findViewById(R.id.add_btn);
        TextView orderNumTv = (TextView)view.findViewById(R.id.order_num_tv);
        ImageView difBtn = (ImageView)view.findViewById(R.id.dif_btn);

        productNameTv.setText(shoppingCart.getProductName());
        describeTv.setText(shoppingCart.getDescribe());
        orderNumTv.setText(String.valueOf(shoppingCart.getNum()));
        String productPrice = "￥" + shoppingCart.getProductPrice();
        productPriceTv.setText(productPrice);
        if (position == checkPosition) {
            selRb.setChecked(true);
        }
        selRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (position == checkPosition) {
                        checkPosition = -1;
                    }
                    checkPosition = position;
                } else {
                    checkPosition = -1;
                }
                mListener.onSelRbClickListener(position, isChecked);
                ShoppingCartAdapter.this.notifyDataSetChanged();
            }
        });

        final int orderNum = Integer.parseInt(orderNumTv.getText().toString());
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddBtnClickListener(position, orderNum+1);
            }
        });

        difBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDifBtnClickListener(position, orderNum-1);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return shoppingCarts.size();
    }



    public interface OnItemBtnClickListener {
        void onAddBtnClickListener(int position, int order_num);
        void onDifBtnClickListener(int position, int order_num);
        void onSelRbClickListener(int position, boolean isChecked);
    }
}
