package com.coffee.saber.wxapi;

import android.app.Activity;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by Simo on 2019/1/4.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    @Override
    public void onReq(BaseReq baseReq) {
        Log.i(TAG, "onReq: ");
    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}
