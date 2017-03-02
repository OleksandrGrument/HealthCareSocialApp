package com.comeonbaby.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Constants;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.common.ServiceConsts;
import com.comeonbaby.android.app.db.dto.CommentDTO;
import com.comeonbaby.android.app.db.dto.CommunityDTO;
import com.comeonbaby.android.app.requests.ExtraConstants;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.CheckBoxCustom;
import com.comeonbaby.android.app.view.customview.EditTextCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.List;

public class CommunityDetailsActivity extends BaseActivity implements OnClickListener {
	public static CommunityDetailsActivity activity;

    private static final String TAG = "CommDetailsActivity";
	CheckBoxCustom toggleLike;
	CommunityDTO communityDto;
	ListView listComment;
	EditTextCustom editCompose;
	List<CommentDTO> listCommentDto;
	final int RESQUEST_CODE_EDIT = 1;
	ImageView menuOptions;
    Handler handler;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_community_details);
		activity = this;
		initObjectUI();
        initHandler();
		setupHideKeyboard(findViewById(R.id.layoutRootCommunityDetails));
	}

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Bundle data = msg.getData();
                String message = "";
                if (data.containsKey(ExtraConstants.MESSAGE)) message = data.getString(ExtraConstants.MESSAGE);
                if (msg.what != com.comeonbaby.android.app.requests.Constants.MSG_ERROR) showSnackMessage(message);

                switch (msg.what) {
					case com.comeonbaby.android.app.requests.Constants.MSG_DELETE_COMMUNITY_SUCCESS: {
						finish();
						break;
					}
					case com.comeonbaby.android.app.requests.Constants.MSG_DELETE_COMMUNITY_FAIL: {
						break;
					}
                    default: {
                        break;
                    }
                }
            }
        };
    }

    //Всплывающее сообщение
    private void showSnackMessage(String msg) {
        Snackbar.make(findViewById(R.id.layoutRootCommunityDetails), msg, Snackbar.LENGTH_LONG).show();
    }

	private void initObjectUI() {
		communityDto = (CommunityDTO) getIntent().getSerializableExtra(ServiceConsts.EXTRA_COMMUNITY);
		listComment = (ListView) findViewById(R.id.listviewComment);
		editCompose = (EditTextCustom) findViewById(R.id.editCompose);

		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((TextViewCustom) findViewById(R.id.textUsername)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((TextViewCustom) findViewById(R.id.textContent)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);

		menuOptions = (ImageView) findViewById(R.id.imgMenu);

		//Set enabled or disabled options menu
		menuOptions.setEnabled(false);
		menuOptions.setVisibility(View.INVISIBLE);
		if(communityDto != null && communityDto.getUser() != null && communityDto.getUser().getSystemID() != null &&
				communityDto.getUser().getSystemID().equals(AppSession.getSession().getSystemUser().getSystemID())) {
			Log.d(TAG, "SET ENABLED!!!!!!!");
			menuOptions.setEnabled(true);
			menuOptions.setVisibility(View.VISIBLE);
			menuOptions.setOnClickListener(this);
		}

		((ButtonCustom) findViewById(R.id.buttonWrite)).setOnClickListener(this);
		toggleLike = (CheckBoxCustom) findViewById(R.id.toggleLike);
		toggleLike.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.imgMenu:
			final OnClickListener sureListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.showProgress();




					Commands.deleteCommunityRecord(handler, communityDto);
				}
			};
			final OnClickListener notSureListener = new OnClickListener() {
				@Override
				public void onClick(View v) {}
			};

			OnClickListener yesButtonListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					launchActivity();
				}
			};
			OnClickListener noButtonListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					DialogUtilities.showConfirmDialog(activity, getString(R.string.dialog_title_delete_community), getString(R.string.dialog_message_delete_community), sureListener, notSureListener);
				}
			};
			DialogUtilities.showCommunityDialog(CommunityDetailsActivity.this, yesButtonListener, noButtonListener);


			break;
		case R.id.toggleLike:
//			showProgress();
//			PostLikeCommunityCommand.start(baseActivity, communityDto.getId());
		case R.id.imageRetry:
			try {
				CommentDTO comment = (CommentDTO) v.getTag();
				if (comment != null) {
//					showProgress();
//					PostCommentCommunityCommand.start(baseActivity, comment.getComment(), communityDto.getId() + "");
				}
			} catch (Exception e) {
			}
		default:
			break;
		}
	}

	private void showDataContent() {
		if (communityDto != null) {
			String imageUrl = "";
			if (!communityDto.getUser().getProfileDTO().getAvatar().isEmpty())
				imageUrl = com.comeonbaby.android.app.requests.Constants.IMAGES_URL + communityDto.getUser().getProfileDTO().getAvatar();
			ImageLoader.getInstance().displayImage(imageUrl, ((ImageView) findViewById(R.id.imgAvatar)), Constants.GROUP_AVATAR_PROFILE_DISPLAY_OPTIONS);
			((TextViewCustom) findViewById(R.id.txtTitle)).setText(communityDto.getTitle());
			((TextViewCustom) findViewById(R.id.textUsername)).setText(communityDto.getUser().getProfileDTO().getNickname());
			((TextViewCustom) findViewById(R.id.textDate)).setText(communityDto.getDate_created());
			((TextViewCustom) findViewById(R.id.textContent)).setText(communityDto.getContent());
			((TextViewCustom) findViewById(R.id.textCount)).setText(communityDto.getLike_count() + "");
			toggleLike.setChecked(communityDto.isLike());
			LinearLayout layoutImage = (LinearLayout) findViewById(R.id.layoutImage);
			layoutImage.removeAllViews();
			if (communityDto.getListImage() != null && communityDto.getListImage().size() > 0) {
				for (int i = 0; i < communityDto.getListImage().size(); i++) {
					ImageView imageView = new ImageView(this);
					LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					param.topMargin = 5;
					param.bottomMargin = 5;
					imageView.setLayoutParams(param);
					imageView.setScaleType(ScaleType.FIT_CENTER);
					String imgURL = com.comeonbaby.android.app.requests.Constants.IMAGES_URL + communityDto.getListImage().get(i).getImage();
					ImageLoader.getInstance().displayImage(imgURL, imageView, Constants.GROUP_AVATAR_DISPLAY_OPTIONS);
					layoutImage.addView(imageView);
				}
			}
		}
	}

	private void launchActivity() {
		Intent intent = new Intent(CommunityDetailsActivity.this, CommunityDetailsEditActivity.class);
		intent.putExtra(ServiceConsts.EXTRA_COMMUNITY, communityDto);
		startActivityForResult(intent, RESQUEST_CODE_EDIT);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK) {
			if (arg0 == RESQUEST_CODE_EDIT) {
				if (arg2 != null) {
					CommunityDTO dto = (CommunityDTO) arg2.getSerializableExtra(ServiceConsts.EXTRA_COMMUNITY);
					if (dto != null && dto.getId() > 0)
						communityDto = dto;
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		showDataContent();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}