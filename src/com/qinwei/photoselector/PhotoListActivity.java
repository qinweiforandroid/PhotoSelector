package com.qinwei.photoselector;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.qinwei.photoselector.entity.PhotoAlbum;
import com.qinwei.photoselector.entity.PhotoEntity;
import com.qinwei.photoselector.lib.BaseGridViewActivity;
import com.qinwei.photoselector.utils.Constants;

/**
 * @author qinwei email:qinwei_it@163.com
 * @created createTime: 2015-9-11 下午10:35:44
 * @version 1.0
 */

public class PhotoListActivity extends BaseGridViewActivity implements OnClickListener {
	private Button mPhotoDoChoiceBtn;
	private ArrayList<PhotoEntity> selectedCache = new ArrayList<PhotoEntity>();

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_photo_list);
	}

	@Override
	public void initializeView() {
		super.initializeView();
		mPhotoDoChoiceBtn = (Button) findViewById(R.id.mPhotoDoChoiceBtn);
		mPhotoDoChoiceBtn.setOnClickListener(this);
	}

	@Override
	protected void initializeData() {
		MyApplication.getInstance().addToTasks(this);
		PhotoAlbum album = (PhotoAlbum) getIntent().getSerializableExtra(Constants.KEY_ALBUM_ENTITIES);
		// 获取选中图片进行回显
		Serializable serializable = getIntent().getSerializableExtra(Constants.KEY_PHOTOS);
		if (serializable != null) {
			selectedCache.addAll((ArrayList<PhotoEntity>) serializable);
			for (PhotoEntity photo : selectedCache) {
				if (album.imageList.contains(photo)) {
					int i = album.imageList.indexOf(photo);
					album.imageList.get(i).isSelected = true;
				}
			}
		}
		modules.addAll(album.imageList);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.putExtra(Constants.KEY_PHOTOS, selectedCache);
		//设置广播行为
		intent.setAction(BasePublishPhotoActivity.PhotoSelectReceiver.ACTION_SELECT_PHOTO);
		sendBroadcast(intent);
		MyApplication.getInstance().clearTasks();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		PhotoEntity photo = (PhotoEntity) modules.get(position);
		if (photo.isSelected) {
			photo.isSelected = false;
			selectedCache.remove(photo);
		} else {
			photo.isSelected = true;
			selectedCache.add(photo);
		}
		mPhotoDoChoiceBtn.setText("确定(" + selectedCache.size() + ")");
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public View getAdapterViewAtPosition(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(this).inflate(R.layout.activity_photo_list_item, null);
			holder = new Holder();
			holder.initializeView(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.initializeData(position);
		return convertView;
	}

	class Holder {
		private ImageView mPhotoItemIconImg;
		private ImageView mPhotoItemCheckedStateImg;

		public void initializeView(View convertView) {
			mPhotoItemIconImg = (ImageView) convertView.findViewById(R.id.mPhotoItemIconImg);
			mPhotoItemCheckedStateImg = (ImageView) convertView.findViewById(R.id.mPhotoItemCheckedStateImg);
		}

		public void initializeData(int position) {
			PhotoEntity photo = (PhotoEntity) modules.get(position);
			String tempUri = "";
			if (photo.thumbnailPath != null && new File(photo.thumbnailPath).exists()) {
				tempUri = "file:///" + photo.thumbnailPath;
			} else {
				tempUri = "file:///" + photo.imagePath;
			}
			imageLoader.displayImage(tempUri, mPhotoItemIconImg, options);
			if (photo.isSelected) {
				mPhotoItemCheckedStateImg.setVisibility(View.VISIBLE);
			} else {
				mPhotoItemCheckedStateImg.setVisibility(View.GONE);
			}
		}
	}
}
