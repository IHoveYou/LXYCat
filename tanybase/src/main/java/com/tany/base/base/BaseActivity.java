package com.tany.base.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tany.base.R;
import com.tany.base.bean.NetBean;
import com.tany.base.utils.AppHelper;
import com.tany.base.utils.LogUtil;
import com.tany.base.utils.NetUtil;
import com.tany.base.utils.StatusBarUtil;
import com.tany.base.utils.StringUtil;
import com.tany.base.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Tany on 2018/11/5.
 * Desc:
 */


public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseVM> extends AppCompatActivity implements BaseView, View.OnClickListener {
    private static BaseActivity activity;
    public static ArrayList<BaseActivity> activities = new ArrayList<>();
    public Activity mContext;//子类用这个上下文
    private RelativeLayout rlTop;
    private RelativeLayout rlContent;
    private ViewStub vsError;
    private ViewStub vsLoading;
    private ImageView ivLeft;
    private TextView tvLeft;
    private TextView tvTitle;
    private TextView tvRight;
    private ImageView ivRight;
    private RelativeLayout rlTitle;
    public View contentView;
    public B mBinding;
    public VM mVM;
    public int netModile;//网络状态
    private ImageView ivError;
    private TextView tvErrorTitle;
    private TextView tvErrorContent;
    private TextView tvError;
    private RelativeLayout rlError;
    private RelativeLayout rlLoading;
    public static Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        activities.add(this);
        mContext = getActivity();
        setContentView(R.layout.activity_base);
        setStatusBar();
        EventBus.getDefault().register(this);//订阅
        findViewById();
        mVM = createVM();

        setContentLayout();
    }

    /**
     * 子类重写此方法绑定VM
     *
     * @return
     */
    protected abstract VM createVM();
    /**
     * 绑定内容布局
     */
    protected abstract void setContentLayout();

    /**
     * 初始化界面
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    private void findViewById() {
        rlTop = findViewById(R.id.rl_top);
        rlContent = findViewById(R.id.rl_content);
        vsError = findViewById(R.id.vs_error);
        vsLoading = findViewById(R.id.vs_loading);
        ivLeft = findViewById(R.id.iv_left);
        tvLeft = findViewById(R.id.tv_left);
        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);
        ivRight = findViewById(R.id.iv_right);
        rlTitle = findViewById(R.id.rl_title);
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        rlTitle.setOnClickListener(v -> {
        });
    }

    /**
     * 绑定内容布局并用DataBindingUtil绑定
     *
     * @param resId
     */
    public void setContentLayout(int resId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(resId, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(layoutParams);
        if (null != rlContent) {
            rlContent.addView(contentView);
            if (DataBindingUtil.getBinding(contentView) != null
                    || (contentView.getTag() instanceof String))
                mBinding = DataBindingUtil.bind(contentView);
        }
//        try {
        init();
//        } catch (Exception e) {
//            LogUtil.e("Error", e.getMessage() + "");
//        }
    }

    public void init() {
        netModile = NetUtil.getNetWrokState(this);
        initView();
        if (netModile == -1) {
            toast("网络未连接，请检查网络设置");
        } else {
            initData();
        }
    }

    /**
     * 子类想改变其他颜色就重写这个方法
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.maincolor));
//        StatusBarUtil.setLightMode(mContext);
//        StatusBarUtil.setDarkMode(mContext);
    }

    protected void hideStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
    }
    /**
     * 获取当前activity 若当前关闭获取上一个未关闭的activity
     *
     * @return
     */
    public static BaseActivity getActivity() {
        for (int i = activities.size() - 1; i >= 0; i--) {
            if (!activities.get(i).isFinishing()) {
                return activities.get(i);
            }
        }
        return activity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消订阅
        removeActivity(this);
        callBack.clear();
        callBack = null;
    }

    public static synchronized void removeActivity(BaseActivity activity) {
        if (activities.contains(activity))
            activities.remove(activity);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_left) {
            clickIvLeft();

        } else if (i == R.id.tv_right) {
            clickTvRight();

        } else if (i == R.id.tv_left) {
            clickTvLeft();

        } else if (i == R.id.iv_right) {
            clickIvRight();

        } else if (i == R.id.rl_error) {
            clickError();
        }
    }

    @Override
    public void toast(String toast, String... location) {
        try {
            ToastUtils.showMessage(toast, location);
        } catch (WindowManager.BadTokenException e) {
        }
    }

    @Override
    public void clickIvLeft() {//点击标题左边图片
        finish();
    }

    @Override
    public void clickTvRight() {//点击标题左边文字
    }

    @Override
    public void clickIvRight() {//点击标题右边图片
    }

    @Override
    public void clickTvLeft() {//点击标题右边文字
        finish();
    }


    @Override
    public void clickError() {//点击错误界面
    }

    @Override
    public void setLeftImage(int id) {//设置标题左边图片
        ivLeft.setImageResource(id);
        ivLeft.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLeftTv(String left) {//设置标题左边文字
        tvLeft.setText(left);
        tvLeft.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLeftImage() {//隐藏左边图片
        ivLeft.setVisibility(View.GONE);
    }

    @Override
    public void hideLeftTv() {//隐藏标题左边文字
        tvLeft.setVisibility(View.GONE);
    }

    @Override
    public void setRightImage(int id) {//设置标题右边图片
        ivRight.setImageResource(id);
        ivRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRightTv(String right) {//设置标题右边文字
        tvRight.setText(right);
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRightImage() {//隐藏标题右边图片
        ivRight.setVisibility(View.GONE);
    }

    @Override
    public void hideRightTv() {//隐藏标题右边文字
        tvRight.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(String title) {//设置标题
        tvTitle.setText(title);
    }

    public void setTitleColor(int color) {//设置标题颜色
        tvTitle.setTextColor(AppHelper.getColor(color));
    }

    @Override
    public void showTitle() {//显示标题
        rlTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTitle() {//隐藏标题
        rlTitle.setVisibility(View.GONE);
    }

    @Override
    public void setRightTvColor(int id) {//设置标题右边文字的颜色
        tvRight.setTextColor(AppHelper.getColor(id));
    }

    @Override
    public void setLeftTvColor(int id) {//设置标题左边文字的颜色
        tvLeft.setTextColor(AppHelper.getColor(id));
    }

    @Override
    public void showError() {
        try {
            vsError.inflate();     //inflate 方法只能被调用一次，
            ivError = findViewById(R.id.iv_error);
            tvErrorTitle = findViewById(R.id.tv_error_title);
            tvErrorContent = findViewById(R.id.tv_error_content);
            rlError = findViewById(R.id.rl_error);
            tvError = findViewById(R.id.tv_error);
            rlError.setOnClickListener(this);
        } catch (Exception e) {
            vsError.setVisibility(View.VISIBLE);
        } finally {
        }
    }

    @Override
    public void setError(int... ids) {
        showError();
        ivError.setImageResource(ids[0]);
        tvErrorTitle.setText(AppHelper.getString(ids[1]));
        if (ids.length == 3) {
            tvErrorContent.setVisibility(View.VISIBLE);
            tvErrorContent.setText(AppHelper.getString(ids[2]));
        } else if (ids.length == 4) {
            tvErrorContent.setVisibility(View.VISIBLE);
            tvErrorContent.setText(AppHelper.getString(ids[2]));
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(AppHelper.getString(ids[3]));
            tvError.setOnClickListener(this);
        }
    }

    @Override
    public void hideError() {
        vsError.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        try {
            vsLoading.inflate();     //inflate 方法只能被调用一次，
            rlLoading = findViewById(R.id.rl_loading);
            rlLoading.setOnClickListener(this);
        } catch (Exception e) {
            vsLoading.setVisibility(View.VISIBLE);
        } finally {
        }
    }

    @Override
    public void hideLoading() {
        vsLoading.setVisibility(View.GONE);
    }

    /**
     * 网络变化
     *
     * @param netBean
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void netChangeBus(NetBean netBean) {
        this.netModile = netBean.netState;
        if (netModile == 1) {
            LogUtil.i("已切换至Wi-Fi");
        } else if (netModile == 0) {
            LogUtil.i("已切换至移动网络，请注意流量消耗");
        } else if (netModile == -1) {
            LogUtil.i("网络已断开");
        }
    }

    /**
     * 打开键盘
     **/
    public void showInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideInputForce() {
        if (this == null || this.getCurrentFocus() == null)
            return;

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取String传值
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        if (getIntent() == null || StringUtil.isEmpty(getIntent().getStringExtra(key))) {
            return "";
        }
        return getIntent().getStringExtra(key);
    }

    /**
     * 获取long传值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLong(String key, int defaultValue) {
        return getIntent().getLongExtra(key, defaultValue);
    }

    /**
     * 获取int传值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        return getIntent().getIntExtra(key, defaultValue);
    }

    /**
     * 获取double传值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public double getDouble(String key, int defaultValue) {
        return getIntent().getDoubleExtra(key, defaultValue);
    }

    /**
     * 获取对象传值
     *
     * @param key
     * @return
     */
    public Object getObj(String key) {
        return getIntent().getSerializableExtra(key);
    }

    /**
     * 获取垂直的recycleview布局
     *
     * @return
     */
    public LinearLayoutManager getVertiaclManager() {
        LinearLayoutManager linearL = new LinearLayoutManager(this);
        linearL.setOrientation(LinearLayoutManager.VERTICAL);
        return linearL;
    }

    /**
     * 获取水平的recycleview布局
     *
     * @return
     */
    public LinearLayoutManager getHorizontallManager() {
        LinearLayoutManager linearL = new LinearLayoutManager(this);
        linearL.setOrientation(LinearLayoutManager.HORIZONTAL);
        return linearL;
    }

    /**
     * 获取recycleview网格布局
     *
     * @param count
     * @return
     */
    public GridLayoutManager getGridManager(int count) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, count);
        return gridLayoutManager;
    }

    /**
     * 关闭app杀死所有activity
     */
    public static void closeApp() {
        for (AppCompatActivity activity : BaseActivity.activities) {
            if (!activity.isFinishing())
                activity.finish();
        }
        BaseActivity.activities.clear();
    }

    public void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
    }



    private Map<Integer, CallBack> callBack = new HashMap<>();

    //权限
    public void applyPermissions(int code, CallBack callBack, String[] pers) throws RuntimeException, Error {
        try {
            this.callBack.put(code, callBack);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!isPermissions(pers)) {
                    ActivityCompat.requestPermissions(this,
                            pers,
                            code);
                } else if (callBack != null) {
                    callBack.callBack(code, true);
                }
            } else if (callBack != null) {
                callBack.callBack(code, true);
            }
        } catch (OutOfMemoryError outOfMemoryError) {
            LogUtil.e("内存不足");
        } catch (NullPointerException e) {
            LogUtil.e("空指针异常");
        } catch (NumberFormatException e) {
            LogUtil.e("数字格式异常");
        } catch (ArrayStoreException e0) {
            LogUtil.e("向数组中存放与声明类型不兼容对象异常");
        } catch (ClassCastException e1) {
            LogUtil.e("类型强制转换异常");
        } catch (IllegalArgumentException e2) {
            LogUtil.e("传递非法参数异常");
        } catch (ArithmeticException e3) {
            LogUtil.e("算术运算异常");
        } catch (IndexOutOfBoundsException e3) {
            LogUtil.e("下标越界异常");
        } catch (NegativeArraySizeException e3) {
            LogUtil.e("创建一个大小为负数的数组错误异常");
        } catch (SecurityException e3) {
            callBack.callBack(code, false);
            LogUtil.e("安全异常");
        } catch (UnsupportedOperationException e) {
            callBack.callBack(code, false);
            LogUtil.e("不支持的操作异常");
        } catch (RuntimeException err) {
            callBack.callBack(code, false);
        }
    }

    public boolean isPermissions(String[] per) {
        for (int i = 0; i < per.length; i++) {
            if (ContextCompat.checkSelfPermission(this, per[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean isPermissions(int[] per) {
        for (int i = 0; i < per.length; i++) {
            if (per[i] != PackageManager.PERMISSION_GRANTED) {
                //判断是否勾选禁止后不再询问
                return false;
            }
        }
        return true;
    }

    public interface CallBack {
        void callBack(int tag, boolean isgranted);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (callBack.get(requestCode) != null) {
            callBack.get(requestCode).callBack(requestCode, isPermissions(grantResults));
        }
    }

}
