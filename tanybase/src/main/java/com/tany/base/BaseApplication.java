package com.tany.base;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tany.base.receiver.ConnectionChangeReceiver;
import com.tany.base.tanyutils.TanyUtil;

/**
 * Created by Tany on 2018/6/4.
 * Desc:
 */


public class BaseApplication extends Application {
    private static BaseApplication application;

    public static synchronized BaseApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = (BaseApplication) getApplicationContext();
        TanyUtil.init(this);
        /**
         *解决未捕获异常引起的崩溃
         */
//        CrashHandlerUtil crashHandlerUtil = CrashHandlerUtil.getInstance();
//        crashHandlerUtil.init(getApplicationContext());

        /**
         * 动态注册网络监听广播
         */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");  //添加要收到的广播
        ConnectionChangeReceiver networkChangeReceiver = new ConnectionChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, R.color.gray1);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc(); // 垃圾回收
    }
}
