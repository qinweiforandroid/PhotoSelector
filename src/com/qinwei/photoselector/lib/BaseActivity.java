package com.qinwei.photoselector.lib;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public abstract class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		setContentView();
		initializeView();
		if (saveInstance != null) {
			recoveryState(saveInstance);
		} else {
			initializeData();
		}
	}

	/**
	 * 1. 设置布局
	 */
	protected abstract void setContentView();

	/**
	 * 2. 初始化布局
	 */
	protected abstract void initializeView();

	/**
	 * 界面被系统强杀 数据状态恢复
	 * 
	 * @param saveInstance
	 *            状态数据
	 */
	protected void recoveryState(Bundle saveInstance) {

	}

	/**
	 * 3. 初始化ui数据
	 */
	protected abstract void initializeData();

}
