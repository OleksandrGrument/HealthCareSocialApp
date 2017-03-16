package com.comeonbaby.android.app.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.CommunityQADTO;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.CheckBoxCustom;
import com.comeonbaby.android.app.view.customview.EditTextCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

//import com.carmunity.core.dto.CommunityQADTO;
//import com.carmunity.core.system.commands.Command;
//import com.carmunity.core.system.commands.PostCommunityQACommand;
//import com.carmunity.core.system.commands.ServiceConsts;

public class QADetailsNewActivity extends BaseActivity implements OnClickListener {

	private String TAG = "QADetailsActivity";
	Handler handler;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_qa_details_new);
		initObjectUI();
		initHandler();
		setupHideKeyboard(findViewById(R.id.layoutRootCommunityDetailsEdit));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_qa_new);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((EditTextCustom) findViewById(R.id.textTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((EditTextCustom) findViewById(R.id.textContent)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.buttonDone)).setOnClickListener(this);
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
		default:
			break;
		}
	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				hideProgress();
				switch (msg.what) {
					case com.comeonbaby.android.app.requests.Constants.MSG_SAVE_QA_SUCCESS: {
						Log.d(TAG, "SAVE QA SUCCESS!!!!");
						finish();
						break;
					}
					case com.comeonbaby.android.app.requests.Constants.MSG_SAVE_QA_FAIL: {
						Log.d(TAG, "SAVE QA FAIL!!!!");
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

	private void requestToServer() {
		showProgress();
		CommunityQADTO dto = new CommunityQADTO();
		dto.setTitle(((EditTextCustom) findViewById(R.id.textTitle)).getText().toString());
		dto.setQuestion_text(((EditTextCustom) findViewById(R.id.textContent)).getText().toString());
		dto.setIs_private(((CheckBoxCustom) findViewById(R.id.checkboxLockUnlock)).isChecked());

		showProgress();

//        ServerEmulator.addNewQuestionRecord(dto);
		Commands.saveQARecord(handler, dto);
        Log.e("QADetailsNewActivity:", "Метод: requestToServer()");
		//finish();
//		PostCommunityQACommand.start(baseActivity, dto);
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
//		addAction(ServiceConsts.POST_COMMUNITY_QA_SUCCESS_ACTION, new PostCommunitySuccessAction());
//		addAction(ServiceConsts.POST_COMMUNITY_QA_FAIL_ACTION, new PostCommunityFailAction());
//		updateBroadcastActionList();
//	}
//
//	private void removeActions() {
//		removeAction(ServiceConsts.POST_COMMUNITY_QA_SUCCESS_ACTION);
//		removeAction(ServiceConsts.POST_COMMUNITY_QA_FAIL_ACTION);
//		updateBroadcastActionList();
//	}
//
//	private class PostCommunitySuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//			finish();
//		}
//	}
//
//	private class PostCommunityFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//			DialogUtilities.showAlertDialog(QADetailsNewActivity.this,
//					R.layout.dialog_error_warning, R.string.string_error,
//					R.string.error_message_for_service_unavailable, null);
//		}
//	}
}