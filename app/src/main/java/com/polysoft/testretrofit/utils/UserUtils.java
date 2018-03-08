package com.polysoft.testretrofit.utils;

import android.content.Context;

import com.polysoft.testretrofit.config.ConstantConfig;


/**
 * 常用的一些方法
 */

public abstract class UserUtils {



    /**
     * 获取token
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        return (String) SharedPerfrenceUtils.get(context, ConstantConfig.TOKEN, "");
    }



    /**
     * 设置token
     *
     * @param context
     * @return
     */
    public static void setToken(Context context, String token) {
        SharedPerfrenceUtils.put(context, ConstantConfig.TOKEN, token);
    }

    /**
     * 获取用户名
     *
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
        return (String) SharedPerfrenceUtils.get(context, ConstantConfig.USER_NAME, "");
    }

    /**
     * 设置用户名称
     */
    public static void setUserName(Context context, String userName){
        SharedPerfrenceUtils.put(context, ConstantConfig.USER_NAME, userName + "");
    }


}
