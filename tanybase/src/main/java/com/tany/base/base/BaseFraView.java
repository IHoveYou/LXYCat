package com.tany.base.base;

/**
 * Created by Tany on 2018/6/4.
 * Desc:
 */


public interface BaseFraView {
    /**
     * 文字
     *
     * @param toast
     * @param location
     */
    void toast(String toast, String... location);

    /**
     * 显示加载框
     */
    void showLoading();

    /**
     * 隐藏加载框
     */
    void hideLoading();

    /**
     * 显示错误界面
     */
    void showError();

    /**
     * 隐藏错误界面
     */
    void hideError();

    /**
     * 设置错误内容
     *
     * @param ids
     */
    void setError(int... ids);

    /**
     * 点击错误按钮
     */
    void clickError();
}
