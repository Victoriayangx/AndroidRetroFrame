package com.polysoft.testretrofit.presenter;

import com.polysoft.testretrofit.base.BaseActivity;
import com.polysoft.testretrofit.base.BasePresenter;
import com.polysoft.testretrofit.bean.LoginBean;
import com.polysoft.testretrofit.config.ConstantConfig;
import com.polysoft.testretrofit.listener.LoadFinishBackListenerImpl;
import com.polysoft.testretrofit.model.LoginActivityModel;
import com.polysoft.testretrofit.utils.SharedPerfrenceUtils;

import es.dmoral.toasty.Toasty;

/**
 * Created by yang on 2018/3/6.
 */

public class LoginActivityPresenter extends BasePresenter<BaseActivity> {


    private LoginActivityModel loginActivityModel;

    public LoginActivityPresenter() {
        loginActivityModel = new LoginActivityModel();
    }


    public void login(final String username,final String password){
        if (isEmpty(username)) {
            showActivityError("登录名不能为空");
            return;
        }
        if (isEmpty(password)) {
            showActivityError("密码不能为空");
            return;
        }
        showActivityLoading();
        loginActivityModel.login(mActivity,username, password, new LoadFinishBackListenerImpl<LoginBean>() {
            @Override
            public void onFinish(final LoginBean result) {
                super.onFinish(result);
                if (result.success) {
                    if (isActive()) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.success(mActivity,"登录成功");
                                SharedPerfrenceUtils.put(mActivity, ConstantConfig.USER_NAME, username);
                                SharedPerfrenceUtils.get(mActivity, ConstantConfig.PASSWORD, password);
                            }
                        });

                    }
                } else {
                    showActivityError(result.errorMsg);
                }
                hideActivityLoading();
            }

            @Override
            public void onFailure(String errorCode) {
                super.onFailure(errorCode);
                loadError();
            }
        });

    }
}
