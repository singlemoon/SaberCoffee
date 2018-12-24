package com.coffee.saber.ui.fragment.shopping_cart;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coffee.saber.R;
import com.coffee.saber.model.Order;
import com.coffee.saber.model.Product;
import com.coffee.saber.model.ShoppingCart;
import com.coffee.saber.ui.adapter.ShoppingCartAdapter;
import com.coffee.saber.ui.fragment.BaseFragment;
import com.coffee.saber.utils.DisplayUtils;
import com.coffee.saber.utils.Global;
import com.coffee.saber.utils.HttpParser;
import com.coffee.saber.utils.JsonUtils;
import com.coffee.saber.utils.SPPrivateUtils;
import com.coffee.saber.utils.T;
import com.coffee.saber.utils.TestData;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Simo on 2018/12/11.
 */
public class ShoppingCartFragment extends BaseFragment {
    private View mProductView = null;
    private TextView title = null;
    private ListView shoppingCartLV = null;
    private TextView sumPriceTv = null;
    private Button buyBtn = null;

    private List<ShoppingCart> shoppingCarts = null;
    private ShoppingCartAdapter mAdapter = null;
    private OrderHandler mHandler = null;
    private int checkPosition = -1;
    private static final String TAG = "ShoppingCartFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mProductView == null) {
            mProductView = inflater.inflate(R.layout.fragment_shopping_cart, null);
        }

        title = (TextView) mProductView.findViewById(R.id.title_text);
        shoppingCartLV = (ListView) mProductView.findViewById(R.id.shopping_cart_lv);
        sumPriceTv = (TextView) mProductView.findViewById(R.id.sum_price_tv);
        buyBtn = (Button) mProductView.findViewById(R.id.buy_btn);

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
        shoppingCarts = new ArrayList<>();
        mHandler = new OrderHandler(this);
        getShoppingCarts();
    }

    private void initView() {
        title.setText("购物车");
        sumPriceTv.setText("￥0");
        mAdapter = new ShoppingCartAdapter(mActivity, R.layout.item_shopping_cart, shoppingCarts, new ShoppingCartAdapter.OnItemBtnClickListener() {

            @Override
            public void onAddBtnClickListener(int position, int order_num) {
                shoppingCarts.get(position).setNum(order_num);
                if (checkPosition == position) {
                    String sumPrice = "￥" + order_num * shoppingCarts.get(position).getProductPrice();
                    sumPriceTv.setText(sumPrice);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDifBtnClickListener(int position, int order_num) {
                if (order_num >= 0) {
                    shoppingCarts.get(position).setNum(order_num);
                    if (checkPosition == position) {
                        String sumPrice = "￥" + order_num * shoppingCarts.get(position).getProductPrice();
                        sumPriceTv.setText(sumPrice);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    T.showShort(mActivity,"这位客官行行好，实在是不能再减了");
                }
            }

            @Override
            public void onSelRbClickListener(int position, boolean isChecked) {
                int sumPrice = 0;
                if (isChecked) {
                    checkPosition = position;
                    sumPrice = shoppingCarts.get(position).getProductPrice() * shoppingCarts.get(position).getNum();
                } else {
                    checkPosition = -1;
                    sumPrice = 0;
                }
                sumPriceTv.setText("￥" + sumPrice);
            }
        });
        shoppingCartLV.setAdapter(mAdapter);
        shoppingCartLV.setDividerHeight(DisplayUtils.dp2px(mActivity, 1));

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPosition > 0) {
                    ShoppingCart shoppingCart = shoppingCarts.get(checkPosition);
                    Order order = new Order(SPPrivateUtils.getInt(mActivity, "user_id", 0), shoppingCart.getProductId(), shoppingCart.getNum());
                    buy(order, shoppingCart.getId());
                } else {
                    T.showShort(mActivity, "没有选择任何商品");
                }
            }
        });
    }

    private void buy(final Order order, final int shoppingCartId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(1);
                    Map<String, String> map = HttpParser.parseMapPost(Global.BUY_URL,
                            "data=" + order.toJson()+"&shopping_cart_id="+shoppingCartId);
                    int status = Integer.parseInt(map.get("status"));
//                    int status = 1;
                    Message msg = new Message();
                    msg.what = BUY;
                    msg.arg1 = status;
                    mHandler.sendMessage(msg);

//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }).start();
    }

    private void getShoppingCarts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> map = HttpParser.parseMapGet(Global.SHOPPING_CART_LIST+"&user_id="+SPPrivateUtils.get(mActivity,"user_id",0));
                int status = Integer.parseInt(map.get("status"));
//                    int status = 1;
                Message msg = new Message();
                msg.what = SHOPPING_CART;
                msg.arg1 = status;
                msg.obj = map.get("data");
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private void updateList(List<ShoppingCart> shoppingCartList) {
        shoppingCarts.clear();
        shoppingCarts.addAll(shoppingCartList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    private static final int SHOPPING_CART = 123456;
    private static final int BUY = 123457;

    static class OrderHandler extends Handler {
        ShoppingCartFragment mFragment = null;
        Activity activity = null;

        public OrderHandler(ShoppingCartFragment fragment) {
            this.mFragment = fragment;
            this.activity = mFragment.getActivity();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOPPING_CART:
                    if (msg.arg1 == 1) {
                        String shoppingCartJson = (String) msg.obj;
                        Log.i(TAG, "handleMessage: " + shoppingCartJson);
                        List<ShoppingCart> shoppingCartList = JsonUtils.fromJson(shoppingCartJson, new TypeToken<List<ShoppingCart>>(){}.getType());
                        if (null == shoppingCartList) {
                            Log.i(TAG, "handleMessage: Json 解析失败");
                        } else {
                            for(ShoppingCart shoppingCart : shoppingCartList) {
                                Log.i(TAG, "handleMessage: shoppingCart = "+shoppingCart.toString());
                            }
                        }
                        mFragment.updateList(shoppingCartList);
                    } else {
                        T.showShort(activity, "产品获取失败");
                    }
                    break;
                case BUY:
                    if (1 == msg.arg1) {
                        T.showShort(activity, "下单成功");
                    } else {
                        T.showShort(activity, "下单失败");
                    }
                    break;

            }
        }
    }
}
