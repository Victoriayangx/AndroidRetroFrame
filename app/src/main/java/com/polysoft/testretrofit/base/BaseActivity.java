package com.polysoft.testretrofit.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.orhanobut.logger.Logger;
import com.polysoft.testretrofit.R;
import com.polysoft.testretrofit.application.TestApplication;
import com.polysoft.testretrofit.config.ConstantConfig;
import com.polysoft.testretrofit.utils.WindowUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * activity 只做数据的展示 , 以及界面的一些操作
 *
 * @param <T>
 */
public abstract class BaseActivity<T extends BasePresenter, V extends BaseBean> extends FragmentActivity implements IBaseView {
    FragmentManager fragmentManager;
    protected SVProgressHUD progressBar;

    public T mPresenter;


    /**
     * 初始化布局
     */
    public abstract int setContentViewResource();

    /**
     * 初始化控制器
     */
    public abstract void createPresenter();

    /**
     * 初始化相关数据
     */
    protected void initData() {
    }

    /**
     * 初始化控件
     */
    public abstract void findView();


    /**
     * 设置控件功能
     */
    protected void setUpView() {
    }

    /**
     * 销毁控制器
     */
    protected void destroyPresenter() {
        mPresenter = null;
    }

    /**
     * 展示页面信息
     */
    public void showInfoInView(V bean) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPresenter();
        initData();
        mPresenter.attachView(this);
        setContentView(setContentViewResource());
        if(!getActivity().getClass().getSimpleName().contains("CustomAlertDialog")){
            initStatusBar();
        }
        findView();
        progressBar = new SVProgressHUD(getContext());
        setUpView();
        TestApplication.getActivityInstance().add(this);
    }

    /**
     * 沉浸状态栏，文字变为深色
     */
    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(ContextCompat.getColor(this, R.color.app_black));
            tintManager.setStatusBarTintEnabled(true);
        }
    }


    /**
     * 全屏
     */

    protected void setFullScreen() {
        WindowUtil.setFullScreen(getWindow());
    }

    @Override
    public void showLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.show();
            }
        });

    }

    @Override
    public void hideLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.dismiss();
            }
        });
    }

    /**
     * 设置文字,如果是空或者null 就设置为空字符串
     *
     * @param content
     * @return
     */
    public String setText(String content) {
        if (isEmpty(content)) {
            return "";
        }
        return content;
    }

    /**
     * 判断是否为空
     *
     * @param content
     * @return
     */
    public boolean isEmpty(String content) {
        return TextUtils.isEmpty(content);
    }


    @Override
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showError(int resId) {
        showError(getString(resId));
    }

    @Override
    public void showSuccess(int resId) {
        showSuccess(getString(resId));
    }

    @Override
    public void showToast(final String msg) {
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toasty.normal(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void showError(final String msg) {
        if (!isFinishing() && !isDestroyed()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toasty.error(getContext(), msg, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }


    @Override
    public void showSuccess(final String msg) {
        if (!isFinishing() && !isDestroyed()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toasty.success(getContext(), msg, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    @Override
    public Context getContext() {
        return this;
    }


    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.detach();
        destroyPresenter();
        TestApplication.getActivityInstance().remove(this);
        super.onDestroy();
    }


    /**
     * 开启activity
     *
     * @param clazz
     */
    public void openActivity(Class clazz) {
        Intent intent = new Intent(getContext(), clazz);
        getContext().startActivity(intent);
    }

    /**
     * 开启activity
     *
     * @param intent
     */
    public void openActivity(Intent intent) {
        getContext().startActivity(intent);
    }

    public void openActivityForResult(Intent intent, int resultCode) {
        getActivity().startActivityForResult(intent, resultCode);
    }

    /**
     * 强制隐藏输入法键盘
     */
    public void hideInputSoft() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 打印log
     *
     * @param info
     */
    public void printLog(String info) {
        Logger.e(getClass().getSimpleName().toString(), info);
    }


    //--------------------------Fragment相关--------------------------//

    /**
     * 获取Fragment管理器
     *
     * @return
     */
    public FragmentManager getBaseFragmentManager() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        return fragmentManager;
    }

    /**
     * 获取Fragment事物管理
     *
     * @return
     */
    public FragmentTransaction getFragmentTransaction() {
        return getBaseFragmentManager().beginTransaction();
    }

    /**
     * 替换一个Fragment
     *
     * @param res
     * @param fragment
     */
    public void replaceFragment(int res, Fragment fragment) {
        replaceFragment(res, fragment, false);
    }

    /**
     * hide/show  --替换一个Fragment
     *
     * @param fragmentOld
     * @param fragmentNew
     */
    public void replaceFragment(int res, BaseFragment fragmentOld, BaseFragment fragmentNew) {
        if ((fragmentNew.getTag() + "").equals(fragmentOld.getTag() + "")) {
            return;
        }
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        if (!fragmentNew.isAdded()) {
            fragmentTransaction.add(res, fragmentNew, fragmentNew.getClass().getSimpleName()).commit();
        } else {
            fragmentTransaction.show(fragmentNew).hide(fragmentOld).commit();
        }
    }

    /**
     * 替换一个Fragment并设置是否加入回退栈
     *
     * @param res
     * @param fragment
     * @param isAddToBackStack
     */
    public void replaceFragment(int res, Fragment fragment, boolean isAddToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.replace(res, fragment);
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

    }

    /**
     * 添加一个Fragment ,如果已经添加就显示
     *
     * @param res
     * @param fragment
     */
    public void addFragment(int res, Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            fragmentTransaction.add(res, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    /**
     * 移除一个Fragment
     *
     * @param fragment
     */
    public void removeFragment(Fragment fragment) {
        if (fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 显示一个Fragment
     *
     * @param fragment
     */
    public void showFragment(Fragment fragment) {
        if (fragment != null) {
            if (fragment.isHidden()) {
                FragmentTransaction fragmentTransaction = getFragmentTransaction();
                fragmentTransaction.show(fragment);
                fragmentTransaction.commit();
            }
        }
    }

    /**
     * 隐藏一个Fragment
     *
     * @param fragment
     */
    public void hideFragment(Fragment fragment) {
        if (fragment != null)
            if (fragment.isVisible()) {
                FragmentTransaction fragmentTransaction = getFragmentTransaction();
                fragmentTransaction.hide(fragment);
                fragmentTransaction.commit();
            }
    }

    //--------------------------Fragment相关end--------------------------//

    /**
     * 拨打电话
     *
     * @param phoneNumber
     */
    public void callPhone(final String phoneNumber) {
        Logger.e("电话号码：",phoneNumber);
        if (isEmpty(phoneNumber)) {
            showError("当前电话号码为空");
            return;
        }
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + phoneNumber);
                    intent.setData(data);
                    openActivity(intent);
                } else {
                    showError(ConstantConfig.WITHOUT_PERMISSION);
                }
            }
        });
    }
}