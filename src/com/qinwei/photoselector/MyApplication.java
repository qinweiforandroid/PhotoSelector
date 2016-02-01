package com.qinwei.photoselector;

import java.io.File;
import java.util.ArrayList;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

public class MyApplication extends Application {
	private static ArrayList<Activity> tasks = new ArrayList<Activity>();
	private static MyApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initializeImageloader();
	}

	public void addToTasks(Activity activity) {
		tasks.add(activity);
	}

	public void clearTasks() {
		for (Activity a : tasks) {
			a.finish();
		}
		tasks.clear();
	}

	public void initializeImageloader() {
		String cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "shop" + File.separator + "cache";
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.memoryCache(new LruMemoryCache(50 * 1024 * 1024));
		config.diskCacheExtraOptions(480, 800, null);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCache(new UnlimitedDiscCache(new File(cacheDir)));
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app
		ImageLoader.getInstance().init(config.build());
	}
	public static MyApplication getInstance() {
		return mInstance;
	}
}
