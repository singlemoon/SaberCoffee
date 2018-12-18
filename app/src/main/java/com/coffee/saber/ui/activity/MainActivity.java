package com.coffee.saber.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.coffee.saber.R;
import com.coffee.saber.ui.adapter.CoffeePagerAdapter;
import com.coffee.saber.ui.fragment.mine.MineFragment;
import com.coffee.saber.ui.fragment.order.OrderFragment;
import com.coffee.saber.ui.fragment.product.ProductFragment;
import com.coffee.saber.ui.fragment.shopping_cart.ShoppingCartFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity.TAG";
    private LinearLayout productLL;
    private LinearLayout orderLL;
    private LinearLayout shoppingCartLL;
    private LinearLayout mineLL;
    private ViewPager mViewPager;
    private CoffeePagerAdapter mAdapter;
    private FragmentManager mFragmentManager;

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
}
