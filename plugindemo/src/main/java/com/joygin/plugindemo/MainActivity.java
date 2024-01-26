package com.joygin.plugindemo;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import com.joygin.plugin.PluginActivity;

public class MainActivity extends PluginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这里不能用 this，待进一步实现
//                Toast.makeText(this, getString(R.string.app_name), Toast.LENGTH_LONG).show();
                Toast.makeText(getContainer(), getString(R.string.app_name), Toast.LENGTH_LONG).show();
            }
        });
    }
}