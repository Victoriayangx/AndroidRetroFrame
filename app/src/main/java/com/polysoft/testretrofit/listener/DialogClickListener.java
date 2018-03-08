package com.polysoft.testretrofit.listener;

/**
 * 弹出框 ,确认和取消点击
 */

public interface DialogClickListener {
    /**
     * 确认/取消
     * @param confirm
     */
    void onClick(boolean confirm);
}
