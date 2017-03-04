package com.blakequ.activitylifecyclecallbackscompat.ui;

import android.app.Service;

import com.blakequ.activitylifecyclecallbackscompat.App;

/**
 * running background using for keep watch on the user task and activity
 * @author blakequ Blakequ@gmail.com
 * 
 */
public abstract class BaseService extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		App.totalList.add(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		App.totalList.remove(this);
	}
	
	/**
	 * will auto invoke when exit app
	 * <p>Title: exitApp
	 * <p>Description:
	 */
	public abstract void exitApp();
	
	/**
	 * is auto stop service when app exit
	 * <p>Title: isAutoFinish
	 * <p>Description: 
	 * @return
	 */
	public abstract boolean isAutoFinish();
}
