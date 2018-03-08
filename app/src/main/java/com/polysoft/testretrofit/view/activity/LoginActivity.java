package com.polysoft.testretrofit.view.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.polysoft.testretrofit.R;
import com.polysoft.testretrofit.base.BaseActivity;
import com.polysoft.testretrofit.bean.LoginBean;
import com.polysoft.testretrofit.config.ConstantConfig;
import com.polysoft.testretrofit.presenter.LoginActivityPresenter;
import com.polysoft.testretrofit.utils.SharedPerfrenceUtils;

/**
 * Created by yang on 2018/3/6.
 */

public class LoginActivity extends BaseActivity<LoginActivityPresenter,LoginBean> implements View.OnClickListener{
    private Button loginButton;
    private EditText etUserName,etPassword;



    @Override
    public int setContentViewResource() {
        return R.layout.activity_login;
    }

    @Override
    public void createPresenter() {
        mPresenter = new LoginActivityPresenter();

    }

    @Override
    public void findView() {
        loginButton = (Button) findViewById(R.id.login);
        etUserName = (EditText) findViewById(R.id.login_username);
        etPassword = (EditText) findViewById(R.id.login_password);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                mPresenter.login(etUserName.getText().toString(), etPassword.getText().toString());
                break;

        }
    }

    @Override
    protected void setUpView() {
        loginButton.setOnClickListener(this);
        etUserName.setText((String)SharedPerfrenceUtils.get(getActivity(), ConstantConfig.USER_NAME,""));
    }

}
