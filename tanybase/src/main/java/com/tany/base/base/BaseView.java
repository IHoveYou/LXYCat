package com.tany.base.base;

/**
 * Created by Tany on 2018/6/4.
 * Desc:
 */


public interface BaseView {
    /**
     * 设置左边图片
     *
     * @param id
     */
    void setLeftImage(int id);

    /**
     * 设置左边文字
     *
     * @param left
     */
    void setLeftTv(String left);

    /**
     * 隐藏左边图片
     */
    void hideLeftImage();

    /**
     * 隐藏左边文字
     */
    void hideLeftTv();

    /**
     * 设置右边图片
     *
     * @param id
     */
    void setRightImage(int id);

    /**
     * 设置右边文字
     *
     * @param right
     */
    void setRightTv(String right);

    /**
     * 隐藏右边图片
     */
    void hideRightImage();

    /**
     * 隐藏右边文字
     */
    void hideRightTv();

    /**
     * 设置标题
     *
     * @param title
     */
    void setTitle(String title);

    /**
     * 设置标题颜色
     *
     * @param color
     */
    void setTitleColor(int color);

    /**
     * 显示标题
     */
    void showTitle();

    /**
     * 隐藏标题
     */
    void hideTitle();

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

    /**
     * 点击标题左边图片(返回)
     */
    void clickIvLeft();

    /**
     * 点击右边图片
     */
    void clickIvRight();

    /**
     * 点击左边文字
     */
    void clickTvLeft();

    /**
     * 点击右边文字
     */
    void clickTvRight();

    /**
     * 设置右边文字颜色
     *
     * @param id
     */
    void setRightTvColor(int id);

    /**
     * 设置左边文字颜色
     *
     * @param id
     */
    void setLeftTvColor(int id);
}
