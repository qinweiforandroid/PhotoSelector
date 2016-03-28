package com.qinwei.photoselector.fragment;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinwei.photoselector.R;
import com.qinwei.photoselector.entity.PhotoAlbum;
import com.qinwei.photoselector.lib.BaseGridViewFragment;
import com.qinwei.photoselector.utils.AlbumHelper;
import com.qinwei.photoselector.utils.PhotoDisplayManager;

public class PhotoAlbumListFragment extends BaseGridViewFragment {
	public interface OnPhotoAlbumItemClickListener {
		void onPhotoAlbumItemClick(PhotoAlbum album);
	}

	private OnPhotoAlbumItemClickListener listener;

	@Override
	protected int getFragmentLayoutId() {
		return R.layout.fragment_album_list;
	}

	@Override
	public void initializeView(View v) {
		super.initializeView(v);
		List<PhotoAlbum> imagesBucketList = AlbumHelper.getInstance(getActivity()).getImagesBucketList(false);
		modules.addAll(imagesBucketList);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * @param activity
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnPhotoAlbumItemClickListener) {
			listener = (OnPhotoAlbumItemClickListener) activity;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (listener != null) {
			listener.onPhotoAlbumItemClick((PhotoAlbum) modules.get(position));
		}
		// Intent intent = new Intent(this, PhotoListFragment.class);
		// intent.putExtra(Constants.KEY_ALBUM_ENTITIES, );
		// intent.putExtra(Constants.KEY_PHOTOS,
		// getIntent().getSerializableExtra(Constants.KEY_PHOTOS));
		// startActivity(intent);
	}

	@Override
	public View getAdapterViewAtPosition(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_album_list_item, null);
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
			PhotoDisplayManager.getInstance().displayImage(uri, mPhotoAlbumItemIconImg);
			mPhotoAlbumItemNameLabel.setText(album.bucketName);
			mPhotoAlbumItemNumLabel.setText(album.count + "");
		}
	}

}
