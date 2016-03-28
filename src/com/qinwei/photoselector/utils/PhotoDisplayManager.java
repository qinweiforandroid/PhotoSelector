package com.qinwei.photoselector.utils;

import android.widget.ImageView;

/**
 * @author qinwei email:qinwei_it@163.com
 * @created createTime: 2016-2-22 下午1:48:28
 * @version 1.0
 */

public class PhotoDisplayManager {
	private PhotoDisplayManager() {
	}

	private static PhotoDisplayManager mInstance;
	private IImageDisplay display;

	public static PhotoDisplayManager getInstance() {
		if (mInstance == null) {
			synchronized (PhotoDisplayManager.class) {
				mInstance = new PhotoDisplayManager();
			}
		}
		return mInstance;
	}

	public void init(IImageDisplay display) {
		this.display = display;
	}

	public void displayImage(String uri, ImageView imageView) {
		if (display == null) {
			throw new IllegalArgumentException("IImageDisplay can't null");
		}
		display.displayImage(uri, imageView);
	}

	public void displayImage(int id, ImageView imageView) {
		if (display == null) {
			throw new IllegalArgumentException("IImageDisplay can't null");
		}
		display.displayImage(id, imageView);
	}

	public void clearMemoryCache() {
		display.clearMemoryCache();
	}
}
