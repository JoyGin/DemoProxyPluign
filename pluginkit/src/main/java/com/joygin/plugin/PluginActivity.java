package com.joygin.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.StringRes;
import java.lang.ref.WeakReference;

/**
 * 插件 activity，代理
 */
abstract public class PluginActivity {

    private WeakReference<Activity> mContainerActivity;
    private PluginInfo mPluginInfo;

    /**
     * 链接容器 activity 和 插件 activity
     *
     * @param containerActivity 容易 activity
     * @param pluginInfo 插件信息
     */
    final public void attach(Activity containerActivity, PluginInfo pluginInfo) {
        mContainerActivity = new WeakReference<>(containerActivity);
        mPluginInfo = pluginInfo;
    }

    final public Activity getContainer() {
        return mContainerActivity.get();
    }

    // ==================================================
    // 以下方法委托给 ContainerActivity
    // ==================================================

    public void setContentView(int layoutResID) {
        mContainerActivity.get().setContentView(layoutResID);
    }

    public void setContentView(View view) {
        mContainerActivity.get().setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContainerActivity.get().setContentView(view, params);
    }

    public <T extends View> T findViewById(int id) {
        return mContainerActivity.get().findViewById(id);
    }

    public Resources getResources() {
        return mPluginInfo.getResources();
    }

    public final String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    public WindowManager getWindowManager() {
        return mContainerActivity.get().getWindowManager();
    }

    public ClassLoader getClassLoader() {
        return mContainerActivity.get().getClassLoader();
    }

    public Context getApplicationContext() {
        return mContainerActivity.get().getApplicationContext();
    }

    public MenuInflater getMenuInflater() {
        return mContainerActivity.get().getMenuInflater();
    }

    public Window getWindow() {
        return mContainerActivity.get().getWindow();
    }

    public Intent getIntent() {
        return mContainerActivity.get().getIntent();
    }

    public LayoutInflater getLayoutInflater() {
        return mContainerActivity.get().getLayoutInflater();
    }

    public String getPackageName() {
        return mPluginInfo.getPackageName();
    }

    // ==================================================
    // 以下方法有 ContainerActivity 代理执行
    // ==================================================

    protected void onCreate(Bundle savedInstanceState) {

    }

    protected void onStart() {

    }

    protected void onResume() {

    }

    protected void onStop() {

    }

    protected void onPause() {

    }

    protected void onDestroy() {

    }
}
