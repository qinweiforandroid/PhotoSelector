package com.qinwei.photoselector.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * @author qinwei email:qinwei_it@163.com
 * @created createTime: 2015-9-12 上午1:38:00
 * @version 1.0
 */

public class ImageCacheManager {

	private static ImageCacheManager mInstance;

	public static ImageCacheManager getInstance() {
		if (mInstance == null) {
			mInstance = new ImageCacheManager();
		}
		return mInstance;
	}

	private Map<String, SoftReference<Bitmap>> bitmapCaches = new HashMap<String, SoftReference<Bitmap>>();

	public void put(String key, Bitmap bitmap) {
		bitmapCaches.put(key, new SoftReference<Bitmap>(bitmap));
	}

	public Bitmap getBitmap(String key) {
		SoftReference<Bitmap> softReference = bitmapCaches.get(key);
		if (softReference != null) {
			return softReference.get();
		}
		return null;
	}

	public void clearMemory() {
		bitmapCaches.clear();
		System.gc();
	}
}
