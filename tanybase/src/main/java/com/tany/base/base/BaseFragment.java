package com.tany.base.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tany.base.R;
import com.tany.base.bean.NetBean;
import com.tany.base.utils.AppHelper;
import com.tany.base.utils.LogUtil;
import com.tany.base.utils.NetUtil;
import com.tany.base.utils.ToastUtils;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Tany on 2018/11/5.
 * Desc:
 */


public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseVM> extends Fragment implements BaseFraView, View.OnClickListener {
    protected Context mContext;
    public RelativeLayout rl_content;
    public RelativeLayout rlTop;
    private RelativeLayout rlError;
    private RelativeLayout rlLoading;
    public int netModile;
    private ImageView ivError;
    private TextView tvErrorTitle;
    private TextView tvErrorContent;
    private TextView tvError;
    public B mBinding;
    public VM mVM;
    public View contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        View view = inflater.inflate(R.layout.fragment_base, null);
        rl_content = view.findViewById(R.id.rl_content_fra);
        rlTop = view.findViewById(R.id.rl_top_fra);
        rlError = view.findViewById(R.id.rl_error_fra);
        rlLoading = view.findViewById(R.id.rl_loading_fra);
        ivError = view.findViewById(R.id.iv_error);
        tvErrorTitle = view.findViewById(R.id.tv_error_title);
        tvErrorContent = view.findViewById(R.id.tv_error_content);
        tvError = view.findViewById(R.id.tv_error);
        rlError.setOnClickListener(this);
        contentView = setContentView(inflater);
        if (contentView != null) {
            setContentLayout(contentView);
        }
        mVM = createVM();
        return view;
    }

    /**
     * 内容区域
     */
    public void setContentLayout(View contentView) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(layoutParams);
        if (null != rl_content) {
            rl_content.addView(contentView);
            if (DataBindingUtil.getBinding(contentView) != null
                    || (contentView.getTag() instanceof String))
                mBinding = DataBindingUtil.bind(contentView);
        }
    }

    public abstract View setContentView(LayoutInflater inflater);

    protected abstract VM createVM();

    /**
     * 子类在此方法中进行界面初始化
     */
    protected abstract void initView();

    /**
     * 子类在此方法中实现数据的初始化
     */
    public abstract void initData();

    /**
     * 当Activity初始化之后可以在这里进行一些数据的初始化操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        try {
            init();
//        } catch (Exception e) {
//            LogUtil.e("Error", e.getMessage() + "");
//        }
    }

    private void init() {
        netModile = NetUtil.getNetWrokState(mContext);
        initView();
        if (netModile == -1) {
        } else {
            initData();
        }
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroyView();
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

    @Override
    public void toast(String toast, String... location) {
        ToastUtils.showMessage(toast, location);
    }

    @Override
    public void showLoading() {
        rlLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        rlLoading.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        rlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        rlError.setVisibility(View.GONE);
    }

    @Override
    public void setError(int... ids) {
        rlError.setVisibility(View.VISIBLE);
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
    public void clickError() {
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_error) {
            clickError();
        }
    }

    public void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
    }

    /**
     * 隐藏软键盘
     */
    public void hideInputForce() {
        if (getActivity() == null || getActivity().getCurrentFocus() == null)
            return;

        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 打开键盘
     **/
    public void showInput(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public LinearLayoutManager getVertiaclManager() {
        LinearLayoutManager linearL = new LinearLayoutManager(getActivity());
        linearL.setOrientation(LinearLayoutManager.VERTICAL);
        return linearL;
    }

    public LinearLayoutManager getHorizontallManager() {
        LinearLayoutManager linearL = new LinearLayoutManager(getActivity());
        linearL.setOrientation(LinearLayoutManager.HORIZONTAL);
        return linearL;
    }

    public GridLayoutManager getGridManager(int count) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), count);
        return gridLayoutManager;
    }
}
