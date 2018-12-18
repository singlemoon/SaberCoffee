package com.coffee.saber.ui.fragment.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coffee.saber.R;
import com.coffee.saber.model.Order;
import com.coffee.saber.model.Product;
import com.coffee.saber.ui.adapter.ProductAdapter;
import com.coffee.saber.ui.fragment.BaseFragment;
import com.coffee.saber.utils.FormatUtils;
import com.coffee.saber.utils.Global;
import com.coffee.saber.utils.HttpParser;
import com.coffee.saber.utils.SPPrivateUtils;
import com.coffee.saber.utils.T;
import com.coffee.saber.utils.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Simo on 2018/12/11.
 */
public class ProductFragment extends BaseFragment {
    private View mProductView = null;
    private TextView title = null;
    private ListView productLV = null;

    private List<Product> products = null;
    private ProductHandler mHandler = null;
    private ArrayAdapter mAdapter = null;
    private AlertDialog buyDialog = null;

    private static final String TAG = "ProductFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mProductView == null) {
            mProductView = inflater.inflate(R.layout.fragment_product, null);
        }

        title = (TextView) mProductView.findViewById(R.id.title_text);
        productLV = (ListView) mProductView.findViewById(R.id.product_lv);

        return mProductView;
    }

    /**
     * 该方法在 Activity 创建后调用，可能会用到 Activity 的操作最好都在这里进行
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initValue();
        initView();
    }

    private void initValue() {
        products = new ArrayList<>();
        mHandler = new ProductHandler(this);
        getProducts();
    }

    private void initView() {
        title.setText("商品");
//        products = TestData.getProducts();
        mAdapter = new ProductAdapter(mActivity, R.layout.item_product, products, new ProductAdapter.OnItemBtnClickListener() {
            @Override
            public void onAddShoppingCartBtnClick(int position) {
                addShoppingCart();
            }

            @Override
            public void onBuyBtnClick(int position) {
                Product product = products.get(position);
                showAlert(product);
            }
        });
        productLV.setAdapter(mAdapter);
    }

    private void showAlert(final Product product) {
        buyDialog = new AlertDialog.Builder(mActivity).create();

        final View alertView = LayoutInflater.from(mActivity).inflate(R.layout.alert_buy, null);
        final TextView sumPriceTV = alertView.findViewById(R.id.sum_price_tv);
        final TextView orderNumTV = alertView.findViewById(R.id.order_num_tv);
        ImageView addBtn = alertView.findViewById(R.id.add_btn);
        ImageView difBtn = alertView.findViewById(R.id.dif_btn);
        Button addOrderBtn = alertView.findViewById(R.id.add_order_ben);

        String sumPrice = "￥" + product.getPrice();
        sumPriceTV.setText(sumPrice);
        orderNumTV.setText(String.valueOf(1));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldNum = Integer.parseInt(orderNumTV.getText().toString());
                int newNum = oldNum+1;
                float newPrice = product.getPrice() * newNum;
                orderNumTV.setText(String.valueOf(newNum));
                String sumPrice = "￥"+ FormatUtils.get2Decimal(newPrice);
                sumPriceTV.setText(sumPrice);
            }
        });
        difBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldNum = Integer.parseInt(orderNumTV.getText().toString());
                int newNum = oldNum-1;
                if (newNum > 0) {
                    float newPrice = product.getPrice() * newNum;
                    orderNumTV.setText(String.valueOf(newNum));
                    String sumPrice = "￥"+ FormatUtils.get2Decimal(newPrice);
                    sumPriceTV.setText(sumPrice);
                } else {
                    T.showShort(mActivity, "少侠饶命啊，实在不能再少了");
                }
            }
        });

        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orderNum = Integer.valueOf(orderNumTV.getText().toString());
                buy(product,orderNum);

            }
        });
        buyDialog.show();
        buyDialog.setContentView(alertView);
    }

    private void getProducts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Map<String,String> result = HttpParser.parseMapGet(Global.PRODUCT_LIST_URL);
                try {
                    Thread.sleep(1);
                    List<Product> productList = TestData.getProducts();
                    products.clear();
                    products.addAll(productList);
                    mHandler.sendEmptyMessage(PRODUCT_LIST);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addShoppingCart() {

    }

    private void buy(final Product product, final int num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                    int userId = SPPrivateUtils.getInt(mActivity, "user_id", 0);
                    int productId = product.getId();
                    Order order = new Order(userId, productId, num);
//                    Map<String, String> map = HttpParser.parseMapPost(Global.BUY_URL, order.toJson());
//                    int status = Integer.parseInt(map.get("status"));
                    int status = 1;
                    Message msg = new Message();
                    msg.what = BUY;
                    msg.arg1 = status;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateList() {
        mAdapter.notifyDataSetChanged();
    }

    private static final int SHOPPING_CART = 123456;
    private static final int BUY = 123457;
    private static final int PRODUCT_LIST = 123458;

    static class ProductHandler extends Handler {
        ProductFragment mFragment = null;
        Activity activity = null;

        public ProductHandler(ProductFragment fragment) {
            this.mFragment = fragment;
            this.activity = mFragment.getActivity();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOPPING_CART:
                    break;
                case BUY:
                    if (1 == msg.arg1) {
                        T.showShort(activity, "下单成功");
                        mFragment.buyDialog.dismiss();
                    } else {
                        T.showShort(activity, "下单失败");
                    }
                    break;
                case PRODUCT_LIST:
                    mFragment.updateList();
                    break;

            }
        }
    }
}
