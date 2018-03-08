package com.polysoft.testretrofit.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.polysoft.testretrofit.listener.DialogClickListener;


public abstract class BaseDialogFragment extends DialogFragment {
    //点击监听
    protected DialogClickListener mClickListener;

    /**
     * 初始化布局
     */
    public abstract int getLayoutRes();

    /**
     * 初始化视图
     */
    public abstract void initView(View view, Bundle savedInstanceState);


    /**
     * 是否点击屏幕外消失
     */
    public abstract boolean touchOutSideDismiss();

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
     * 弹出框 ,确认和取消按钮点击事件
     *
     * @param listener
     */
    public BaseDialogFragment setOnConfirmListener(DialogClickListener listener) {
        mClickListener = listener;
        return this;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setUpView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(touchOutSideDismiss());
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = inflater.inflate(getLayoutRes(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

}

