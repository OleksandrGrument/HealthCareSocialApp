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

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.common.ServerPath;
import com.comeonbabys.android.app.common.ServiceConsts;
import com.comeonbabys.android.app.db.dto.CommunityQADTO;
import com.comeonbabys.android.app.requests.commands.Commands;
import com.comeonbabys.android.app.utils.AppSession;
import com.comeonbabys.android.app.view.customview.ButtonCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;
import com.nostra13.universalimageloader.core.ImageLoader;
import static com.comeonbabys.android.R.id.btnButtonDeleteQA;

public class QADetailsActivity extends BaseActivity implements OnClickListener {

	CommunityQADTO item;
	Handler handler;


	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_qa_details);
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootQADetails));
		initHandler();
		ButtonCustom btnDelete = (ButtonCustom) findViewById(btnButtonDeleteQA);

		btnDelete.setEnabled(false);
		btnDelete.setVisibility(View.INVISIBLE);
		if(item != null && item.getUser() != null && item.getUser().getSystemID() != null &&
				item.getUser().getSystemID().equals(AppSession.getSession().getSystemUser().getSystemID())) {
			Log.d("SET ENABLED!!!!!!!", "SET ENABLED!!!!!!!");
			btnDelete.setEnabled(true);
			btnDelete.setVisibility(View.VISIBLE);
			btnDelete.setOnClickListener(this);
		}
	}

	private void initObjectUI() {
		item = (CommunityQADTO) getIntent().getSerializableExtra(ServiceConsts.EXTRA_COMMUNITY_QA);

		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((TextViewCustom) findViewById(R.id.textUsername)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((TextViewCustom) findViewById(R.id.textContent)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(btnButtonDeleteQA)).setOnClickListener(this);

		if (item != null) {
			((TextViewCustom) findViewById(R.id.txtTitle)).setText(item.getTitle());
			((TextViewCustom) findViewById(R.id.textUsername)).setText(item.getUser().getProfileDTO().getNickname());
			((TextViewCustom) findViewById(R.id.textDate)).setText(item.getQuestion_date());
			((TextViewCustom) findViewById(R.id.textContent)).setText(item.getQuestion_text());

			if (item.isIs_private()) {
				((TextViewCustom) findViewById(R.id.txtTitle)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_lock, 0, 0, 0);
				((TextViewCustom) findViewById(R.id.txtTitle)).setCompoundDrawablePadding(5);
			} else {
				((TextViewCustom) findViewById(R.id.txtTitle)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				((TextViewCustom) findViewById(R.id.txtTitle)).setCompoundDrawablePadding(0);
			}

			if (!TextUtils.isEmpty(item.getUser().getProfileDTO().getAvatar())) {
				String imageUrl = "";
				if (item.getUser().getProfileDTO().getAvatar().contains("http://") || item.getUser().getProfileDTO().getAvatar().contains("https://"))
					imageUrl = item.getUser().getProfileDTO().getAvatar();
				else
					imageUrl = ServerPath.SERVER_MEDIA + item.getUser().getProfileDTO().getAvatar();
				ImageLoader.getInstance().displayImage(imageUrl, ((ImageView) findViewById(R.id.imgAvatar)), Constants.PROFILE_AVATAR_FEMALE_DISPLAY_OPTIONS);
			}
//			if (item.isIs_answered() && item.getUserAnswer() != null) {
			if (item.isIs_answered()) {
				((LinearLayout) findViewById(R.id.layoutAnswer)).setVisibility(View.VISIBLE);
//				((TextViewCustom) findViewById(R.id.textUsernameAnswered)).setText(item.getUserAnswer().getProfileDTO().getNickname());
				((TextViewCustom) findViewById(R.id.textUsernameAnswered)).setText("Admin");
				((TextViewCustom) findViewById(R.id.textDateAnswered)).setText(item.getAnswer_date());
				((TextViewCustom) findViewById(R.id.textAnswered)).setText(item.getAnswer_text());
			} else {
				((LinearLayout) findViewById(R.id.layoutAnswer)).setVisibility(View.GONE);
			}
		}
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case btnButtonDeleteQA:
			showProgress();
			Commands.deleteQARecord(handler, item);
			//ServerEmulator.removeQuestionRecord(item.getId());
			finish();
//			DeleteCommunityQACommand.start(baseActivity, item);
			break;
		default:
			break;
		}
	}



	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
					case com.comeonbabys.android.app.requests.Constants.MSG_DELETE_COMMUNITY_SUCCESS: {
						finish();
						break;
					}
					case com.comeonbabys.android.app.requests.Constants.MSG_DELETE_COMMUNITY_FAIL: {
						hideProgress();
						break;
					}
					default: {
						break;
					}
				}
			}
		};
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
//		addAction(ServiceConsts.DELETE_COMMUNITY_QA_SUCCESS_ACTION, new DeleteCommunityQASuccessAction());
//		addAction(ServiceConsts.DELETE_COMMUNITY_QA_FAIL_ACTION, new DeleteCommunityQAFailAction());
//		updateBroadcastActionList();
//	}
//
//	private void removeActions() {
//		removeAction(ServiceConsts.DELETE_COMMUNITY_QA_SUCCESS_ACTION);
//		removeAction(ServiceConsts.DELETE_COMMUNITY_QA_FAIL_ACTION);
//		updateBroadcastActionList();
//	}
//
//	private class DeleteCommunityQASuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//			finish();
//		}
//	}
//
//	private class DeleteCommunityQAFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//			DialogUtilities.showAlertDialog(QADetailsActivity.this,
//					R.layout.dialog_error_warning, R.string.string_error,
//					R.string.error_message_for_service_unavailable, null);
//		}
//	}
}