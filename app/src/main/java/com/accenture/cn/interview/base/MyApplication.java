package com.accenture.cn.interview.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.accenture.cn.interview.BuildConfig;
import com.accenture.cn.interview.utils.FileUtil;
import com.accenture.cn.interview.widget.SupportMultipleScreensUtil;
import com.socks.library.KLog;

/**
 * Created by chengyou.huang on 2016/12/26.
 */

public class MyApplication extends Application {

    private static String TAG = "com.accenture.cn.interview";
    public static int APP_STATE_ATY = -1;
    public static int countAty = 0;
    public static String saveDriPath = null;
    public static Context CONTEXT = null;
    private volatile static MyApplication mInstance = null;


    public static MyApplication instance() {

        MyApplication inst = mInstance;
        if (inst == null) {
            synchronized (MyApplication.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new MyApplication();
                    mInstance = inst;
                }
            }
        }
        return inst;
    }


    public MyApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();
        saveDriPath = FileUtil.getDiskCacheDir(CONTEXT);
        SupportMultipleScreensUtil.init(CONTEXT);
        KLog.init(BuildConfig.LOG_DEBUG);
        KLog.i(MyApplication.TAG, "saveDriPath----" + saveDriPath);
        registerActivityLifecycleCallbacks(new AppStatusTracker());
    }


    public class AppStatusTracker implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            KLog.i(activity.toString(), "countAty---onActivityCreated:" + countAty);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            countAty++;
            KLog.i(activity.toString(), "countAty---onActivityStarted:" + countAty);
        }

        @Override
        public void onActivityResumed(Activity activity) {

            KLog.i(activity.toString(), "countAty---onActivityResumed:" + countAty);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            KLog.i(activity.toString(), "countAty---onActivityPaused:" + countAty);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            countAty--;
            KLog.i(activity.toString(), "countAty---onActivityStopped:" + countAty);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            KLog.i(activity.toString(), "countAty---onActivitySaveInstanceState:" + countAty);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            KLog.i(activity.toString(), "countAty---onActivityDestroyed:" + countAty);
            if (countAty == 0) {
                countAty--;
                //标识app被强杀或是主动退出app
                KLog.i(activity.toString(), "countAty---onActivityDestroyed---kill:" + countAty);
            }
        }
    }

}
