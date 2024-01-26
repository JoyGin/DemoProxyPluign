package com.joygin.plugin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.util.HashMap;

/**
 * 负责加载插件apk，包括如下功能：
 * 1.加载插件中的类
 * 2.加载插件中的资源
 * 3.提供启动插件中四大组件的方法
 */
public class PluginManager {

    private HashMap<String, PluginInfo> mPlugins = new HashMap<>();
    private Context mContext;

    private PluginManager() {
    }

    private interface Holder {

        PluginManager INSTANCE = new PluginManager();
    }

    public static PluginManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 加载插件 apk
     *
     * @param pluginApkPath plugin apk path
     */
    public void loadPluginApk(String pluginApkPath) {
        // 插件已加载
        if (mPlugins.get(pluginApkPath) != null) {
            return;
        }
        // 加载插件
        if (!new File(pluginApkPath).exists()) {
            Log.e(PluginConstant.TAG, "pluginApkPath = " + pluginApkPath + " 不存在");
        }
        Log.i(PluginConstant.TAG, "pluginApkPath = " + pluginApkPath + " exists");
        // 1.加载插件中的类
        DexClassLoader dexClassLoader = loadPluginClassLoader(pluginApkPath);
        // 2.加载插件中的资源
        Resources resources = loadPluginResources(pluginApkPath);
        String packageName = getPackageName(pluginApkPath);
        PluginInfo pluginInfo = new PluginInfo(pluginApkPath, packageName, resources, dexClassLoader);
        mPlugins.put(pluginInfo.getPackageName(), pluginInfo);
    }

    public PluginInfo getPlugin(String packageName) {
        return mPlugins.get(packageName);
    }

    /**
     * 加载插件中的类
     *
     * @return 插件类加载器
     */
    private DexClassLoader loadPluginClassLoader(String pluginApkPath) {
        String nativeLibDir = new File(mContext.getFilesDir(), "pluginlib").getAbsolutePath();
        String dexOutPath = new File(mContext.getFilesDir(), "dexout").getAbsolutePath();
        // 生成 DexClassLoader 用来加载插件类
        DexClassLoader pluginClassLoader = new DexClassLoader(pluginApkPath, dexOutPath, nativeLibDir,
                getClass().getClassLoader());
        return pluginClassLoader;
    }

    /**
     * 加载插件中的资源
     *
     * @return 插件资源
     */
    private Resources loadPluginResources(String pluginApkPath) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(
                pluginApkPath,
                PackageManager.GET_ACTIVITIES
                        | PackageManager.GET_META_DATA
                        | PackageManager.GET_SERVICES
                        | PackageManager.GET_PROVIDERS
                        | PackageManager.GET_SIGNATURES
        );
        if (packageArchiveInfo != null) {
            packageArchiveInfo.applicationInfo.sourceDir = pluginApkPath;
            packageArchiveInfo.applicationInfo.publicSourceDir = pluginApkPath;
            Resources injectResources = null;
            try {
                injectResources = packageManager.getResourcesForApplication(packageArchiveInfo.applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                // ...
                Log.e(PluginConstant.TAG, "loadPluginResources: exception=" + e.getMessage());
            }
            return injectResources;
        } else {
            Log.e(PluginConstant.TAG, "packageArchiveInfo == null, pluginApkPath = " + pluginApkPath);
            return null;
        }
    }

    /**
     * 获取包名
     *
     * @param pluginApkPath 插件 apk 文件路径
     * @return 包名
     */
    private String getPackageName(String pluginApkPath) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(
                pluginApkPath,
                PackageManager.GET_ACTIVITIES
                        | PackageManager.GET_META_DATA
                        | PackageManager.GET_SERVICES
                        | PackageManager.GET_PROVIDERS
                        | PackageManager.GET_SIGNATURES
        );
        if (packageArchiveInfo != null) {
            return packageArchiveInfo.packageName;
        }
        return null;
    }

    /**
     * 启动插件中的 activity
     *
     * @param packageName 包名
     * @param activityName activity类名
     */
    public void startActivity(String packageName, String activityName) {
        PluginInfo pluginInfo = mPlugins.get(packageName);
        if (pluginInfo == null) {
            Log.e(PluginConstant.TAG, "not found package: " + packageName + " plugin");
            return;
        }
        Intent intent = new Intent(mContext, ContainerActivity.class);
        intent.putExtra(PluginConstant.PLUGIN_PACKAGE_NAME, packageName);
        intent.putExtra(PluginConstant.PLUGIN_ACTIVITY_NAME, activityName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
