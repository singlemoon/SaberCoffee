package com.coffee.saber.ui.fragment.order;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.coffee.saber.R;
import com.coffee.saber.model.Order;
import com.coffee.saber.ui.adapter.OrderAdapter;
import com.coffee.saber.ui.fragment.BaseFragment;
import com.coffee.saber.utils.DisplayUtils;
import com.coffee.saber.utils.T;
import com.coffee.saber.utils.TestData;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Simo on 2018/12/11.
 */
public class OrderFragment extends BaseFragment {
    private View mOrderView = null;
    private TextView title = null;
    private ListView orderLV = null;

    private List<Order> orders = null;
    private OrderAdapter mAdapter = null;
    private OrderHandler mHandler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mOrderView == null) {
            mOrderView = inflater.inflate(R.layout.fragment_order, null);
        }
        title = (TextView) mOrderView.findViewById(R.id.title_text);
        orderLV = (ListView) mOrderView.findViewById(R.id.order_lv);

        return mOrderView;
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
        orders = new ArrayList<>();
        mHandler = new OrderHandler(this);
        getOrders();
    }

    private void initView() {
        title.setText("订单");
        mAdapter = new OrderAdapter(mActivity, R.layout.item_order, orders, new OrderAdapter.OnItemBtnClickListener() {
            @Override
            public void onItemClick(int position) {
                T.showShort(mActivity, "查看订单详情");
            }

            @Override
            public void onOrderAgainBtnClick(int position) {
                T.showShort(mActivity, "再来一单");
            }
        });
        orderLV.setAdapter(mAdapter);
        orderLV.setDividerHeight(DisplayUtils.dp2px(mActivity, 5));
    }

    private void getOrders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Map<String,String> result = HttpParser.parseMapGet(Global.Order_LIST_URL);
                try {
                    Thread.sleep(1);
                    List<Order> orderList = TestData.getOrders();
                    orders.clear();
                    orders.addAll(orderList);
                    mHandler.sendEmptyMessage(ORDER_LIST);
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
    private static final int ORDER_LIST = 123458;

    static class OrderHandler extends Handler {
        OrderFragment mFragment = null;
        Activity activity = null;

        public OrderHandler(OrderFragment fragment) {
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
                    } else {
                        T.showShort(activity, "下单失败");
                    }
                    break;
                case ORDER_LIST:
                    mFragment.updateList();
                    break;

            }
        }
    }
}
