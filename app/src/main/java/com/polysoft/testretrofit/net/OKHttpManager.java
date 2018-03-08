package com.polysoft.testretrofit.net;


import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.polysoft.testretrofit.base.BaseBean;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * OKHttp 工具类
 *
 * @author SangYr
 */
public class OKHttpManager {

    public static final MediaType TYPE_MIXED = MediaType.parse("multipart/mixed");
    public static final MediaType TYPE_ALTERNATIVE = MediaType.parse("multipart/alternative");
    public static final MediaType TYPE_DIGEST = MediaType.parse("multipart/digest");
    public static final MediaType TYPE_PARALLEL = MediaType.parse("multipart/parallel");
    public static final MediaType TYPE_FORM = MediaType.parse("multipart/form-data");
    public static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TYPE_PNG = MediaType.parse("image/png");
    public static final MediaType TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");


    private static final int CONNECT_TIMEOUT = 15; //超时时间
    private static final int READ_TIMEOUT = 15; //超时时间
    private static final int WRITE_TIMEOUT = 15; //超时时间

    private static HashMap<String, Call> callsHashMap;// 请求

    private static OKHttpManager mOkHttpManager;
    private LogInterceptor interceptor;

    private static OkHttpClient mHttpClient = //
            new OkHttpClient.Builder()//
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//
                    .build();
    private static final String ERROR_MESSAGE = "请求错误";


    public static OKHttpManager getInstance() {
        if (mOkHttpManager == null) {
            synchronized (OKHttpManager.class) {
                if (mOkHttpManager == null) {
                    mOkHttpManager = new OKHttpManager();
                }
            }
        }
        return mOkHttpManager;
    }

    //请求们
    private static HashMap<String, Call> getCallHashMap() {
        if (callsHashMap == null) {
            synchronized (OKHttpManager.class) {
                if (callsHashMap == null) {
                    callsHashMap = new HashMap<>();
                }
            }
        }
        return callsHashMap;
    }

    public OKHttpManager() {
        interceptor = new LogInterceptor();
        mHttpClient = new OkHttpClient.Builder()//
                //打印请求log
                .addInterceptor(interceptor)
//                //失败重连
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//
                .build();
    }


    /**
     * 异步GET
     *
     * @param url
     * @throws Exception
     */
    public <T extends BaseBean> void getAsync(String url, final Class clazz, final LoadFinishCallBackListener<T> callBack) {
        Request request = new Request.Builder()//
                .url(url)//
                .tag(clazz.getSimpleName())//
                .build();
        createCall(clazz.getSimpleName(), request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                loadFail(e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                loadSuccess(response, callBack, clazz);
            }
        });
    }

    /**
     * 异步POST ,返回json
     * key-value
     *
     * @param url
     */
    public <T extends BaseBean> void postAsyncJsonMap(String url, LinkedHashMap<String, String> values, final Class clazz, final LoadFinishCallBackListener<T> callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            Logger.i("entry.getKey()=" + entry.getKey() + ";entry.getValue()==" + entry.getValue());
            if (entry.getKey() != null && entry.getValue() != null) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder()//
                .url(url)//
                .post(builder.build())//
                .tag(clazz.getSimpleName())//
                .addHeader("User-Agent", String.format("%s/%s (Linux; Android %s; %s Build/%s)", "yf", "1.0", Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID))
                .build();
        createCall(clazz.getSimpleName(), request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                loadFail(e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                loadSuccess(response, callBack, clazz);
            }
        });
    }


    /**
     * 异步POST ,返回json
     *
     * @param url
     */
    public <T extends BaseBean> void postAsyncObject(String url, HashMap<String, String> values, final Class clazz, final LoadFinishCallBackListener<T> callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()//
                .url(url)//
                .post(builder.build())//
                .tag(clazz.getSimpleName())//
                .build();
        createCall(clazz.getSimpleName(), request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                loadFail(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                loadSuccess(response, callBack, clazz);
            }
        });
    }

    /**
     * 异步Post方式提交String
     *
     * @param url
     * @param content
     * @throws Exception
     */
    public <T extends BaseBean> void postStringAsync(String url, String content, final Class clazz, final LoadFinishCallBackListener<T> callBack) {
        Request request = new Request.Builder()//
                .url(url)//
                .post(RequestBody.create(TYPE_MARKDOWN, content))//
                .tag(clazz.getSimpleName())//
                .build();
        createCall(clazz.getSimpleName(), request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                loadFail(e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                loadSuccess(response, callBack, clazz);
            }
        });
    }

    /**
     * 异步Post方式提交JSON
     *
     * @param url
     * @param content
     * @throws Exception
     */
    public <T extends BaseBean> void postJsonAsync(String url, String content, final Class clazz, final LoadFinishCallBackListener<T> callBack) {
        Request request = new Request.Builder()//
                .url(url)//
                .post(RequestBody.create(TYPE_JSON, content))//
                .tag(clazz.getSimpleName())//
                .build();
        createCall(clazz.getSimpleName(), request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                loadFail(e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                loadSuccess(response, callBack, clazz);
            }
        });
    }

    /**
     * 异步POST上传文件
     *
     * @param url
     * @param filePath
     * @return
     * @throws Exception
     */
    public <T extends BaseBean> void postSubmitFileAsync(String url, String filePath, final Class clazz, final LoadFinishCallBackListener<T> callBack) {
        File file = new File(filePath);
        Request request = new Request.Builder()//
                .url(url)//
                .post(RequestBody.create(TYPE_MARKDOWN, file))//
                .tag(clazz.getSimpleName())//
                .build();
        createCall(clazz.getSimpleName(), request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                loadFail(e, callBack);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                loadSuccess(response, callBack, clazz);
            }
        });
    }

    /**
     * 上传多张图片
     *
     * @param url
     * @param callBack
     * @param uploadServName 上传图片 key值
     * @param params         参数
     * @param pictures
     * @throws Exception
     */
    public <T extends BaseBean> void submitMultiPicture(String url, String uploadServName, HashMap<String, String> params, HashMap<String, String> pictures, final Class clazz, final LoadFinishCallBackListener<T> callBack) {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        // 遍历map中所有参数到builder
        if (pictures.size() > 0) {
            // 遍历paths中所有图片绝对路径到builder，并约定key作为后台接受多张图片的key
            int position = 0;
            for (Map.Entry<String, String> entry : pictures.entrySet()) {
//            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(entry.getValue());
                multipartBodyBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file" + (position++) + "\"; filename=\"" + entry.getKey() + "\""),
                        RequestBody.create(MediaType.parse("image/png"), new File(entry.getValue())));
            }
        } else {
            //如果图片为空,为了不报错 , 传一个空的 key  value
            multipartBodyBuilder.addFormDataPart("", "");
        }
        // 构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        // 构建请求
        Request request = new Request.Builder()//
                .url(url)// 地址
                .post(requestBody)// 添加请求体
                .tag(clazz.getSimpleName())
                .build();
        // 发送异步请求，同步会报错
        createCall(clazz.getSimpleName(), request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {//失败
                loadFail(e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {//成功
                loadSuccess(response, callBack, clazz);
            }
        });
    }
