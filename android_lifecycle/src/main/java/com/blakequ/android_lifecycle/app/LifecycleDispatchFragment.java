package com.blakequ.android_lifecycle.app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blakequ.android_lifecycle.LifecycleDispatcher;

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
 * date     : 2017/3/3 18:52 <br>
 * last modify author : <br>
 * version : 1.0 <br>
 * description:
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class LifecycleDispatchFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        LifecycleDispatcher.get().onFragmentStarted(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LifecycleDispatcher.get().onFragmentResumed(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LifecycleDispatcher.get().onFragmentPaused(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        LifecycleDispatcher.get().onFragmentStopped(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LifecycleDispatcher.get().onFragmentSaveInstanceState(this, outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LifecycleDispatcher.get().onFragmentActivityCreated(this, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LifecycleDispatcher.get().onFragmentViewCreated(this, view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LifecycleDispatcher.get().onFragmentAttach(this, context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LifecycleDispatcher.get().onFragmentDetach(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LifecycleDispatcher.get().onFragmentCreated(this, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LifecycleDispatcher.get().onFragmentDestroyView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LifecycleDispatcher.get().onFragmentCreateView(this, inflater, container, savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        LifecycleDispatcher.get().onFragmentDestroyed(this);
    }
}
