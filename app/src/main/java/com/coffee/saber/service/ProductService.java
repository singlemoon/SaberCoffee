package com.coffee.saber.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.coffee.saber.model.Product;
import com.coffee.saber.ui.adapter.ProductAdapter;
import com.coffee.saber.utils.Global;
import com.coffee.saber.utils.HttpParser;
import com.coffee.saber.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Created by Simo on 2018/12/20.
 */
public class ProductService extends Service {

    private boolean runFlag = true;
    private OnGetProductResponseListener listener;

    private static final String TAG = "ProductService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return new ProductBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        runFlag = false;    // 解除绑定结束线程
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        runFlag = false;    // 解除绑定结束线程
    }

    public class ProductBinder extends Binder {
        public void startGetProduct(final OnGetProductResponseListener listener) {
            Log.i(TAG, "startGetProduct: ");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (runFlag) {
                        try {
                            Thread.sleep(5*60*1000); // 每隔五分钟获取一次产品
                            Log.i(TAG, "run: GetProduct");
                            Map<String, String> resMap = HttpParser.parseMapGet(Global.PRODUCT_LIST_URL);
                            int status = Integer.parseInt(resMap.get("status"));
                            if (listener != null) {
                                if (status == 1) {
                                    String productsJson = resMap.get("data");
                                    listener.onSuccess(productsJson);
                                } else {
                                    listener.onFailed(resMap.get("data"));
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    public interface OnGetProductResponseListener {
        void onSuccess(String productJson);
        void onFailed(String errMsg);
    }
}
