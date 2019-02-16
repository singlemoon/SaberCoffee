package com.coffee.saber.ui.fragment.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.coffee.saber.model.ShoppingCart;
import com.coffee.saber.service.ProductService;
import com.coffee.saber.ui.adapter.ProductAdapter;
import com.coffee.saber.ui.fragment.BaseFragment;
import com.coffee.saber.ui.fragment.shopping_cart.ShoppingCartFragment;
import com.coffee.saber.utils.FormatUtils;
import com.coffee.saber.utils.Global;
import com.coffee.saber.utils.HttpParser;
import com.coffee.saber.utils.JsonUtils;
import com.coffee.saber.utils.SPPrivateUtils;
import com.coffee.saber.utils.T;
import com.google.gson.reflect.TypeToken;

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
    private ProductService.ProductBinder productBinder = null;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: ");
            productBinder = (ProductService.ProductBinder) service;
            if (null != productBinder) {
                productBinder.startGetProduct(new ProductService.OnGetProductResponseListener() {
                    @Override
                    public void onSuccess(String productJson) {
                        Message msg = new Message();
                        msg.what = PRODUCT_LIST;
                        msg.arg1 = 1;
                        msg.obj = productJson;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onFailed(String errMsg) {
                        Message msg = new Message();
                        msg.what = PRODUCT_LIST;
                        msg.arg1 = 0;
                        msg.obj = errMsg;
                        mHandler.sendMessage(msg);
                    }
                });
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: ");
            productBinder = null;
        }
    };

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
        mActivity.getApplicationContext().bindService(new Intent(mActivity, ProductService.class), mConn, Context.BIND_AUTO_CREATE);
        getProducts();
    }

    private void initView() {
        title.setText("商品");
//        products = TestData.getProducts();
        mAdapter = new ProductAdapter(mActivity, R.layout.item_product, products, new ProductAdapter.OnItemBtnClickListener() {
            @Override
            public void onAddShoppingCartBtnClick(int position) {
                addShoppingCart(products.get(position), 1);
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
        final TextView productNameTv = (TextView) alertView.findViewById(R.id.product_name_tv);
        final TextView describeTv = (TextView) alertView.findViewById(R.id.describe_tv);
        final TextView sumPriceTV = (TextView) alertView.findViewById(R.id.sum_price_tv);
        final TextView orderNumTV = (TextView) alertView.findViewById(R.id.order_num_tv);
        ImageView addBtn = (ImageView) alertView.findViewById(R.id.add_btn);
        ImageView difBtn = (ImageView) alertView.findViewById(R.id.dif_btn);
        Button addOrderBtn = (Button) alertView.findViewById(R.id.add_order_ben);

        String sumPrice = "￥" + product.getPrice();
        productNameTv.setText(product.getName());
        describeTv.setText(product.getDescribe());
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
                Map<String, String> result = HttpParser.parseMapGet(Global.PRODUCT_LIST_URL);
                int status = Integer.parseInt(result.get("status"));
                Message msg = new Message();
                msg.what = PRODUCT_LIST;
                msg.arg1 = status;
                msg.obj = result.get("data");
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private void addShoppingCart(final Product product, final int num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                    int userId = SPPrivateUtils.getInt(mActivity, "user_id", 0);
                    int productId = product.getId();
                    ShoppingCart shoppingCart = new ShoppingCart(userId, productId, num);
                    Map<String, String> map = HttpParser.parseMapPost(Global.ADD_SHOPPING_CART_URL,  "data="+shoppingCart.toJson());
                    int status = Integer.parseInt(map.get("status"));
//                    int status = 1;
                    Message msg = new Message();
                    msg.what = SHOPPING_CART;
                    msg.arg1 = status;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                    Map<String, String> map = HttpParser.parseMapPost(Global.BUY_URL,  "data="+order.toJson());
                    int status = Integer.parseInt(map.get("status"));
//                    int status = 1;
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

    private void updateList(List<Product> list) {
        products.clear();
        products.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        mActivity.getApplicationContext().unbindService(mConn);
        super.onDestroyView();
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
                    if (1 == msg.arg1) {
                        T.showShort(activity, "加入购物车成功");
                    } else {
                        T.showShort(activity, "加入购物车失败");
                    }
                    break;
                case BUY:
                    if (1 == msg.arg1) {
                        T.showShort(activity, "下单成功");
                        mFragment.buyDialog.dismiss();
                        // 刷新订单列表
                        activity.sendBroadcast(new Intent("com.coffee.refresh"));
                    } else {
                        T.showShort(activity, "下单失败");
                    }
                    break;
                case PRODUCT_LIST:
                    if (msg.arg1 == 1) {
                        String productsJson = (String) msg.obj;
                        Log.i(TAG, "handleMessage: " + productsJson);
                        List<Product> productList = JsonUtils.fromJson(productsJson, new TypeToken<List<Product>>(){}.getType());
                        if (null == productList) {
                            Log.i(TAG, "handleMessage: Json 解析失败");
                        } else {
                            for(Product product : productList) {
                                Log.i(TAG, "handleMessage: Product = "+product.toString());
                            }
                        }
                        mFragment.updateList(productList);
                    } else {
                        T.showShort(activity, "产品获取失败");
                    }
                    break;

            }
        }
    }
}
