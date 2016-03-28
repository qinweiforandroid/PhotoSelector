package com.qinwei.photoselector;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.qinwei.photoselector.entity.PhotoAlbum;
import com.qinwei.photoselector.entity.PhotoEntity;
import com.qinwei.photoselector.fragment.PhotoAlbumListFragment;
import com.qinwei.photoselector.fragment.PhotoAlbumListFragment.OnPhotoAlbumItemClickListener;
import com.qinwei.photoselector.fragment.PhotoListFragment;
import com.qinwei.photoselector.fragment.PhotoListFragment.OnPhotoSelectChangedListener;
import com.qinwei.photoselector.lib.BaseActivity;
import com.qinwei.photoselector.utils.Constants;

/**
 * @author qinwei email:qinwei_it@163.com
 * @created createTime: 2016-3-28 下午3:47:38
 * @version 1.0
 */

public class PhotoSelectorActivity extends BaseActivity implements OnPhotoAlbumItemClickListener, OnPhotoSelectChangedListener {
	/**
	 * 选中的图片集合
	 */
	protected ArrayList<PhotoEntity> photos = new ArrayList<PhotoEntity>();
	private PhotoAlbumListFragment mPhotoAlbumListFragment;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_photo_selector);
	}

	@Override
	protected void initializeView() {
		photos = (ArrayList<PhotoEntity>) getIntent().getSerializableExtra(Constants.KEY_PHOTOS);
		mPhotoAlbumListFragment = new PhotoAlbumListFragment();
		FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
		ft2.replace(R.id.mPhotoContainer, mPhotoAlbumListFragment);
		// ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft2.addToBackStack(null);
		ft2.commit();
	}

	@Override
	protected void initializeData() {

	}

	/**
	 * @param album
	 * @see com.qinwei.photoselector.fragment.PhotoAlbumListFragment.OnPhotoAlbumItemClickListener#onPhotoAlbumItemClick(com.qinwei.photoselector.entity.PhotoAlbum)
	 */
	@Override
	public void onPhotoAlbumItemClick(PhotoAlbum album) {
		for (PhotoEntity entity : album.imageList) {
			entity.isSelected = false;
		}
		FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
		ft2.replace(R.id.mPhotoContainer, PhotoListFragment.getInstance(photos, album));
		// ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft2.addToBackStack(null);
		ft2.commit();
	}

	/**
	 * 描述
	 * 
	 * @param selectedCache
	 */
	public void done(ArrayList<PhotoEntity> selectedCache) {
		Intent data = new Intent();
		data.putExtra(Constants.KEY_PHOTOS, selectedCache);
		setResult(RESULT_OK, data);
		finish();
	}

	/**
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			finish();
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * @param isRemove
	 * @param photo
	 * @see com.qinwei.photoselector.fragment.PhotoListFragment.OnPhotoSelectChangedListener#onPhotoSelectChanged(boolean,
	 *      com.qinwei.photoselector.entity.PhotoEntity)
	 */
	@Override
	public void onPhotoSelectChanged(boolean isRemove, PhotoEntity photo) {
		if (isRemove) {
			photos.remove(photo);
		} else {
			photos.add(photo);
		}
	}
}
