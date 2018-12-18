package com.coffee.saber.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.coffee.saber.R;
import com.coffee.saber.model.Product;
import com.coffee.saber.utils.FormatUtils;

import java.util.List;

/**
 * Created by Simo on 2018/12/12.
 */
public class ProductAdapter extends ArrayAdapter {
    private int resourceId;
    private List<Product> products = null;
    private Context context = null;
    private OnItemBtnClickListener mListener;

    public ProductAdapter(Context context, int resourceId, List<Product> products, OnItemBtnClickListener listener) {
        super(context, resourceId, products);
        this.context = context;
        this.resourceId = resourceId;
        this.products = products;
        this.mListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Product product = products.get(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView productNameTv = (TextView) view.findViewById(R.id.product_name_tv);
        TextView priceTv = (TextView) view.findViewById(R.id.price_tv);
        TextView describeTv = (TextView) view.findViewById(R.id.describe_tv);
        Button addShoppingCartBtn = view.findViewById(R.id.add_shopping_cart_btn);
        Button buyBtn = view.findViewById(R.id.buy_btn);

        productNameTv.setText(product.getName());
        describeTv.setText(product.getDescribe());
        String priceText = "￥" + product.getPrice();
        priceTv.setText(priceText);
        addShoppingCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddShoppingCartBtnClick(position);
            }
        });
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBuyBtnClick(position);
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return products.size();
    }



    public interface OnItemBtnClickListener {
        void onAddShoppingCartBtnClick(int position);
        void onBuyBtnClick(int position);
    }
}
