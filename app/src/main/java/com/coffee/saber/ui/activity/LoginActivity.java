package com.coffee.saber.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coffee.saber.R;
import com.coffee.saber.utils.SPPrivateUtils;
import com.coffee.saber.utils.T;

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
//                User user = new User(username, password);
//                Map<String,String> response = HttpParser.parseMapPost(Global.LOGIN_URL, "data="+user.toJson());
//                String status = Integer.parseInt(response.get("status"));
                int status = 1;
                Message msg = new Message();
                msg.what = LOGIN;
                msg.arg1 = status;
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
//                User user = new User(username, password);
//                Map<String,String> response = HttpParser.parseMapPost(Global.LOGIN_URL, user.toJson());
//                String status = Integer.parseInt(response.get("status"));
            int status = 1;
            Message msg = new Message();
            msg.what = REGISTER;
            msg.arg1 = status;
            msg.arg2 = 1;// 用户 id
            lh.sendMessage(msg);
            }
        }).start();
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
                        SPPrivateUtils.put(mActivity, "user_id", msg.arg2);
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        mActivity.startActivity(intent);
                        mActivity.finish();
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
