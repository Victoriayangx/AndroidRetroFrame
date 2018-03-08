package com.polysoft.testretrofit.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 同activity, 主要用户界面展示
 */
public abstract class BaseFragment<T extends BaseActivity> extends Fragment implements IBaseView {
    private T mActivity;
    //可见状态
    protected boolean hidden;

    /**
     * 初始化布局
     */
    public abstract int getLayoutRes();

    /**
     * 初始化视图
     */
    public abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 初始化控件
     */
    protected void setUpView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 刷新数据
     */
    protected void refresh() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (T) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setUpView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getCreateView(inflater, container);   //初始化布局
        initView(view, savedInstanceState);
        return view;
    }

    /**
     * 获取Fragment布局文件的View
     *
     * @param inflater
     * @param container
     * @return
     */
    protected View getCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    /**
     * 获取当前Fragment状态
     *
     * @return true为正常 false为未加载或正在删除
     */
    private boolean getStatus() {
        return (isAdded() && !isRemoving());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("fragment", getClass().getSimpleName());
        super.onSaveInstanceState(outState);
    }


    /**
     * setUserVisibleHint和onHiddenChanged 有区别, 注意使用
     */

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        this.hidden = isVisibleToUser;
//        if (isVisibleToUser) {
//            refresh();
//        }
//    }

    /**
     * 刷新
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    /**
     * 获取Activity
     *
     * @return
     */
    public T getBaseActivity() {
        if (mActivity == null) {
            mActivity = (T) getActivity();
        }
        return mActivity;
    }

    /**
     * 判断是否为空
     *
     * @param s
     * @return
     */
    public boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }


    @Override
    public void showToast(int resId) {
        showToast(resId);
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
    public void showToast(String msg) {
        if (getStatus()) {
            getBaseActivity().showToast(msg);
        }
    }

    @Override
    public void showError(final String msg) {
        if (getStatus()) {
            getBaseActivity().showError(msg);
        }
    }


    @Override
    public void showSuccess(final String msg) {
        if (getStatus()) {
            getBaseActivity().showSuccess(msg);
        }
    }


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void close() {

    }
}
