package com.comeonbaby.android.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

//import com.carmunity.core.dto.CommunityDTO;
//import com.carmunity.core.system.commands.Command;
//import com.carmunity.core.system.commands.PostCommunityCommand;
//import com.carmunity.core.system.commands.ServiceConsts;
//import com.carmunity.core.system.helpers.CommunityHelper;
//import com.carmunity.core.system.utils.ConstsCore;
//import com.carmunity.core.system.utils.IntentConstants;
import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.common.IntentConstants;
import com.comeonbaby.android.app.db.dto.CommunityDTO;
import com.comeonbaby.android.app.requests.Constants;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.server.ServerEmulator;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.utils.ConstsCore;
import com.comeonbaby.android.app.utils.ImageUtils;
import com.comeonbaby.android.app.utils.PermHelper;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.EditTextCustom;
import com.comeonbaby.android.app.view.customview.ImageViewMySuccess;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CommunityDetailsNewActivity extends BaseActivity implements OnClickListener {

    private String TAG = "CommDetailsActivity";
	LinearLayout layoutImage;
	final int TYPE_DELETE_IMAGE = 1;
	final int REQUEST_CAMERA = 1;
	final int SELECT_FILE = 2;

    //Тип контента, передается через интент при нажатии кнопки добавления
    //Значения: ConstsCore.SUCCESS_TYPE, ConstsCore.RECIPE_TYPE, ConstsCore.HUSBAND_TYPE
	int content_type;

    //Хранит ключи - ImageViewMySuccess (добавленые вьюшки с картинками пользователя)
    //А также значения - обьекты Bitmap этих картинок
	HashMap<ImageViewMySuccess, Bitmap> hashBitmap;

    //Переменная для генерирования случайного Id для созданых обьектов ImageViewMySuccess
	Random randomId = new Random(1);

    Handler handler;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_community_details_new);
		hashBitmap = new HashMap<>();
		initObjectUI();
        initHandler();
		setupHideKeyboards(findViewById(R.id.layoutRootCommunityDetailsEdit));
	}

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                hideProgress();
                switch (msg.what) {
                    case Constants.MSG_SAVE_COMUNITY_SUCCESS: {
                        Log.d(TAG, "SAVE COMMUNITY SUCCESS!!!!");
                        finish();
                        break;
                    }
                    case Constants.MSG_SAVE_COMUNITY_FAIL: {
                        Log.d(TAG, "SAVE COMMUNITY FAIL!!!!");
                        break;
                    }
                }
            }
        };
    }

	public void setupHideKeyboards(View view) {
		final View basicView = view;
		// set up to hide when touch on non edittext
		if (view != null && (!(view instanceof EditText) || !view.isFocusable()) && !(view instanceof ImageView)) {
			view.setOnTouchListener(new View.OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					hideSoftKeyboard(CommunityDetailsNewActivity.this, basicView);
					return false;
				}
			});
		}

		// if the view is parent, then we will iterate through it
		if (view != null && view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View innerView = ((ViewGroup) view).getChildAt(i);
				setupHideKeyboards(innerView);
			}
		}
	}

	private void initObjectUI() {
        //Определяем с какой вкладки запустили активити (ConstsCore.SUCCESS_TYPE, ConstsCore.RECIPE_TYPE, ConstsCore.HUSBAND_TYPE)
		content_type = getIntent().getIntExtra(IntentConstants.INTENT_content_type, ConstsCore.SUCCESS_TYPE);

		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_community_new);
		layoutImage = (LinearLayout) findViewById(R.id.layoutImage);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((EditTextCustom) findViewById(R.id.textTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((EditTextCustom) findViewById(R.id.textContent)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.buttonDone)).setOnClickListener(this);
		((ImageView) findViewById(R.id.imgAddPhoto)).setOnClickListener(this);
		((ImageView) findViewById(R.id.imageAdd)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.buttonDone:
			requestToServer();
			break;
		case R.id.imgAddPhoto:
		case R.id.imageAdd:
            //Добавление картинки с камеры
//			OnClickListener yesButtonListener = new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// use standard intent to capture an image
//					Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    Uri newImageUri = createNewImageUri("jpg");
//					captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, newImageUri);
//					// we will handle the returned data in onActivityResult
//					startActivityForResult(captureIntent, REQUEST_CAMERA);
//				}
//			};
//            //Добавление картинки с галереи
//			OnClickListener noButtonListener = new OnClickListener() {
//				@Override
//				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
//				}
//			};
//            //Запрос разрешений
//            if(!PermHelper.checkPermissionGranted(this, PermHelper.REQUEST_CAMERA) || !PermHelper.checkPermissionGranted(this, PermHelper.REQUEST_STORAGE)) {
//                PermHelper.verifyCameraPermissions(this);
//                PermHelper.verifyStoragePermissions(this);
//                break;
//            }
//            //Создание диалога выбора способа получения картинки
//            DialogUtilities.showAddPhotoDialog(CommunityDetailsNewActivity.this, yesButtonListener, noButtonListener);
            break;
		default:
			break;
		}
	}

    //Абсолютный путь текущей сфотографированой картинки
    //Имя и путь генерируется методом createNewImageUri() при нажатии кнопки получения картинки с камеры
    private String imgPath;
	private Uri imgUri;

    //Создает новый файл .png в папке /DCIM/ или папке приложения
    //Присваивает путь getAbsolutePath() в строковую переменную imgPath
    //Возвращает созданный Uri для этого файла
    public Uri createNewImageUri(String format) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis() + "." + format);
            imgUri = Uri.fromFile(file);
            imgPath = file.getAbsolutePath();
        } else {
            File file = new File(getFilesDir(), getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis() + "." + format);
            imgUri = Uri.fromFile(file);
            this.imgPath = file.getAbsolutePath();
        }
        return imgUri;
    }

    //Обработка результатов диалога добавления картинки
    //В результате создается новый обьект ImageViewMySuccess и добавляется в layoutImage
    //а также добавляется в HashMap hashBitmap
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				setCapturedImage(imgPath);


				// Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				// thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
				//
				// ImageViewMySuccess imageView = new ImageViewMySuccess(this);
				// imageView.setId(randomId.nextInt());
				// imageView.initImage(this, TYPE_DELETE_IMAGE);
				// imageView.setImageBitmap(thumbnail);
				// layoutImage.addView(imageView);
				// hashBitmap.put(imageView, thumbnail);
			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				setStorageImage(selectedImageUri);
			}
		}
	}

    //Обработка полученой с камеры картинки (поворот)
    //В onPostExecute(String imagePath) вызов метода addImageBitmap() для добавления картинки в layoutImage внизу и в HashMap
	private void setCapturedImage(final String imagePath) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {
				showProgress();
			}

			@Override
			protected String doInBackground(Void... params) {
				try {
					String imgPath = getRightAngleImage(imagePath);
					return imgPath;
				} catch (Throwable e) {e.printStackTrace();}
				return imagePath;
			}

			@Override
			protected void onPostExecute(String imagePath) {
				super.onPostExecute(imagePath);
				addImageBitmap(decodeFile(imagePath));
				hideProgress();
			}
		}.execute();
	}

    //Обработка полученой с галереи картинки
    //вызов метода addImageBitmap() для добавления картинки в layoutImage внизу и в HashMap
    private void setStorageImage(Uri imageUri) {
        Bitmap bm = ImageUtils.decodeSampledBitmapFromFile(getApplicationContext(), imageUri, 400, 400);
        ImageViewMySuccess imageView = new ImageViewMySuccess(this);
        imageView.setId(randomId.nextInt());
        imageView.initImage(this, TYPE_DELETE_IMAGE);
        imageView.setImageBitmap(bm);
        layoutImage.addView(imageView);
        hashBitmap.put(imageView, bm);
    }

    //Повернуть картинку по пути photoPath в зависимости от данных поворота, записанных в Exif-инфо картинки
    //Возвращает переданную строку с указанным путем картинки: String photoPath
	private String getRightAngleImage(String photoPath) {
		try {
			ExifInterface ei = new ExifInterface(photoPath);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int degree = 0;
			switch (orientation) {
			case ExifInterface.ORIENTATION_NORMAL:
				degree = 0;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			case ExifInterface.ORIENTATION_UNDEFINED:
				degree = 0;
				break;
			default:
				degree = 90;
			}
			return rotateImage(degree, photoPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return photoPath;
	}

    //Повернуть картинку по пути imagePath на указанное degree градусов и перезаписать
    //Возвращает переданную строку с указанным путем картинки: String imagePath
	private String rotateImage(int degree, String imagePath) {
		if (degree <= 0) {
			return imagePath;
		}
		try {
			Bitmap b = BitmapFactory.decodeFile(imagePath);
			Matrix matrix = new Matrix();
			if (b.getWidth() > b.getHeight()) {
				matrix.setRotate(degree);
				b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			}

			FileOutputStream fOut = new FileOutputStream(imagePath);
			String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
			String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

			FileOutputStream out = new FileOutputStream(imagePath);
			if (imageType.equalsIgnoreCase("png")) {
				b.compress(Bitmap.CompressFormat.PNG, 100, out);
			} else if (imageType.equalsIgnoreCase("jpeg") || imageType.equalsIgnoreCase("jpg")) {
				b.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
			fOut.flush();
			fOut.close();
			b.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imagePath;
	}

	//Возвращает обьект Bitmap картинки по её пути String path
	public Bitmap decodeFile(String path) {
		try {
//			// Decode deal_image size
//			//Пока непонятное назначение: декодировать файл в битмап без данных самой картинки, только размеры.
//			//Вероятно, для временного изменения размеров
//			BitmapFactory.Options o = new BitmapFactory.Options();
//			o.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(path, o);
//			// // The new size we want to scale to
//			// final int REQUIRED_SIZE = 1024;
//			//
//			// // Find the correct scale value. It should be the power of 2.
//			// int scale = 1;
//			// while (o.outWidth / scale / 2 >= REQUIRED_SIZE
//			// && o.outHeight / scale / 2 >= REQUIRED_SIZE)
//			// scale *= 2;
//			// Decode with inSampleSize


            //Тест, ранее были строки внизу
            Bitmap bm = ImageUtils.decodeSampledBitmapFromFile(getApplicationContext(), Uri.parse(path), 400, 400);
            return bm;

			//Уменьшить картинку по X и Y в inSampleSize раз (здесь пока не уменьшаем)
//			BitmapFactory.Options o2 = new BitmapFactory.Options();
//			o2.inSampleSize = 1;
//			return BitmapFactory.decodeFile(path, o2);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

    //Сохраняет обьект Bitmap в формате JPG в MediaStore
    //Возвращает Uri сохраненного файла .jpg
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		//String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        String imgName = UUID.randomUUID().toString() + ".jpg";
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, imgName, null);
        Log.d(TAG, "First path = " + path + "/" + imgName);
		return Uri.parse(path + "/" + imgName);
	}

    //Принимает на вход обьект Bitmap
    //Создает новый обьект ImageViewMySuccess на основе полученого Bitmap, присваивает рандомный Id и устанавливает слушатель кнопки удаления картинки
    //Добавляет созданный обьект ImageViewMySuccess к layoutImage внизу
    //Дабвляет Bitmap в HashMap hashBitmap с ключем ImageViewMySuccess imageView
	private void addImageBitmap(Bitmap thePic) {
		if (thePic != null) {
			ImageViewMySuccess imageView = new ImageViewMySuccess(this);
			imageView.setId(randomId.nextInt());
			imageView.initImage(this, TYPE_DELETE_IMAGE);
			imageView.setImageBitmap(thePic);
			layoutImage.addView(imageView);
			hashBitmap.put(imageView, thePic);
		}
	}

	/**
	 * @author PvTai This method used for set enable/disable all button
	 */
	private void stateAllButtons(boolean value) {
		((ButtonCustom) findViewById(R.id.buttonDone)).setEnabled(value);
		((ImageView) findViewById(R.id.imgAddPhoto)).setEnabled(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.weaved.connect.common.listener.OnEventControlListener#onEvent(int,
	 * android.view.View, java.lang.Object)
	 */
	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
        //При нажатии на крестик удаления картинки
		case TYPE_DELETE_IMAGE:
			int id = (int) data;
			refreshListImage(id);
			break;
		default:
			break;
		}
	}

    //Удаляет картинку с id из layoutImage и hashBitmap
    //После этого обновляет layoutImage
	private void refreshListImage(int id) {
		for (int i = 0; i < layoutImage.getChildCount(); i++) {
			try {
				ImageViewMySuccess image = (ImageViewMySuccess) layoutImage.getChildAt(i);
				if (image.getId() == id) {
					layoutImage.removeView(image);
					hashBitmap.remove(image);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		layoutImage.invalidate();
		layoutImage.refreshDrawableState();
	}

    //Вызывается при нажатии кнопки ОК
	private void requestToServer() {
        //Проверяем заполнение всех полей
		if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.textTitle)).getText().toString())) {
			String err_msg = getString(R.string.error_enter_new_community);
			DialogUtilities.showAlertDialog(CommunityDetailsNewActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}
		if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.textContent)).getText().toString())) {
			String err_msg = getString(R.string.error_enter_new_community);
			DialogUtilities.showAlertDialog(CommunityDetailsNewActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}
        //Создаем новый обьект CommunityDTO
		CommunityDTO dto = new CommunityDTO();
		dto.setContent_type(content_type);
		dto.setTitle(((EditTextCustom) findViewById(R.id.textTitle)).getText().toString());
		dto.setContent(((EditTextCustom) findViewById(R.id.textContent)).getText().toString());
		dto.setUser(AppSession.getSession().getSystemUser());
        showProgress();
        //-----Мои добавления
        //List<Bitmap> imagesList = new ArrayList<>();
        List<Bitmap> bmpList = new ArrayList<>();
        for(Bitmap bipmap : hashBitmap.values()) {
            bmpList.add(bipmap);
        }
        Commands.saveComunityRecord(handler, dto, bmpList);

        //---------------ServerEmulator.addNewCommunityRecord(dto, imagesList);

//        hideProgress();
//		finish();


//		showProgress();
//		PostCommunityCommand.start(baseActivity, dto);
	}

	@Override
	public void onResume() {
		super.onResume();
//		addActions();
	}

	@Override
	public void onPause() {
//		removeActions();
		super.onPause();
	}

