/**
 * 
 */
package com.comeonbabys.android.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author PvTai Jan 21, 2015 5:03:38 PM
 */
public class ImageUtils {
	private static final String TAG = "ImageUtils";

	public static ImageLoaderConfiguration getImageLoaderConfiguration(Context context) {
		ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.diskCacheSize(50 * 1024 * 1024) // 50 Mb
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.build();
		return imageLoaderConfiguration;
	}
	
	public static Bitmap getBitmapFromView(View view) {
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null) bgDrawable.draw(canvas);
		else canvas.drawColor(Color.WHITE);
		view.draw(canvas);
		return returnedBitmap;
	}

//	//Сохраняет обьект Bitmap в формате JPG в MediaStore
//	//Возвращает Uri сохраненного файла .jpg
//	public static Uri getImageUri(Context inContext, Bitmap inImage) {
////		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
////		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//		String imgName = UUID.randomUUID().toString() + ".jpg";
//		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imgName, null);
//        Log.d(TAG, "New file path = " + path + "/" + imgName);
//		//return Uri.parse(path + "/" + imgName);
//		return Uri.parse(path);
//	}

    //Сохраняет обьект Bitmap в формате JPG в MediaStore
    //Возвращает Uri сохраненного файла .jpg
    public static Uri getImageUri(Activity act, Bitmap inImage) throws IOException {
        String imgName = UUID.randomUUID().toString() + ".jpg";
        File file = new File(act.getFilesDir(), imgName);
        FileOutputStream out = act.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();
        String realPath = file.getAbsolutePath();
        File f = new File(realPath);
        Uri uri = Uri.fromFile(f);
        Log.d(TAG, "New file path = " + uri.toString());
        return uri;
    }

	//Уменьшить размер Bitmap до нужного для предотвращения OutOfMemory
	//https://developer.android.com/topic/performance/graphics/load-bitmap.html
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}
		Log.d(TAG, "Calculated inSampleSize: " + inSampleSize);
		return inSampleSize;
	}

	public static Bitmap getResizedBitmap(Bitmap bmp, int reqWidth, int reqHeight) {
		int height = bmp.getHeight();
		int width = bmp.getWidth();
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, width / inSampleSize, height / inSampleSize, false);
			return resizedBitmap;
		}
		return bmp;
	}

    public static Bitmap decodeBitmapFromFile(Context context, Uri imageUri) {
        Bitmap bm = BitmapFactory.decodeFile(imageUri.getPath());
        Log.d(TAG, "Decoded image size: " + bm.getWidth() + "x" + bm.getHeight());
        return bm;
    }

	public static Bitmap decodeSampledBitmapFromFile(Context context, Uri imageUri, int reqWidth, int reqHeight) {
		String[] projection = { MediaStore.MediaColumns.DATA };
		Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
		cursor.moveToFirst();
		String selectedImagePath = cursor.getString(column_index);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(selectedImagePath, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(selectedImagePath, options);
		Log.d(TAG, "Decoded image size: " + bm.getWidth() + "x" + bm.getHeight());
		return bm;
	}

//	public static Bitmap decodeSampledBitmapFromFile(Context context, Uri imageUri, int reqWidth, int reqHeight) {
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(imageUri.getPath(), options);
//		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//		options.inJustDecodeBounds = false;
//		Bitmap bm = BitmapFactory.decodeFile(imageUri.getPath(), options);
//		Log.d(TAG, "Decoded image size: " + bm.getWidth() + "x" + bm.getHeight());
//		return bm;
//	}




	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
}
