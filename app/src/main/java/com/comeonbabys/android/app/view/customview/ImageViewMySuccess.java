/*
 * Copyright (c) 2012 Ecomlogy Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Ecomlogy Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it without
 * permissions granted by Ecomlogy Inc.
 */
package com.comeonbabys.android.app.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.comeonbabys.android.app.common.OnEventControlListener;
import com.comeonbabys.android.app.common.ServerPath;
import com.comeonbabys.android.app.db.dto.ImageCommunityDTO;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageViewMySuccess extends LinearLayout {

	ImageView imgThumbnail;
	ImageView imgDelete;
	public ImageCommunityDTO imageCommunityDto;

	public ImageView getImgThumbnail() {
		return imgThumbnail;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the context contain this control
	 */
	public ImageViewMySuccess(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.item_mysuccess_imageview, this, true);
		init(context, v);
	}

	/**
	 * Init the footer bar
	 * 
	 * @param flag
	 * @param listener
	 */
	public void initImage(final ImageCommunityDTO item, final OnEventControlListener listener, final int type) {
		imageCommunityDto = item;
		if (imgThumbnail != null && item != null)
			ImageLoader.getInstance().displayImage(ServerPath.SERVER_MEDIA + item.getImage(), imgThumbnail, Constants.GROUP_THUMBNAIL_DISPLAY_OPTIONS);
		if (imgDelete != null)
			imgDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onEvent(type, null, item);
				}
			});
	}

	public void initImage(final OnEventControlListener listener, final int type) {
		if (imgDelete != null)
			imgDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onEvent(type, null, getId());
				}
			});
	}

	private void init(Context context, View v) {
		imgThumbnail = (ImageView) v.findViewById(R.id.imgThumbnail);
		imgDelete = (ImageView) v.findViewById(R.id.imgDelete);
	}

	public void setImageBitmap(Bitmap bitmap) {
		if (imgThumbnail != null)
			imgThumbnail.setImageBitmap(bitmap);
	}
}
