package com.polysoft.testretrofit.base;


import com.orhanobut.logger.Logger;
import com.polysoft.testretrofit.net.OKHttpManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 业务处理
 * Created by Sangyr
 * on 2017/4/5--->14:59
 */

public class BaseModel {
    private static final String KEY = "0ca175b9c05726a831d";

    protected void asyncPost(HashMap<String, String> params, String url, String token, final Class clazz, OKHttpManager.LoadFinishCallBackListener callBack) {
        //使用treeMap 排序
        Map<String, String> treeMap = new TreeMap(params);

        treeMap.put("token", token);
        //拼接需要签名的参数
        StringBuilder sb = new StringBuilder();
        if (treeMap != null) {
            //拼接key  value
            for (String key : treeMap.keySet()) {
                sb.append(key).append("=").append(treeMap.get(key) + "&");
            }
        }
        String paramsStr = sb.toString();
        //如果只有一个参数
        String str;
        if (paramsStr.length() > 0) {
            str = sb.toString().substring(0, paramsStr.length() - 1);
        } else {
            str = "";
        }

        //添加签名
        str = str + KEY;

        //遍历map ,整理为上传顺序
        LinkedHashMap<String, String> submit = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            submit.put(entry.getKey(), entry.getValue());
        }
        //加入sign
        submit.put("sign", encoder(str));

        OKHttpManager.getInstance().postAsyncJsonMap(url, submit, clazz, callBack);
    }

    protected void submitFiles(HashMap<String, String> params, String token, String url, String uploadServName, HashMap<String, String> pictures, final Class clazz, OKHttpManager.LoadFinishCallBackListener callBack) {
        //使用treeMap 排序
        Map<String, String> treeMap = new TreeMap(params);
        treeMap.put("token", token);
        //拼接需要签名的参数
        StringBuilder sb = new StringBuilder();
        if (treeMap != null) {
            //拼接key  value
            for (String key : treeMap.keySet()) {
                sb.append(key).append("=").append(treeMap.get(key) + "&");
            }
        }
        String str = sb.toString().substring(0, sb.toString().length() - 1).replaceAll("\n","");
        //添加签名
        String signStr = str + KEY;

        //遍历map ,整理为上传顺序
        LinkedHashMap<String, String> submit = new LinkedHashMap<>();
        //加入sign
        String sign = encoder(signStr);
        //***********修改后 带文件的上传方式变为参数拼接在url上
        OKHttpManager.getInstance().submitMultiPicture(url + "?" + str + "&sign=" + sign, uploadServName, submit, pictures, clazz, callBack);
    }


    public void downLoadFile(String url, String token, Class clazz, OKHttpManager.downLoadFinishCallBackListener l) {
        HashMap<String, String> params = new HashMap<>();
        OKHttpManager.getInstance().downloadFile(url,clazz,l);
    }

    public static String encoder(String str) {
        MessageDigest md5;
        byte[] bytes = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(str.toString().getBytes("UTF-8"));// md5加密
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e.toString());
        } catch (UnsupportedEncodingException e) {
            Logger.e(e.toString());
        }
        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }



    public static String getKey() {
        return KEY;
    }


}
