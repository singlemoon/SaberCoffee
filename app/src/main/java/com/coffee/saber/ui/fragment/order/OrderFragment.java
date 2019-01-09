package com.coffee.saber.ui.fragment.order;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
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
import java.util.jar.Pack200;


/**
 * Created by Simo on 2018/12/11.
 */
public class OrderFragment extends BaseFragment implements View.OnClickListener {
    private View mOrderView = null;
    private TextView title = null;
    private ListView orderLV = null;
    private LinearLayout orderAllLl = null;
    private LinearLayout orderNotCompleteLl = null;
    private LinearLayout orderCompleteLl = null;

    private List<Order> orders = null;  // 用来存储列表显示数据
    private List<Order> mOrders = null; // 用来存储列表源数据
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
        orderAllLl = (LinearLayout) mOrderView.findViewById(R.id.order_all_ll);
        orderNotCompleteLl = (LinearLayout) mOrderView.findViewById(R.id.order_not_complete_ll);
        orderCompleteLl = (LinearLayout) mOrderView.findViewById(R.id.order_complete_ll);

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
        mOrders = new ArrayList<>();
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
            public void onConfirmBtnClick(int position) {
                T.showShort(mActivity, "确认收货");
            }
        });
        orderLV.setAdapter(mAdapter);
        orderLV.setDividerHeight(DisplayUtils.dp2px(mActivity, 5));

        orderAllLl.setOnClickListener(this);
        orderNotCompleteLl.setOnClickListener(this);
        orderCompleteLl.setOnClickListener(this);
        topTabSelect(true,false,false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_all_ll:
                topTabSelect(true,false,false);
                changeList(2);
                break;
            case R.id.order_not_complete_ll:
                topTabSelect(false,true,false);
                changeList(0);
                break;
            case R.id.order_complete_ll:
                topTabSelect(false,false,true);
                changeList(1);
                break;
            default:
                break;
        }
        listAnim();
    }
    // 修改顶部tab样式
    private void topTabSelect(boolean order_all_ll, boolean order_not_complete_ll, boolean order_complete_ll) {
        orderAllLl.setSelected(order_all_ll);
        orderNotCompleteLl.setSelected(order_not_complete_ll);
        orderCompleteLl.setSelected(order_complete_ll);
    }

    private void changeList(int type) {
        orders.clear();
        if (type == 2) {
            orders.addAll(mOrders);
            for (Order order :
                    orders) {
                Log.i("changeList", "type = " + type + "changeList: " + order.toString());
            }
            updateList();
            return;
        }
        for (int i = 0; i < mOrders.size(); i++) {
            Order order = mOrders.get(i);
            if (order.getStatus() == type) {
                orders.add(order);
            }
        }
        for (Order order :
                orders) {
            Log.i("changeList", "type = " + type + "changeList: " + order.toString());
        }
        updateList();
    }

    private void listAnim() {
        Animation listAnim = AnimationUtils.loadAnimation(mActivity, R.anim.list_anim);
        orderLV.startAnimation(listAnim);
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
                    mOrders.clear();
                    mOrders.addAll(orderList);
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
