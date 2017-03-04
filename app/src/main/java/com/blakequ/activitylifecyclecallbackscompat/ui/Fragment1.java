package com.blakequ.activitylifecyclecallbackscompat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blakequ.activitylifecyclecallbackscompat.R;

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
 * date     : 2017/3/4 15:58 <br>
 * last modify author : <br>
 * version : 1.0 <br>
 * description:
 */

public class Fragment1 extends BaseFragment {

    /**
     * Create a new instance of Fragment, initialized to
     * show the text at 'index'.
     */
    public static Fragment1 newInstance(int index) {
        Fragment1 f = new Fragment1();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment1, null);
    }

    @Override
    protected void initView(View parentView) {

    }
}
