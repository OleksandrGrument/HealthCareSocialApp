/**
 * 
 */
package com.comeonbabys.android.app.common;

import android.graphics.Bitmap;

import com.comeonbabys.android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class Constants {
	public static final String STR_BLANK = "";
	public static final String INTENT_URL = "url";
	public static final String INTENT_STRING_TITLE = "string_title";
	public static final String INTENT_HTML_CONTENT = "html_content";
	public static final String INTENT_ITEM_MY_SUCCESS = "item_mysuccess";
	public static final String INTENT_ITEM_MY_RECIPE = "item_recipe";
	public static final String INTENT_ITEM_MY_HUSBAND = "item_husband";
	public static final String INTENT_ITEM_MY_QA = "item_qa";
	public static final String INTENT_ITEM_REPORT = "item_report";
	public static final String EX_URL_FROM_FILE = "url_from_file";
	public static final String EX_IS_MONTH = "is_month";
	public static final int MSG_EDIT_COMMUNITY_SUCCESS = 43;
	public static final int MSG_EDIT_COMMUNITY_FAIL = 44;




	public static final DisplayImageOptions GROUP_AVATAR_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.showImageOnLoading(R.drawable.icon_photo)
			.showImageForEmptyUri(R.drawable.icon_photo)
			.showImageOnFail(R.drawable.icon_photo).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true).build();

	public static final DisplayImageOptions GROUP_AVATAR_PROFILE_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.showImageOnLoading(R.drawable.avatar_profile)
			.showImageForEmptyUri(R.drawable.avatar_profile)
			.showImageOnFail(R.drawable.avatar_profile).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true).build();

	public static final DisplayImageOptions GROUP_AVATAR_COMMENT_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.showImageOnLoading(R.drawable.avatar_comment)
			.showImageForEmptyUri(R.drawable.avatar_comment)
			.showImageOnFail(R.drawable.avatar_comment).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true).build();

	public static final DisplayImageOptions GROUP_THUMBNAIL_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.showImageOnLoading(R.drawable.bg_white)
			.showImageForEmptyUri(R.drawable.bg_white)
			.showImageOnFail(R.drawable.bg_white).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true).build();

	public static final DisplayImageOptions PROFILE_AVATAR_FEMALE_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.showImageOnLoading(R.drawable.icon_female_large)
			.showImageForEmptyUri(R.drawable.icon_female_large)
			.showImageOnFail(R.drawable.icon_female_large).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true).build();

	public static final DisplayImageOptions PROFILE_AVATAR_MALE_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.showImageOnLoading(R.drawable.icon_male_large)
			.showImageForEmptyUri(R.drawable.icon_male_large)
			.showImageOnFail(R.drawable.icon_male_large).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true).build();
}