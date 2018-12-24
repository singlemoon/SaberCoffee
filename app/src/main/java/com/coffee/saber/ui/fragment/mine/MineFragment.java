package com.coffee.saber.ui.fragment.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.coffee.saber.R;
import com.coffee.saber.dao.UserDao;
import com.coffee.saber.model.User;
import com.coffee.saber.ui.activity.LoginActivity;
import com.coffee.saber.ui.fragment.BaseFragment;
import com.coffee.saber.utils.Global;
import com.coffee.saber.utils.HttpParser;
import com.coffee.saber.utils.SPPrivateUtils;
import com.coffee.saber.utils.T;
import com.coffee.saber.utils.TestData;

import java.util.Map;


/**
 * Created by Simo on 2018/12/11.
 */
public class MineFragment extends BaseFragment {
    private View mProductView = null;
    private TextView title = null;
    private EditText nickEt = null;
    private RadioGroup sexRg = null;
    private RadioButton sexMaleRb = null;
    private RadioButton sexFemaleRb = null;
    private RadioButton sexPrivateRb = null;
    private EditText phoneEt = null;
    private Button saveBtn = null;
    private Button loginOutBtn = null;

    private User mUser = null;
    private MineHandler mHandler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mProductView == null) {
            mProductView = inflater.inflate(R.layout.fragment_mine, null);
        }

        title = (TextView) mProductView.findViewById(R.id.title_text);
        nickEt = (EditText) mProductView.findViewById(R.id.nick_et);
        sexRg = (RadioGroup) mProductView.findViewById(R.id.sex_rg);
        sexMaleRb = (RadioButton) mProductView.findViewById(R.id.sex_male_rb);
        sexFemaleRb = (RadioButton) mProductView.findViewById(R.id.sex_female_rb);
        sexPrivateRb = (RadioButton) mProductView.findViewById(R.id.sex_private_rb);
        phoneEt = (EditText) mProductView.findViewById(R.id.phone_et);
        saveBtn = (Button) mProductView.findViewById(R.id.save_btn);
        loginOutBtn = (Button) mProductView.findViewById(R.id.login_out_btn);

        return mProductView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initValue();
        initView();

    }

    private void initValue() {
        mUser = UserDao.getUser(mActivity);
        if (mUser == null) {
            mUser = new User();
            mUser.setNick("信息获取错误");
        }
        mHandler = new MineHandler(this);
    }

    private void initView() {
        title.setText("我的");
        nickEt.setText(mUser.getNick());
        switch (mUser.getSex()) {
            case 1:
                sexMaleRb.setChecked(true);
                break;
            case 2:
                sexFemaleRb.setChecked(true);
                break;
            default:
                sexPrivateRb.setChecked(true);
                break;
        }
        phoneEt.setText(mUser.getPhone());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = nickEt.getText().toString();
                if (nick.isEmpty()) {
                    T.showShort(mActivity, "昵称不能为空");
                    return ;
                }
                mUser.setNick(nick);
                mUser.setPhone(phoneEt.getText().toString());
                int sex = -1;
                switch (sexRg.getCheckedRadioButtonId()) {
                    case R.id.sex_male_rb:
                        sex = 1;
                        break;
                    case R.id.sex_female_rb:
                        sex = 2;
                        break;
                    case R.id.sex_private_rb:
                        sex = 3;
                }
                mUser.setSex(sex);
                updateUser();
            }
        });

        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOut();
            }
        });
    }

    private void updateUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                    Map<String, String> map = HttpParser.parseMapPost(Global.UPDATE_USER, "data=" + mUser.toJson());
                    int status = Integer.parseInt(map.get("status"));
//                    int status = 1;
                    Message msg = new Message();
                    msg.what = UPDATE_USER;
                    msg.arg1 = status;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loginOut() {
        T.showShort(mActivity, "退出成功");
        SPPrivateUtils.put(mActivity, "is_login", false);
        Intent intent = new Intent(mActivity, LoginActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    private static final int UPDATE_USER = 123456;

    static class MineHandler extends Handler {
        MineFragment mFragment = null;
        Activity activity = null;

        public MineHandler(MineFragment fragment) {
            this.mFragment = fragment;
            this.activity = mFragment.getActivity();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_USER:
                    if (1 == msg.arg1) {
                        T.showShort(activity, "保存成功");
                    } else {
                        T.showShort(activity, "保存失败");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
