package com.blakequ.activitylifecyclecallbackscompat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blakequ.activitylifecyclecallbackscompat.R;
import com.blakequ.activitylifecyclecallbackscompat.util.GarbageUtils;
import com.blakequ.android_lifecycle.app.LifecycleDispatchActivity;

public class MainActivity extends LifecycleDispatchActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityPage2.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        View rootView = GarbageUtils.getRootView(this);
        if (rootView != null){
            GarbageUtils.unBindDrawables(rootView);
            GarbageUtils.unBindListener(rootView);
        }
    }
}
