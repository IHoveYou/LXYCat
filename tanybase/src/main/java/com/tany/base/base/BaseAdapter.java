package com.tany.base.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Tany on 2018/3/20.
 * Desc:万能适配器
 */


public abstract class BaseAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter {
    protected Context mContext;
    protected int mLayoutId;
    public List<T> mDatas;
    protected LayoutInflater mInflater;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;

    public BaseAdapter(Context context, List<T> datas, int layoutId) {
        mContext = context;
        if (context != null)
            mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        B mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), mLayoutId, parent, false);
        ViewHolder holder = new ViewHolder(mContext, mBinding.getRoot());
        holder.setAdapter(this);
        setListener(parent, holder, viewType);
        return holder;
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    protected void setListener(ViewGroup parent, final ViewHolder holder, int viewType) {
        if (!isEnabled(viewType)) return;
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, holder, position);
                }
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int position = holder.getAdapterPosition();
                    return mOnItemLongClickListener.onItemLongClick(v, holder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        B binding = DataBindingUtil.getBinding(holder.itemView);
        if (position >= mDatas.size()) {
            onBindItem((ViewHolder) holder, binding, null, position);
        } else {
            onBindItem((ViewHolder) holder, binding, mDatas.get(position), position);
        }
        binding.executePendingBindings();
    }

    protected abstract void onBindItem(ViewHolder holder, B binding, T t, int position);

    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemClickListener) {
        this.mOnItemLongClickListener = onItemClickListener;
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(BaseAdapter adapter, View view, int position);
    }

    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    public void setmDatas(List<T> datas) {
        if (mDatas == null) {
            mDatas = datas;
        } else {
            mDatas.addAll(datas);
        }
        notifyItemRangeChanged(mDatas.size() - datas.size(), mDatas.size());
    }

    public void refresh(List<T> datas) {
        if (mDatas == null) {
            mDatas = datas;
        } else {
            mDatas.clear();
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }
}
