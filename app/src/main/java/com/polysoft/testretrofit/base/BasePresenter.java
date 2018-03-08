package com.polysoft.testretrofit.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.polysoft.testretrofit.listener.DialogClickListener;
import com.polysoft.testretrofit.net.OKHttpManager;
import com.polysoft.testretrofit.utils.CallOtherOpeanFileUtils;
import com.polysoft.testretrofit.utils.FileUtils;
import com.polysoft.testretrofit.utils.UserUtils;

import java.io.File;
import java.util.regex.Pattern;

import okhttp3.Response;

/**
 * Presenter 主要负责与activity 和 model 进行交互, 负责校验数据传递的正确性
 *
 * @param <T>
 */
public abstract class BasePresenter<T extends BaseActivity> {
    protected T mActivity;

    /*正则表达式：验证手机号*/
    private static final String REGEX_MOBILE = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
    private InputMethodManager inputMethodManager;

    protected void onDestroy() {

    }


    /**
     * 显示loading
     */
    public void showActivityLoading() {
        if (isActive()) {
            mActivity.showLoading();
        }
    }

    /**
     * 隐藏loading
     */
    public void hideActivityLoading() {
        if (isActive()) {
            mActivity.hideLoading();
        }
    }

    /**
     * 检测指定的字符串时候为空
     *
     * @param text
     * @return
     */
    public boolean isEmpty(String text) {
        if (TextUtils.isEmpty(text)) {
            return true;
        }
        return false;
    }

    /**
     * 设置文字
     *
     * @param textView
     * @param content
     */
    public void setText(TextView textView, String content) {
        if (isEmpty(content)) {
            textView.setText("");
        } else {
            textView.setText(content);
        }
    }


    /**
     * 下载附件
     *
     * @param url
     * @param path
     * @param fileName
     * @param openFlag
     * @param clazz
     * @param listener
     */

    public void downLoadFile(String url, final String path, final String fileName, final boolean openFlag, Class clazz, final DialogClickListener listener) {
        if (isEmpty(url)) {
            showActivityError("下载路径为空，请重试");
            return;
        }
        showActivityLoading();
        BaseModel baseModel = new BaseModel();
        baseModel.downLoadFile(url, UserUtils.getToken(mActivity), clazz, new OKHttpManager.downLoadFinishCallBackListener() {
            @Override
            public void onFinish(final Response response) {
                if (response.isSuccessful()) {
                    downLoadFileToLocal(response, path, fileName, openFlag, listener);
                }
                hideActivityLoading();
            }

            @Override
            public void onFailure(String errorCode) {
                loadError();
            }
        });
    }

    /**
     * 下载文件并用相应的应用打开
     */
    private void downLoadFileToLocal(final Response response, final String path, final String fileName, final boolean openFlag, final DialogClickListener listener) {
        FileUtils.writeFile(response, path, fileName);
        if (openFlag) {
            final File downLoadFile = new File(path, fileName);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CallOtherOpeanFileUtils.openFile(mActivity, downLoadFile);
                    if (listener != null) {
                        listener.onClick(true);
                    }
                }
            });


        }
    }


    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    protected boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 请求失败
     */
    protected void loadError() {
        showActivityError("请求失败");
        hideActivityLoading();
    }

    /**
     * 隐藏键盘
     */
    public void hideSoftKeyboard() {
        if (isActive()) {
            if (inputMethodManager == null) {
                inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            if (mActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                if (mActivity.getCurrentFocus() != null)
                    inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示错误信息
     *
     * @param msg
     */
    public void showActivityError(String msg) {
        if (isActive()) {
            mActivity.showError(msg);
        }
    }

    /**
     * 显示成功信息
     *
     * @param msg
     */
    public void showActivitySuccess(String msg) {
        if (isActive()) {
            mActivity.showSuccess(msg);
        }
    }

    /**
     * 是否是手机号
     */
    public static boolean isMobilePhone(String phone) {
        return Pattern.matches(REGEX_MOBILE, phone);
    }

    /**
     * presenter与view 绑定
     *
     * @param view
     */
    public void attachView(T view) {
        this.mActivity = view;
    }

    /**
     * 釋放對activity的持有
     */
    public void detach() {
        mActivity = null;
    }

    /**
     * 当前 activity 是否已经被destroy
     *
     * @return
     */
    public boolean isActive() {
        return mActivity != null;
    }
}
