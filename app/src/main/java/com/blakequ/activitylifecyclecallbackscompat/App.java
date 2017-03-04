package com.blakequ.activitylifecyclecallbackscompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.blakequ.activitylifecyclecallbackscompat.ui.BaseService;
import com.blakequ.activitylifecyclecallbackscompat.util.CrashlyticsTree;
import com.blakequ.activitylifecyclecallbackscompat.util.StrictModeUtil;
import com.blakequ.android_lifecycle.ActivityLifecycleCallbacksAdapter;
import com.blakequ.android_lifecycle.FragmentLifecycleCallbacksAdapter;
import com.blakequ.android_lifecycle.LifecycleDispatcher;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (C) BlakeQu All Rights Reserved <blakequ@gmail.com>
 * <p>
 * Licensed under the blakequ.com License, Version 1.0 (the "License");
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * author  : quhao <blakequ@gmail.com> <br>
 * date     : 2017/3/4 14:44 <br>
 * last modify author : <br>
 * version : 1.0 <br>
 * description:
 */

public class App extends Application {

    //activity or frgament
    public static List<Object> totalList = new LinkedList();
    //启用内存监控
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        StrictModeUtil.init(BuildConfig.DEBUG);

        //register Activity and Fragment lifecycle callback, replace registerActivityLifecycleCallbacks
        LifecycleDispatcher.registerActivityLifecycleCallbacks(this, activityLifecycleCallbacksAdapter);
        LifecycleDispatcher.registerFragmentLifecycleCallbacks(this, fragmentLifecycleCallbacksAdapter);

        //日志开关
        if (!BuildConfig.DEBUG) {
            // for release
            Logger.plant(new CrashlyticsTree());
        }else {
            Logger.plantDefaultDebugTree(new Settings()
                    .isShowMethodLink(true)
                    .isShowThreadInfo(false)
                    .setMethodOffset(0)
                    .setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT));
            //Logger.plantDefaultDebugTree();
        }

        initUmeng();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LifecycleDispatcher.unregisterActivityLifecycleCallbacks(this, activityLifecycleCallbacksAdapter);
        LifecycleDispatcher.unregisterFragmentLifecycleCallbacks(this, fragmentLifecycleCallbacksAdapter);
    }

    /**
     * exit app, clean context and so on
     * <p>
     * Title: exitApp
     * <p>
     * Description:
     *
     * @param context
     */
    @SuppressLint("NewApi")
    public static void exitApp(Context context) {
        Logger.d("Application exit");

        // 退出所有Activity或Service
        clearContextStack();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(context.getPackageName());
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 清除Activity堆栈
     * <p>Title: clearActivityStack
     * <p>Description:
     */
    public static void clearContextStack(){
        for (int i = 0; i < totalList.size(); i++) {
            if (totalList.get(i) instanceof Activity) {
                ((Activity) totalList.get(i)).finish();
            }else if(totalList.get(i) instanceof BaseService){
                BaseService service = ((BaseService) totalList.get(i));
                service.exitApp();
                if (service.isAutoFinish()) {
                    service.stopSelf();
                }
            }
        }
        totalList.clear();
    }

    /**
     * 清除service
     * <p>Title: clearActivityStack
     * <p>Description:
     */
    public static void clearService(){
        for (int i = 0; i < totalList.size(); i++) {
            if(totalList.get(i) instanceof BaseService){
                BaseService service = ((BaseService) totalList.get(i));
                if (service.isAutoFinish()) {
                    service.exitApp();
                    service.stopSelf();
                }
                totalList.remove(i);
            }
        }
    }

    /**
     * 当前应用Activity是否存在
     * @param context
     * @return true表示不存在
     */
    public static boolean isActivityDead(Context context) {
        int num = 0;
        if (totalList != null){
            for (Object task: totalList){
                if (task instanceof Activity){
                    num++;
                }
            }
        }

        if (num == 0){
            return true;
        }
        return false;
    }

    private void initUmeng(){
        //友盟
        MobclickAgent.openActivityDurationTrack(false);
        //友盟
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, "appId",
                "appChannel", MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent. startWithConfigure(config);
        MobclickAgent.enableEncrypt(true);
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
    }

    private ActivityLifecycleCallbacksAdapter activityLifecycleCallbacksAdapter = new ActivityLifecycleCallbacksAdapter() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Logger.d("onActivityCreated "+activity.getClass().getSimpleName());
            totalList.add(activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Logger.d("onActivityDestroyed "+activity.getClass().getSimpleName());
            totalList.remove(activity);

            refWatcher.watch(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            super.onActivityResumed(activity);
            MobclickAgent.onResume(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            super.onActivityPaused(activity);
            MobclickAgent.onPause(activity);
        }
    };

    private FragmentLifecycleCallbacksAdapter fragmentLifecycleCallbacksAdapter = new FragmentLifecycleCallbacksAdapter(){

        @Override
        public void onFragmentCreated(Fragment fragment, Bundle savedInstanceState) {
            Logger.d("onFragmentCreated "+fragment.getClass().getSimpleName());
            totalList.add(fragment);
        }

        @Override
        public void onFragmentDestroyed(Fragment fragment) {
            Logger.d("onFragmentDestroyed "+fragment.getClass().getSimpleName());
            totalList.remove(fragment);

            refWatcher.watch(fragment);
        }

        @Override
        public void onFragmentResumed(Fragment fragment) {
            super.onFragmentResumed(fragment);
            MobclickAgent.onPageStart(fragment.getClass().getSimpleName());
        }

        @Override
        public void onFragmentPaused(Fragment fragment) {
            super.onFragmentPaused(fragment);
            MobclickAgent.onPageEnd(fragment.getClass().getSimpleName());
        }
    };
}
