package com.polysoft.testretrofit.model;

import com.polysoft.testretrofit.base.BaseActivity;
import com.polysoft.testretrofit.base.BaseModel;
import com.polysoft.testretrofit.bean.LoginBean;
import com.polysoft.testretrofit.config.HTTPConstant;
import com.polysoft.testretrofit.net.OKHttpManager;
import com.polysoft.testretrofit.utils.PhoneUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivityModel extends BaseModel {



    public void login(BaseActivity context, String username, String password, OKHttpManager.LoadFinishCallBackListener l){

//        LinkedHashMap<String,String> params = new LinkedHashMap<>();
//        params.put("loginName",username);
//        params.put("password",password);
//        params.put("apptype","ANDROID");
//        params.put("nowVersion", PhoneUtil.getVersionName(context));


        try {
            JSONObject params = new JSONObject();
            params.put("loginName",username);
            params.put("password",password);
            params.put("apptype","ANDROID");
            params.put("nowVersion", PhoneUtil.getVersionName(context));
            String paramsStr = params.toString();
            OKHttpManager.getInstance().postJsonAsync(HTTPConstant.LOGIN, paramsStr, LoginBean.class, l);

        } catch (JSONException e) {
            e.printStackTrace();
        }






    }
}
