package com.blakequ.activitylifecyclecallbackscompat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.blakequ.activitylifecyclecallbackscompat.R;
import com.blakequ.android_lifecycle.app.LifecycleDispatchActivity;

import java.util.ArrayList;
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
 * date     : 2017/3/4 15:44 <br>
 * last modify author : <br>
 * version : 1.0 <br>
 * description:
 */

public class ActivityPage2 extends LifecycleDispatchActivity {
    private int index = 0;
    private Fragment currentFragment;
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        fragments.add(Fragment1.newInstance(index));
        fragments.add(Fragment2.newInstance(index));
        findViewById(R.id.frgament_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0){
                    index = 1;
                }else {
                    index = 0;
                }
                replaceFragment(fragments.get(index));
            }
        });
        currentFragment = fragments.get(0);
        getSupportFragmentManager().beginTransaction().add(R.id.details, currentFragment).commit();
    }

    private void replaceFragment(Fragment targetFragment) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!targetFragment.isAdded()) {
                transaction
                        .hide(currentFragment)
                        .add(R.id.details, targetFragment)
                        .commit();
            } else {
                transaction
                        .hide(currentFragment)
                        .show(targetFragment)
                        .commit();
            }
            currentFragment = targetFragment;
        } catch (Exception e) {
            // TODO: handle exception

        }
    }
}
