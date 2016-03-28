package com.qinwei.photoselector;

import java.io.File;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qinwei.photoselector.utils.IImageDisplay;
import com.qinwei.photoselector.utils.PhotoDisplayManager;

public class MyApplication extends Application {
	private static MyApplication mInstance;
	private DisplayImageOptions options;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initializeImageloader();
		initializePhotoSelectorConfig();
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

	private void initializePhotoSelectorConfig() {
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.bg_transparent).showImageForEmptyUri(R.drawable.bg_transparent)
				.showImageOnFail(R.drawable.bg_transparent).imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
		PhotoDisplayManager.getInstance().init(new ImageloadDisplay());
	}

	private class ImageloadDisplay implements IImageDisplay {

		@Override
		public void displayImage(String uri, ImageView imageView) {
			ImageLoader.getInstance().displayImage(uri, imageView, options);
		}

		@Override
		public void displayImage(int id, ImageView imageView) {
			ImageLoader.getInstance().displayImage("drawable://" + id, imageView, options);
		}

		@Override
		public void clearMemoryCache() {
			ImageLoader.getInstance().clearMemoryCache();
		}
	}

	public static MyApplication getInstance() {
		return mInstance;
	}
}
