package com.coffee.saber.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coffee.saber.App;
import com.coffee.saber.R;
import com.coffee.saber.model.User;
import com.coffee.saber.utils.DataBaseOpenHelper;
import com.coffee.saber.utils.Global;
import com.coffee.saber.utils.HttpParser;
import com.coffee.saber.utils.JsonUtils;
import com.coffee.saber.utils.SPPrivateUtils;
import com.coffee.saber.utils.T;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button registerBtn = null;
    private Button loginBtn = null;
    private EditText usernameEt = null;
    private EditText passwordEt = null;

    private boolean isLogin = true;
    private String username = null;
    private String password = null;
    private LoginHandler lh = null;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SPPrivateUtils.getBoolean(this, "is_login", false)) {
            intentToMain();
        }

        registerBtn = (Button) this.findViewById(R.id.register_btn);
        loginBtn = (Button) this.findViewById(R.id.login_btn);
        usernameEt = (EditText) this.findViewById(R.id.username);
        passwordEt = (EditText) this.findViewById(R.id.password);

        init();
    }

    private void init() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    login();
                } else {
                    register();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = false;
                loginBtn.setText("注册");
                usernameEt.setText("");
                passwordEt.setText("");
            }
        });

        lh = new LoginHandler(LoginActivity.this);
    }

    private void login() {
        username = usernameEt.getText().toString();
        password = passwordEt.getText().toString();
        if (username.trim().isEmpty()) {
            T.showShort(this, "用户名不能为空");
            return;
        }
        if (password.trim().isEmpty()) {
            T.showShort(this, "密码不能为空");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(username, password);
                Map<String,String> response = HttpParser.parseMapPost(Global.LOGIN_URL, "data="+user.toJson());
                int status = Integer.parseInt(response.get("status"));
//                int status = 1;
                Message msg = new Message();
                msg.what = LOGIN;
                msg.arg1 = status;
                msg.obj = response.get("data");
                lh.sendMessage(msg);
            }
        }).start();
    }

    private void register() {
        username = usernameEt.getText().toString();
        password = passwordEt.getText().toString();
        if (username.trim().isEmpty()) {
            T.showShort(this, "用户名不能为空");
            return;
        }
        if (password.trim().isEmpty()) {
            T.showShort(this, "密码不能为空");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
            User user = new User(username, password);
            Map<String,String> response = HttpParser.parseMapPost(Global.REGISTER_URL, "data="+user.toJson());
            int status = Integer.parseInt(response.get("status"));
//            int status = 1;
            Message msg = new Message();
            msg.what = REGISTER;
            msg.arg1 = status;
            msg.arg2 = 1;// 用户 id
            lh.sendMessage(msg);
            }
        }).start();
    }

    private void intentToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private static final int LOGIN = 10086;
    private static final int REGISTER = 10001;

    static class LoginHandler extends Handler {
        private  LoginActivity mActivity = null;

        LoginHandler (LoginActivity activity) {
           mActivity = activity;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN:
                    if (msg.arg1 == 1) {
                        T.showShort(mActivity, "登陆成功");
                        SPPrivateUtils.put(mActivity, "is_login", true);
                        String userJson = (String) msg.obj;
                        User user = (User) JsonUtils.fromJson(userJson, new TypeToken<User>(){}.getType());
                        if (user != null) {
                            SPPrivateUtils.put(mActivity, "user_id", user.getId());
                            DataBaseOpenHelper db = App.getDb();
                            ContentValues userValues = new ContentValues();
                            userValues.put("id", user.getId());
                            userValues.put("nick", user.getNick());
                            userValues.put("username", user.getUsername());
                            userValues.put("password", user.getPassword());
                            userValues.put("phone", user.getPhone());
                            userValues.put("sex", user.getSex());
                            Cursor cursor = db.query("user", "where id='" + SPPrivateUtils.getInt(mActivity, "user_id", 0) + "'");
                            if (cursor.moveToNext()) {
                                Log.i(TAG, "handleMessage1: " + cursor.getInt(1));
                                db.update("user",userValues, "id=?", new String[]{String.valueOf(cursor.getInt(1))});
                            } else {
                                db.insert("user", userValues);
                            }
                            mActivity.intentToMain();
                        } else {
                            T.showShort(mActivity, "数据获取异常");
                        }
                    } else {
                        T.showShort(mActivity, "登陆失败");
                    }
                    break;
                case REGISTER:
                    if (msg.arg1 == 1) {
                        T.showShort(mActivity, "注册成功");
                        mActivity.usernameEt.setText("");
                        mActivity.passwordEt.setText("");
                        mActivity.loginBtn.setText("登陆");
                        mActivity.isLogin = true;
                    } else {
                        T.showShort(mActivity, "注册失败");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
