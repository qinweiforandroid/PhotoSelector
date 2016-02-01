package com.qinwei.photoselector;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.qinwei.photoselector.entity.PhotoEntity;
import com.qinwei.photoselector.lib.ActionSheet;
import com.qinwei.photoselector.lib.ActionSheet.ActionSheetListener;
import com.qinwei.photoselector.lib.BaseGridViewActivity;
import com.qinwei.photoselector.utils.Constants;

/**
 * 图片选择器入口基类
 * 
 * @author qinwei email:qinwei_it@163.com
 * @created createTime: 2015-9-11 下午11:27:53
 * @version 1.0
 */
public abstract class BasePublishPhotoActivity extends BaseGridViewActivity {

	/**
	 * 选中的图片集合
	 */
	protected ArrayList<PhotoEntity> photos = new ArrayList<PhotoEntity>();
	/**
	 * 压缩后图片路径
	 */
	protected ArrayList<String> uploads = new ArrayList<String>();
	private Uri imageUri;
	private PhotoSelectReceiver receiver;

	public static final int REQUEST_OPEN_CAMERA = 10;

	public class PhotoSelectReceiver extends BroadcastReceiver {

		public static final String ACTION_SELECT_PHOTO = "android.intent.action.SELECT_PHOTO";

		/**
		 * @param context
		 * @param intent
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
		 *      android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && intent.getAction().equals(ACTION_SELECT_PHOTO)) {
				ArrayList<PhotoEntity> photos = (ArrayList<PhotoEntity>) intent.getSerializableExtra(Constants.KEY_PHOTOS);
				if (photos == null) {
					photos = new ArrayList<PhotoEntity>();
				}
				notifyDataChanged(photos);
			}
		}

	}

	@Override
	public void initializeView() {
		super.initializeView();
	}

	@Override
	protected void initializeData() {
		receiver = new PhotoSelectReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(PhotoSelectReceiver.ACTION_SELECT_PHOTO);
		registerReceiver(receiver, filter);
	}

	/**
	 * 更新当前UI
	 * 
	 * @param photos
	 */
	public void notifyDataChanged(ArrayList<PhotoEntity> photos) {
		this.photos = photos;
		modules.clear();
		uploads.clear();
		modules.add("");
		modules.addAll(0, photos);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 打开图片选择器
	 */
	public void openPictureSelector() {
		Intent intent = new Intent(this, PhotoAlbumListActivity.class);
		intent.putExtra(Constants.KEY_PHOTOS, photos);
		startActivity(intent);
	}

	/**
	 * 打开系统相机拍照
	 */
	public void openSystemCamera() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOpenCameraTempUri());
		startActivityForResult(openCameraIntent, REQUEST_OPEN_CAMERA);
	}

	/**
	 * 
	 * @return 拍照路径
	 */
	public Uri getOpenCameraTempUri() {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg");
		imageUri = Uri.fromFile(file);
		return imageUri;
	}

	/**
	 * 添加照片菜单
	 */
	public void openMenu() {
		new ActionSheet().createBuilder(this, getFragmentManager()).setCancelButtonTitle("取消").setOtherButtonTitles("从手机选择", "拍照")
				.setListener(new ActionSheetListener() {
					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						switch (index) {
						case 0:
							openPictureSelector();
							break;
						case 1:
							openSystemCamera();
							break;
						default:
							break;
						}
					}

					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
					}
				}).show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (modules.size() - 1 == position) {
			openMenu();
		} else {
			PhotoEntity photo = (PhotoEntity) modules.get(position);
			Toast.makeText(this, "open:" + photo.imagePath, 1000).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_OPEN_CAMERA:
				Log.d("tom", "拍照照片已保存:" + imageUri.getPath());
				PhotoEntity photo = new PhotoEntity();
				photo.imageId = UUID.randomUUID().toString();
				photo.imagePath = imageUri.getPath();
				photo.isSelected = true;
				photos.add(photo);
				notifyDataChanged(photos);
				break;
			default:
				break;
			}
		} else {
			switch (requestCode) {
			case REQUEST_OPEN_CAMERA:
				File file = new File(imageUri.getPath());
				if (file.exists()) {
					file.delete();
				}
				Log.d("tom", "resultCode:" + resultCode + ",删除缓存文件:" + file.getAbsolutePath());
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	};

	@Override
	public View getAdapterViewAtPosition(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(this).inflate(R.layout.activity_publish_list_item, null);
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

		private ImageView mPublishPhotoItemIconImg;
		private String tempUri;

		/**
		 * 描述
		 * 
		 * @param convertView
		 */
		public void initializeView(View convertView) {
			mPublishPhotoItemIconImg = (ImageView) convertView.findViewById(R.id.mPublishPhotoItemIconImg);
		}

		/**
		 * 描述
		 * 
		 * @param convertView
		 */
		public void initializeData(int position) {
			if (position == modules.size() - 1) {
				imageLoader.displayImage("drawable://" + R.drawable.icon_addpic_unfocused, mPublishPhotoItemIconImg, options);
			} else {
				PhotoEntity photo = (PhotoEntity) modules.get(position);
				if (photo.thumbnailPath != null && new File(photo.thumbnailPath).exists()) {
					tempUri = "file:///" + photo.thumbnailPath;
				} else {
					tempUri = "file:///" + photo.imagePath;
				}
				imageLoader.displayImage(tempUri, mPublishPhotoItemIconImg, options);
			}
		}
	}

	/**
	 * @param saveInstance
	 * @see com.qinwei.photoselector.lib.BaseActivity#recoveryState(android.os.Bundle)
	 */
	@Override
	protected void recoveryState(Bundle saveInstance) {
		photos = (ArrayList<PhotoEntity>) saveInstance.getSerializable(Constants.KEY_SELECT_PHOTOS);
		Log.e("wei", saveInstance + "," + photos.size());
		modules.add("");
		modules.addAll(0, photos);
		mAdapter.notifyDataSetChanged();
		super.recoveryState(saveInstance);
	}

	/**
	 * @param outState
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Constants.KEY_SELECT_PHOTOS, photos);
		super.onSaveInstanceState(outState);
	}

	/**
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
