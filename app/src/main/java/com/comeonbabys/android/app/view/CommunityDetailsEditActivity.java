package com.comeonbabys.android.app.view;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.comeonbabys.android.app.common.DialogUtilities;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.common.ILog;
import com.comeonbabys.android.app.common.ServiceConsts;
import com.comeonbabys.android.app.db.dto.CommunityDTO;
import com.comeonbabys.android.app.db.dto.ImageCommunityDTO;
import com.comeonbabys.android.app.model.Comment;
import com.comeonbabys.android.app.requests.commands.Commands;
import com.comeonbabys.android.app.view.customview.ButtonCustom;
import com.comeonbabys.android.app.view.customview.EditTextCustom;
import com.comeonbabys.android.app.view.customview.ImageViewMySuccess;
import com.comeonbabys.android.app.view.customview.TextViewCustom;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CommunityDetailsEditActivity extends BaseActivity implements OnClickListener {

	CommunityDTO communityDto;
	List<ImageCommunityDTO> listImage;
	LinearLayout layoutImage;
	final int TYPE_DELETE_IMAGE = 1;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_community_details_edit);
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootCommunityDetailsEdit));
	}

	private void initObjectUI() {
		communityDto = (CommunityDTO) getIntent().getSerializableExtra(ServiceConsts.EXTRA_COMMUNITY);
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_edit_title_my_success);
		layoutImage = (LinearLayout) findViewById(R.id.layoutImage);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((EditTextCustom) findViewById(R.id.textTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((EditTextCustom) findViewById(R.id.textContent)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.buttonDone)).setOnClickListener(this);

		if (communityDto != null) {
			listImage = communityDto.getListImage();
			((EditTextCustom) findViewById(R.id.textTitle)).setText(communityDto.getTitle());
			((EditTextCustom) findViewById(R.id.textContent)).setText(communityDto.getContent());
			loadDataImage();
			loadDataComment();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.buttonDone:
			checkImageRemoved();
			break;
		default:
			break;
		}
	}

	private void loadDataImage() {
		ILog.e("loadDataImage");
		layoutImage.removeAllViews();
		if (listImage != null && listImage.size() > 0)
			for (int i = 0; i < listImage.size(); i++) {
				ILog.e("loadDataImage " + i);
				ImageViewMySuccess image = new ImageViewMySuccess(this);
                String thumbURL = com.comeonbabys.android.app.requests.Constants.IMAGES_URL + listImage.get(i).getImage();
				image.initImage(listImage.get(i), this, TYPE_DELETE_IMAGE);
				layoutImage.addView(image);
                ImageLoader.getInstance().displayImage(thumbURL, image.getImgThumbnail(), Constants.GROUP_AVATAR_DISPLAY_OPTIONS);
			}
	}

	private List<Comment> loadDataComment() {
		List<Comment> list = new ArrayList<Comment>();
		for (int i = 0; i < 10; i++) {
			Comment item = new Comment("http://placehold.it/120x120&text=image1", "user", Calendar.getInstance().getTime().toString());
			list.add(item);
		}
		return list;
	}

	int count_image_deleted = 0;
	int count_call_delete = 0;
    String imagesToRemove;

	private void checkImageRemoved() {
		count_image_deleted = 0;
		count_call_delete = 0;
		if (listImage != null && listImage.size() > 0) {
			if (layoutImage.getChildCount() == listImage.size()) {
				requestToServer();
				return;
			} else {
				for (int i = 0; i < listImage.size(); i++) {
					if (!checkImageExistOnView(listImage.get(i))) {
						count_image_deleted++;
                        addImageToRemove(listImage.get(i).getImage());
					}
				}
				if (count_image_deleted == 0)
					requestToServer();
			}
		} else {
			requestToServer();
		}
	}

    private void addImageToRemove(String imgName) {
        if(imagesToRemove == null) {
            imagesToRemove = imgName;
        } else {
            imagesToRemove = imagesToRemove + "," + imgName;
        }
    }

	private boolean checkImageExistOnView(ImageCommunityDTO dto) {
		if (layoutImage != null && layoutImage.getChildCount() > 0) {
			for (int i = 0; i < layoutImage.getChildCount(); i++) {
				ImageViewMySuccess image = (ImageViewMySuccess) layoutImage.getChildAt(i);
				if (image.imageCommunityDto.getId() == dto.getId())
					return true;
			}
		}
		return false;
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
		case TYPE_DELETE_IMAGE:
			ImageCommunityDTO imageDto = (ImageCommunityDTO) data;
			for (int i = 0; i < layoutImage.getChildCount(); i++) {
				ImageViewMySuccess image = (ImageViewMySuccess) layoutImage.getChildAt(i);
				if (image.imageCommunityDto.getId() == imageDto.getId()) {
					layoutImage.removeView(image);
					break;
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * @author PvTai This method used for set enable/disable all button
	 */
	private void stateAllButtons(boolean value) {
		((ButtonCustom) findViewById(R.id.buttonDone)).setEnabled(value);
	}

	Handler handler;

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				hideProgress();
				switch (msg.what) {
					case Constants.MSG_EDIT_COMMUNITY_SUCCESS: {
						Log.d("SAVE COMMUNITY SUCCESS", "SAVE COMMUNITY SUCCESS!!!!");
						hideProgress();
						finish();
						break;
					}
					case Constants.MSG_EDIT_COMMUNITY_FAIL: {
						Log.d("SAVE COMMUNITY FAIL!!!!", "SAVE COMMUNITY FAIL!!!!");
						hideProgress();
						Toast.makeText(CommunityDetailsEditActivity.this, "SAVE COMMUNITY FAIL!!", Toast.LENGTH_SHORT).show();
						finish();
						break;
					}
				}
			}
		};
	}


	private void requestToServer() {
		if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.textTitle)).getText().toString())) {
			String err_msg = getString(R.string.error_enter_new_community);
			DialogUtilities.showAlertDialog(CommunityDetailsEditActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}
		if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.textContent))
				.getText().toString())) {
			String err_msg = getString(R.string.error_enter_new_community);
			DialogUtilities.showAlertDialog(CommunityDetailsEditActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}

		Log.e("!!!!!!!EDIT_BUTTON_DONE","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		communityDto.setTitle(((EditTextCustom) findViewById(R.id.textTitle)).getText().toString());
		communityDto.setContent(((EditTextCustom) findViewById(R.id.textContent)).getText().toString());
		showProgress();
		Commands.sendEditComunityItem(handler, communityDto);
		hideProgress();
		finish();
//		PutCommunityCommand.start(baseActivity, communityDto);
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
//		addAction(ServiceConsts.PUT_COMMUNITY_SUCCESS_ACTION,
//				new PutCommunitySuccessAction());
//		addAction(ServiceConsts.PUT_COMMUNITY_FAIL_ACTION,
//				new PutCommunityFailAction());
//		addAction(ServiceConsts.DELETE_IMAGE_COMMUNITY_SUCCESS_ACTION,
//				new DeleteImageCommunitySuccessAction());
//		addAction(ServiceConsts.DELETE_IMAGE_COMMUNITY_FAIL_ACTION,
//				new DeleteImageCommunityFailAction());
//		updateBroadcastActionList();
//	}
//
//	private void removeActions() {
//		removeAction(ServiceConsts.PUT_COMMUNITY_SUCCESS_ACTION);
//		removeAction(ServiceConsts.PUT_COMMUNITY_FAIL_ACTION);
//		removeAction(ServiceConsts.DELETE_IMAGE_COMMUNITY_SUCCESS_ACTION);
//		removeAction(ServiceConsts.DELETE_IMAGE_COMMUNITY_FAIL_ACTION);
//		updateBroadcastActionList();
//	}

//	private class DeleteImageCommunitySuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			count_call_delete++;
//			if (count_call_delete == count_image_deleted) {
//				PutCommunityCommand.start(baseActivity, communityDto);
//			}
//		}
//	}
//
//	private class DeleteImageCommunityFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			if (count_call_delete == count_image_deleted) {
//				stateAllButtons(true);
//				hideProgress();
//				DialogUtilities.showAlertDialog(
//						CommunityDetailsEditActivity.this,
//						R.layout.dialog_error_warning, R.string.string_error,
//						R.string.error_message_for_service_unavailable, null);
//			}
//		}
//	}
//
//	private class PutCommunitySuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			CommunityDTO community = (CommunityDTO) bundle
//					.getSerializable(ServiceConsts.EXTRA_COMMUNITY);
//			Intent intent = new Intent();
//			intent.putExtra(ServiceConsts.EXTRA_COMMUNITY, community);
//			setResult(RESULT_OK, intent);
//			hideProgress();
//			stateAllButtons(true);
//			finish();
//		}
//	}
//
//	private class PutCommunityFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			stateAllButtons(true);
//			hideProgress();
//			DialogUtilities.showAlertDialog(CommunityDetailsEditActivity.this,
//					R.layout.dialog_error_warning, R.string.string_error,
//					R.string.error_message_for_service_unavailable, null);
//		}
//	}
}