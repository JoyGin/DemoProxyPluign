package com.joygin.demoproxypluign;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.joygin.plugin.PluginManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Host_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String pluginApkPath = loadPluginApkFile();

        PluginManager.getInstance().init(this);
        PluginManager.getInstance().loadPluginApk(pluginApkPath);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = "com.joygin.plugindemo";
                String activityName = "com.joygin.plugindemo.MainActivity";
                PluginManager.getInstance().startActivity(packageName, activityName);
            }
        });
    }

    /**
     * 加载 apk 插件
     * @return
     */
    private String loadPluginApkFile() {
        File apkFile = new File(getFilesDir().getAbsolutePath(), "plugindemo.apk");
        if (!apkFile.exists()) {
            try {
                InputStream inputStream = getAssets().open("plugindemo.apk");
                // 把 inputStream 的内容写到 file 中
                OutputStream outputStream = new FileOutputStream(apkFile);

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // 关闭 output stream 和 input stream
                outputStream.close();
                inputStream.close();
                Log.i(TAG, "success to load apk file from assets to files dir of app");
            } catch (IOException e) {
                Log.e(TAG, "fail to load apk file from assets to files dir of app, exception = " + e.getMessage());
            }
        }
        return apkFile.getAbsolutePath();
    }
}