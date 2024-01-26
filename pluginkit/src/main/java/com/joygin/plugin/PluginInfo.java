package com.joygin.plugin;

import android.content.res.Resources;
import dalvik.system.DexClassLoader;

/**
 * 插件信息类
 */
public class PluginInfo {

    private final String path;
    private final String packageName;
    private final Resources resources;
    private final DexClassLoader classLoader;

    public PluginInfo(String path, String packageName, Resources resources, DexClassLoader classLoader) {
        this.path = path;
        this.packageName = packageName;
        this.resources = resources;
        this.classLoader = classLoader;
    }

    public String getPath() {
        return path;
    }

    public String getPackageName() {
        return packageName;
    }

    public Resources getResources() {
        return resources;
    }

    public DexClassLoader getClassLoader() {
        return classLoader;
    }
}