//    /**
//     * 上传多张图片
//     *
//     * @param url
//     * @param callBack
//     * @param uploadServName 上传图片 key值
//     * @param params         参数
//     * @param pictures
//     * @throws Exception
//     */
//    public <T extends BaseBean> void submitMultiPicture(String url, String uploadServName, HashMap<String, String> params, HashMap<String, String> pictures, final Class clazz, final LoadFinishCallBackListener<T> callBack) {
//        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
//        multipartBodyBuilder.setType(MultipartBody.FORM);
//        // 遍历map中所有参数到builder
//        if (pictures.size() > 0) {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
//            }
//            // 遍历paths中所有图片绝对路径到builder，并约定key作为后台接受多张图片的key
//            for (Map.Entry<String, String> entry : pictures.entrySet()) {
////            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(entry.getValue());
//                multipartBodyBuilder.addFormDataPart(uploadServName, entry.getKey(), RequestBody.create(TYPE_PNG, new File(entry.getValue())));
//            }
//        } else {
//            //如果图片为空,为了不报错 , 传一个空的 key  value
//            multipartBodyBuilder.addFormDataPart("", "");
//        }
//        // 构建请求体
//        RequestBody requestBody = multipartBodyBuilder.build();
//        // 构建请求
//        Request request = new Request.Builder()//
//                .url(url)// 地址
//                .post(requestBody)// 添加请求体
//                .tag(clazz.getSimpleName())
//                .build();
//        // 发送异步请求，同步会报错
//        createCall(clazz.getSimpleName(), request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {//失败
//                loadFail(e, callBack);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {//成功
//                loadSuccess(response, callBack, clazz);
//            }
//        });
//    }


    /**
     * 下载附件
     */
    public void downloadFile(final String url, final Class clazz, final downLoadFinishCallBackListener callBack) {
        Request request = new Request.Builder().url(url).get().tag(clazz.getSimpleName()).build();
        createCall(clazz.getSimpleName(), request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                loadFail(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                downLoadSuccess(response, callBack);
            }
        });

    }

    /**
     * 存储请求 ,用于取消
     *
     * @param TAG
     * @param request
     * @return
     */
    private static Call createCall(String TAG, Request request) {
        final Call call = mHttpClient.newCall(request);
        getCallHashMap().put(TAG, call);
        return call;
    }

    /**
     * 请求失败
     */
    private void loadFail(final IOException e, final LoadFinishCallBackListener callBack) {
        Logger.e("接口连接", e.toString());
        if (e != null) {
            callBack.onFailure(e.getMessage());
        } else {
            callBack.onFailure(ERROR_MESSAGE);
        }
    }

    /**
     * 请求成功 , 返回json
     *
     * @param response
     * @param callBack
     * @throws IOException
     */
    private <T extends BaseBean> void loadSuccess(final Response response, final LoadFinishCallBackListener<T> callBack, final Class clazz) {
        try {
            if (response != null) {
                String content = response.body().string();
                if (TextUtils.isEmpty(content)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("status", "0");
                    jsonObject.put("msg", "连接服务器异常");
                    callBack.onFinish((T) JSON.parseObject(jsonObject.toJSONString(), clazz));
                } else {
                    callBack.onFinish((T) JSON.parseObject(content, clazz));
                }
            }
        } catch (Exception e) {
            callBack.onFailure("");
            Logger.e(e.toString());
        }
    }


    /**
     * 下载文件
     *
     * @param response
     * @param callBack
     */
    private void downLoadSuccess(final Response response, final downLoadFinishCallBackListener callBack) {
        try {
            if (response != null) {
                callBack.onFinish(response);
            }
        } catch (Exception e) {
            callBack.onFailure("");
            Logger.e(e.toString());
        }
    }


    /**
     * 取消操作
     */

    public static void cancelCallByTag(String TAG) {
        Call call = callsHashMap.get(TAG);
        if (call != null) {
            call.cancel();
        }
    }

    /**
     * 取消全部
     */
    public static void cancelAll() {
        mHttpClient.dispatcher().cancelAll();
    }

    /**
     * 回调接口
     */
    public interface LoadFinishCallBackListener<T extends BaseBean> {
        void onFinish(T result);

        void onFailure(String errorCode);
    }

    public interface downLoadFinishCallBackListener {
        void onFinish(Response result);

        void onFailure(String errorCode);
    }
}
