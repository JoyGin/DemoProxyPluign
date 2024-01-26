package com.joygin.plugin;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 容器 activity，代理插件 activity 的所有方法
 */
public class ContainerActivity extends AppCompatActivity {

    private PluginActivity mPluginActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String packageName = getIntent().getStringExtra(PluginConstant.PLUGIN_PACKAGE_NAME);
        String activityName = getIntent().getStringExtra(PluginConstant.PLUGIN_ACTIVITY_NAME);

        mPluginActivity = loadPluginActivity(packageName, activityName);
        mPluginActivity.onCreate(savedInstanceState);
    }

    /**
     * 加载插件 activity
     *
     * @param packageName 插件 activity 所在 apk 的包名
     * @param activityName 插件 activity 的全路径类名
     * @return 插件 activity 实例
     */
    public PluginActivity loadPluginActivity(String packageName, String activityName) {
        PluginActivity pluginActivity = null;
        PluginInfo pluginInfo = PluginManager.getInstance().getPlugin(packageName);
        if (pluginInfo != null) {
            try {
                Class<?> aClass = pluginInfo.getClassLoader().loadClass(activityName);
                pluginActivity = (PluginActivity) aClass.newInstance();
                pluginActivity.attach(this, pluginInfo);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                Log.e(PluginConstant.TAG, "loadActivityClass fail, exception: " + e.getMessage());
            }
        }
        return pluginActivity;
    }

    @Override
    public Resources getResources() {
        if (mPluginActivity != null && mPluginActivity.getResources() != null) {
            return mPluginActivity.getResources();
        }
        return super.getResources();
    }

    // ==================================================
    // 代理执行插件 activity 的方法
    // ==================================================

    @Override
    public void onStart() {
        super.onStart();
        mPluginActivity.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPluginActivity.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPluginActivity.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPluginActivity.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPluginActivity.onDestroy();
    }
}
