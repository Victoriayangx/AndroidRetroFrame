package com.polysoft.testretrofit.listener;


import com.polysoft.testretrofit.base.BaseBean;
import com.polysoft.testretrofit.net.OKHttpManager;

public class LoadFinishBackListenerImpl<T extends BaseBean> implements OKHttpManager.LoadFinishCallBackListener<T> {
    @Override
    public void onFinish(T result) {
        if (result == null ) {
            return;
        }
        if (result.errorCode!=0) {
            return;
        }
        if (result.errorCode==401 || result.errorCode==402) {
            //Token 过期重新获取Token
        }

    }

    @Override
    public void onFailure(String errorCode) {

    }
}
