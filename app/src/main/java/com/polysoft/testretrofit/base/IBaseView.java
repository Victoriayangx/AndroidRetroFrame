package com.polysoft.testretrofit.base;

import android.content.Context;

/**
 * 公共View接口
 */
public interface IBaseView {
    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 弹出toast
     *
     * @param resId 资源文件id
     */
    void showToast(int resId);

    /**
     * 根据字符串弹出toast
     *
     * @param msg 提示内容
     */
    void showToast(String msg);

    /**
     * 弹出toast--失败
     *
     * @param resId 资源文件id
     */
    void showError(int resId);

    /**
     * 根据字符串弹出toast --失败
     *
     * @param msg 提示内容
     */
    void showError(String msg);

    /**
     * 弹出toast--成功
     *
     * @param resId 资源文件id
     */
    void showSuccess(int resId);

    /**
     * 根据字符串弹出toast --成功
     *
     * @param msg 提示内容
     */
    void showSuccess(String msg);


    /**
     * 获取当前上下文对象
     *
     * @return
     */
    Context getContext();


    /**
     * 结束当前页面
     */
    void close();

}
