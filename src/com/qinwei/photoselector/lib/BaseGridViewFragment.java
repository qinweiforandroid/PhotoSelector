package com.qinwei.photoselector.lib;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.qinwei.photoselector.R;

public abstract class BaseGridViewFragment extends BaseFragment implements OnItemClickListener {

	protected GridView gridView;
	protected ListAdapter mAdapter;
	protected ArrayList<Object> modules = new ArrayList<Object>();

	@Override
	public void initializeView(View v) {
		gridView = (GridView) v.findViewById(R.id.gridView);
		if (gridView == null) {
			throw new IllegalArgumentException("you contentView must contains id:generalPullToRefreshLsv");
		}
		mAdapter = new ListAdapter();
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);
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
