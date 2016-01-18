package com.qinwei.photoselector.lib;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qinwei.photoselector.R;

public abstract class BaseGridViewActivity extends BaseActivity implements OnItemClickListener {

	protected GridView gridView;
	protected ListAdapter mAdapter;
	protected ArrayList<Object> modules = new ArrayList<Object>();
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;

	@Override
	public void initializeView() {
		initializeImageLoaderDefaultConfig();
		gridView = (GridView) findViewById(R.id.gridView);
		if (gridView == null) {
			throw new IllegalArgumentException("you contentView must contains id:generalPullToRefreshLsv");
		}
		mAdapter = new ListAdapter();
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);
	}

	private void initializeImageLoaderDefaultConfig() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.bg_transparent).showImageForEmptyUri(R.drawable.bg_transparent)
				.showImageOnFail(R.drawable.bg_transparent).imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true).bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
	}

	public boolean getPullToRefreshOverScrollEnabled() {
		return false;
	}

	public class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return modules.size();

		}

		@Override
		public Object getItem(int position) {
			return modules.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getAdapterViewAtPosition(position, convertView, parent);
		}

		@Override
		public int getViewTypeCount() {
			return getAdapterViewTypeCount();
		}

		@Override
		public int getItemViewType(int position) {
			return getAdapterViewType(position);
		}

	}

	public int getAdapterViewTypeCount() {
		return 1;
	}

	public abstract View getAdapterViewAtPosition(int position, View convertView, ViewGroup parent);

	public int getAdapterViewType(int position) {
		return 0;
	}

	/**
	 * ItemView的点击事件
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	public void trace(String msg) {
		Log.d("tom", msg);
	}
}
