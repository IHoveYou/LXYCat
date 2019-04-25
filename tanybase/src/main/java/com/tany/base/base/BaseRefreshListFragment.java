package com.tany.base.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tany.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tany on 2018/11/6.
 * Desc:
 */


public abstract class BaseRefreshListFragment<T extends Object, A extends BaseAdapter, FVM extends BaseVM> extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    protected T t;
    protected A adapter;
    protected FVM mFVM;
    protected List<T> list = new ArrayList<>();
    protected int page = 0;
    protected int type = 0;//0初始化1 刷新 2加载
    private SmartRefreshLayout smart;
    private View view;
    protected boolean isRefresh = true;//是否需要刷新
    protected boolean isLoadMore = true;//是否需要加载更多

    @Override
    public View setContentView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.activity_base_refreshlist, null);
        return view;
    }

    @Override
    protected BaseVM createVM() {
        mFVM = createFVM();
        return null;
    }

    @Override
    protected void initView() {
        initMyView();
        smart = view.findViewById(R.id.smart);
        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        smart.setEnableLoadMore(isLoadMore);
        smart.setEnableRefresh(isRefresh);
        smart.setOnRefreshListener(this);
        smart.setOnLoadMoreListener(this);
        smart.setEnableAutoLoadMore(true);
        recyclerview.setLayoutManager(getVertiaclManager());
        adapter = initAdapter();
        recyclerview.setAdapter(adapter);
    }

    protected abstract void initMyView();

    @Override
    public void initData() {
        type = 0;
        page = 0;
        getData(page, true);
    }

    public void initList(List<T> beans) {
        if (type != 2) {
            list.clear();
        }
        list.addAll(beans);
        adapter.notifyDataSetChanged();
    }

    protected abstract FVM createFVM();

    protected abstract A initAdapter();

    protected abstract void getData(int page, boolean isShow);

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        type = 1;
        page = 0;
        getData(page, false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        type = 2;
        page = page + 1;
        getData(page, false);
    }

    public void finishLoad() {
        if (smart != null) {
            smart.finishRefresh();
            smart.finishLoadMore();
        }
    }
}
