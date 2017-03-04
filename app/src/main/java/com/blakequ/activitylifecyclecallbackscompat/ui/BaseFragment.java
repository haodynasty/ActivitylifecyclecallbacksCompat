package com.blakequ.activitylifecyclecallbackscompat.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.blakequ.activitylifecyclecallbackscompat.util.GarbageUtils;
import com.blakequ.android_lifecycle.app.LifecycleDispatchFragment;

import java.lang.reflect.Field;

/**
 * 基本Framgent类
 */
public abstract class BaseFragment extends LifecycleDispatchFragment implements OnClickListener {
	protected View mBaseView;
	//避免ViewPager在一开始创建,在可见的时候加载，默认开启
	private boolean hasLazyLoad = true;
	protected FragmentActivity mActivity;

	//必须有空的构造函数
	public BaseFragment() {
	}

	/**
	 * 必须实现，需要载入root视图
	 * <p>Title: inflaterView
	 * <p>Description: 
	 * @param inflater
	 * @param container
	 * @param bundle
	 * @return
	 */
	protected abstract View inflaterView(LayoutInflater inflater,
            ViewGroup container, Bundle bundle);

	/**
     * initialization widget, you should look like parentView.findviewbyid(id);
     * call method
     * 
     * @param parentView
     */
    protected abstract void initView(View parentView);

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof FragmentActivity){
			mActivity = (FragmentActivity) context;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
		removeContext();
	}
	
	/**
	 * 该方法会在完全可见之前调用两次，第一次在{@link #onCreate(Bundle)}之前调用isVisibleToUser=false;第二次在{@link #onCreate(Bundle)}之后，{@link #onInflate(Context, AttributeSet, Bundle)}
	 * 之前调用isVisibleToUser=true
	 * @param isVisibleToUser
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint() && hasLazyLoad) {
			onLazyLoad();
			hasLazyLoad = false;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		hasLazyLoad = true;
		//解决嵌套 Fragment 的bug
		removeContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflaterView(inflater, container, savedInstanceState);
		this.mBaseView = view;
		initView(view);
		return view;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mBaseView != null){
			GarbageUtils.unBindDrawables(mBaseView);
			GarbageUtils.unBindListener(mBaseView);
		}
	}

	private void removeContext(){
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置是否懒加载数据，当界面显示的时候才显示加载数据，默认为true开启
	 * @param hasLazyLoad
	 */
	public void setHasLazyLoad(boolean hasLazyLoad) {
		this.hasLazyLoad = hasLazyLoad;
	}

	/**
	 * 懒加载,当界面可见时调用，防止ViewPager重复创建
	 * 注意：该方法在{@link #onInflate(Context, AttributeSet, Bundle)}之前会调用，故而不能做初始化视图等工作，只能加载其他非UI数据；
	 * 初始化视图在{@link #initView(View)}中完成
	 */
	protected void onLazyLoad() {
	}


	/**
	 * Look for a child view with the given id. If this view has the given id, return this view.
	 * <p>Title: findViewById
	 * <p>Description:
	 * @param id  The id to search for.
	 * @return The view that has the given id in the hierarchy or null
	 */
	public View findViewById(int id){
		if (mBaseView != null) {
			return this.mBaseView.findViewById(id);
		}
		return null;
	}

	/**
	 * Return the {@link FragmentActivity} this fragment is currently associated with.
	 * May return {@code null} if the fragment is associated with a {@link Context}
	 * instead.
	 * @return
	 */
	protected FragmentActivity getActivityReference() {
		if (mActivity == null) {
			mActivity = getActivity();
		}
		return mActivity;
	}
	
}
