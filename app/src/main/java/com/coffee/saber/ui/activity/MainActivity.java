package com.coffee.saber.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coffee.saber.R;
import com.coffee.saber.ui.adapter.CoffeePagerAdapter;
import com.coffee.saber.ui.fragment.mine.MineFragment;
import com.coffee.saber.ui.fragment.order.OrderFragment;
import com.coffee.saber.ui.fragment.product.ProductFragment;
import com.coffee.saber.ui.fragment.shopping_cart.ShoppingCartFragment;
import com.coffee.saber.utils.T;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,IWXAPIEventHandler {
    private static final String TAG = "MainActivity.TAG";
    private LinearLayout productLL;
    private LinearLayout orderLL;
    private LinearLayout shoppingCartLL;
    private LinearLayout mineLL;
    private ImageView share;
    private ViewPager mViewPager;
    private CoffeePagerAdapter mAdapter;
    private FragmentManager mFragmentManager;
    // 这里替换为你的应用从官方网站申请到的合法appID
    private static final String APP_ID = "wx42e0891a41900bc3";
    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    List<Fragment> mFragmentList = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        initFragmentList();
        mAdapter = new CoffeePagerAdapter(mFragmentManager,mFragmentList);
        initView();
        initViewPager();
        regToWx();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initViewPager() {
        mViewPager.addOnPageChangeListener(new ViewPagetOnPagerChangedLisenter());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        updateBottomLinearLayoutSelect(true,false,false,false);
    }

    public void initFragmentList() {
        Fragment productFragment = new ProductFragment();
        Fragment orderFragment = new OrderFragment();
        Fragment shoppingCartFragment = new ShoppingCartFragment();
        Fragment mineFragment = new MineFragment();
        mFragmentList.add(productFragment);
        mFragmentList.add(orderFragment);
        mFragmentList.add(shoppingCartFragment);
        mFragmentList.add(mineFragment);
    }

    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.main_pager);
        productLL = (LinearLayout) findViewById(R.id.product_ll);
        productLL.setOnClickListener(this);
        orderLL = (LinearLayout) findViewById(R.id.order_ll);
        orderLL.setOnClickListener(this);
        shoppingCartLL = (LinearLayout) findViewById(R.id.shopping_cart_ll);
        shoppingCartLL.setOnClickListener(this);
        orderLL.setOnClickListener(this);
        mineLL = (LinearLayout) findViewById(R.id.mine_ll);
        mineLL.setOnClickListener(this);
        share = (ImageView) this.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "testShare: ");
                //初始化一个 WXTextObject 对象，填写分享的文本内容
                WXTextObject textObj = new WXTextObject();
                textObj.text = "来自剑士咖啡的分享";

                //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObj;
                msg.description = "剑士咖啡，好喝又好看，不来一杯吗？";

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());  //transaction字段用与唯一标示一个请求
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;

                //调用api接口，发送数据到微信
                api.sendReq(req);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_ll:
                mViewPager.setCurrentItem(0);
                updateBottomLinearLayoutSelect(true,false,false,false);
                break;
            case R.id.order_ll:
                mViewPager.setCurrentItem(1);
                updateBottomLinearLayoutSelect(false,true,false,false);
                break;
            case R.id.shopping_cart_ll:
                mViewPager.setCurrentItem(2);
                updateBottomLinearLayoutSelect(false,false,true,false);
                break;
            case R.id.mine_ll:
                mViewPager.setCurrentItem(3);
                updateBottomLinearLayoutSelect(false,false,false,true);
                break;
            default:
                break;
        }
    }
    private void updateBottomLinearLayoutSelect(boolean p, boolean o, boolean s, boolean m) {
        productLL.setSelected(p);
        orderLL.setSelected(o);
        shoppingCartLL.setSelected(s);
        mineLL.setSelected(m);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        T.showShort(this, "分享成功");
    }

    class ViewPagetOnPagerChangedLisenter implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.d(TAG,"onPageScrooled");
        }
        @Override
        public void onPageSelected(int position) {
            Log.d(TAG,"onPageSelected");
            boolean[] state = new boolean[mFragmentList.size()];
            state[position] = true;
            updateBottomLinearLayoutSelect(state[0],state[1],state[2],state[3]);
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d(TAG,"onPageScrollStateChanged");
        }
    }

    private void regToWx() {
        Log.i(TAG, "regToWx: ");
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(APP_ID);
    }
}