//	private void addActions() {
//		addAction(ServiceConsts.POST_COMMUNITY_SUCCESS_ACTION,
//				new PostCommunitySuccessAction());
//		addAction(ServiceConsts.POST_COMMUNITY_FAIL_ACTION,
//				new PostCommunityFailAction());
//		addAction(ServiceConsts.POST_IMAGE_COMMUNITY_SUCCESS_ACTION,
//				new PostCommunityImageSuccessAction());
//		addAction(ServiceConsts.POST_IMAGE_COMMUNITY_FAIL_ACTION,
//				new PostCommunityImageFailAction());
//		updateBroadcastActionList();
//	}
//
//	private void removeActions() {
//		removeAction(ServiceConsts.POST_COMMUNITY_SUCCESS_ACTION);
//		removeAction(ServiceConsts.POST_COMMUNITY_FAIL_ACTION);
//		removeAction(ServiceConsts.POST_IMAGE_COMMUNITY_SUCCESS_ACTION);
//		removeAction(ServiceConsts.POST_IMAGE_COMMUNITY_FAIL_ACTION);
//		updateBroadcastActionList();
//	}

	int count_post_image = 0;

//	private class PostCommunitySuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			if (layoutImage != null && layoutImage.getChildCount() > 0) {
//				CommunityDTO community = (CommunityDTO) bundle
//						.getSerializable(ServiceConsts.EXTRA_COMMUNITY);
//				count_post_image = 0;
//				new loadURLMethod().execute(community.getId() + "");
//			} else {
//				hideProgress();
//				stateAllButtons(true);
//				finish();
//			}
//		}
//	}
//
//	private class PostCommunityFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			stateAllButtons(true);
//			hideProgress();
//			DialogUtilities.showAlertDialog(CommunityDetailsNewActivity.this,
//					R.layout.dialog_error_warning, R.string.string_error,
//					R.string.error_message_for_service_unavailable, null);
//		}
//	}
//
//	private class PostCommunityImageSuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			count_post_image++;
//			if (count_post_image == layoutImage.getChildCount()) {
//				hashBitmap.clear();
//				stateAllButtons(true);
//				hideProgress();
//				finish();
//			}
//		}
//	}
//
//	private class PostCommunityImageFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			count_post_image++;
//			if (count_post_image == layoutImage.getChildCount()) {
//				stateAllButtons(true);
//				hideProgress();
//				finish();
//			}
//		}
//	}

	private void post(Context context, String avatar, String community, byte[] binarydata) {
//		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//		builder.addTextBody("community", community, ContentType.TEXT_PLAIN);
//
//		ContentType contentType = ContentType.create("image/image");
//		builder.addBinaryBody("image", binarydata, contentType, avatar);
//		CommunityHelper.postImageCommunity(builder.build());
	}

	class loadURLMethod extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			for (int i = 0; i < layoutImage.getChildCount(); i++) {
				ImageViewMySuccess image = (ImageViewMySuccess) layoutImage.getChildAt(i);
				Bitmap bitmap = hashBitmap.get(image);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] bitmapdata = stream.toByteArray();
				post(baseActivity, "image.png", "/community/" + params[0] + "/", bitmapdata);
			}
			return true;
		}

		@Override
		public void onPostExecute(Boolean kq) {
			hashBitmap.clear();
			stateAllButtons(true);
			hideProgress();
			finish();
			onCancelled();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}
}