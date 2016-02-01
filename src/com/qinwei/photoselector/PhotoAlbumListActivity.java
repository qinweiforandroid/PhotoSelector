package com.qinwei.photoselector;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinwei.photoselector.entity.PhotoAlbum;
import com.qinwei.photoselector.lib.BaseGridViewActivity;
import com.qinwei.photoselector.utils.AlbumHelper;
import com.qinwei.photoselector.utils.Constants;

public class PhotoAlbumListActivity extends BaseGridViewActivity {

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_album_list);
	}

	@Override
	public void initializeView() {
		super.initializeView();
	}

	@Override
	protected void initializeData() {
		MyApplication.getInstance().addToTasks(this);
		List<PhotoAlbum> imagesBucketList = AlbumHelper.getInstance(getApplicationContext()).getImagesBucketList(false);
		modules.addAll(imagesBucketList);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, PhotoListActivity.class);
		intent.putExtra(Constants.KEY_ALBUM_ENTITIES, (PhotoAlbum) modules.get(position));
		intent.putExtra(Constants.KEY_PHOTOS, getIntent().getSerializableExtra(Constants.KEY_PHOTOS));
		startActivity(intent);
	}

	@Override
	public View getAdapterViewAtPosition(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(this).inflate(R.layout.activity_album_list_item, null);
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

		private ImageView mPhotoAlbumItemIconImg;
		private TextView mPhotoAlbumItemNameLabel;
		private TextView mPhotoAlbumItemNumLabel;

		/**
		 * 描述
		 * 
		 * @param convertView
		 */
		public void initializeView(View convertView) {
			mPhotoAlbumItemIconImg = (ImageView) convertView.findViewById(R.id.mPhotoAlbumItemIconImg);
			mPhotoAlbumItemNameLabel = (TextView) convertView.findViewById(R.id.mPhotoAlbumItemNameLabel);
			mPhotoAlbumItemNumLabel = (TextView) convertView.findViewById(R.id.mPhotoAlbumItemNumLabel);
		}

		/**
		 * 描述
		 * 
		 * @param convertView
		 */
		public void initializeData(int position) {
			PhotoAlbum album = (PhotoAlbum) modules.get(position);
			final String uri = "file:///" + album.imageList.get(0).imagePath;
			imageLoader.displayImage(uri, mPhotoAlbumItemIconImg, options);
			mPhotoAlbumItemNameLabel.setText(album.bucketName);
			mPhotoAlbumItemNumLabel.setText(album.count + "");
		}
	}
}
