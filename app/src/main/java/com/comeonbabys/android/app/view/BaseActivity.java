package com.comeonbabys.android.app.view;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.OnEventControlListener;
import com.comeonbabys.android.app.utils.AppSession;
import com.comeonbabys.android.app.utils.PrefsHelper;
import com.comeonbabys.android.app.view.customview.ButtonCustom;
import com.comeonbabys.android.app.view.customview.EditTextCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;


public abstract class BaseActivity extends FragmentActivity implements OnEventControlListener {
    public static BaseActivity baseActivity;
    private View mainView;
    public boolean isFinished = false;
    protected PrefsHelper prefsHelper;

    private static final int DIALOG_ID_PROGRESS_DEFAULT = 0x174980;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        baseActivity = this;
        onBeforeCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        //failAction = new FailAction();
        //successAction = new SuccessAction();
        //activityHelper = new ActivityHelper(this);
        //activityHelper.onCreate();
        prefsHelper = new PrefsHelper(this);
        onCreateContent(savedInstanceState);
        onAfterCreate(savedInstanceState);
    }

    protected void onBeforeCreate(Bundle savedInstanceState) {
    }

    protected void onAfterCreate(Bundle savedInstanceState) {
    }

    protected void onCreateContent(Bundle savedInstanceState) {
        LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        int rid = getContentViewID();
        if (rid != -1) {
            mainView = inflator.inflate(getContentViewID(), null, false);
        } else {
            mainView = createContentView();
        }
        setContentView(mainView);
    }

    protected View createContentView() {
        View viev = findViewById(getContentViewID());
        return viev;
    }

    protected int getContentViewID() {
        return -1;
    }

    protected final View getMainView() {
        return mainView;
    }

    //Set up to hide keyboard when touch on non edittext
    public void setupHideKeyboard(View view) {
        final View basicView = view;
        if (view != null && (!(view instanceof EditText) || !view.isFocusable())) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {hideSoftKeyboard(BaseActivity.this, basicView);
                    return false;
                }
            });
        }
        // if the view is parent, then we will iterate through it
        if (view != null && view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupHideKeyboard(innerView);
            }
        }
    }

    //Hide soft keyboard on method call
    public void hideSoftKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * @author PvTai
     * @description set focusable false for all edit text view
     */
    public void setFocusable(View view) {
        // set up to hide when touch on non edittext
        if (view != null && (view instanceof EditText)) {
            view.setFocusable(false);
            view.setFocusableInTouchMode(false);
        }

        // if the view is parent, then we will iterate through it
        if (view != null && view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setFocusable(innerView);
            }
        }
    }

    /**
     * @author PvTai
     * @description set Clickable true for all edit text view
     */
    public void setClickable(View view) {
        // set up to hide when touch on non edittext
        if (view != null && !(view instanceof ViewGroup)) {
            view.setClickable(true);
        }

        // if the view is parent, then we will iterate through it
        if (view != null && view instanceof ViewGroup) {
            view.setClickable(true);
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setFocusable(innerView);
            }
        }
    }

    /**
     * @author PvTai
     * @description set focusable false for all edit text view
     */
    public void setSizeTextView(View view, float size) {
        // set up to hide when touch on non edittext
        if (view != null) {
            if ((view instanceof TextViewCustom))
                ((TextViewCustom) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            else if ((view instanceof EditTextCustom))
                ((EditTextCustom) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            else if ((view instanceof ButtonCustom))
                ((ButtonCustom) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }

        // if the view is parent, then we will iterate through it
        if (view != null && view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setSizeTextView(innerView, size);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void showProgress() {
        showDialog(DIALOG_ID_PROGRESS_DEFAULT);
    }

    @SuppressWarnings("deprecation")
    public void hideProgress() {
        try {
            removeDialog(DIALOG_ID_PROGRESS_DEFAULT);
        } catch (IllegalArgumentException iae) {
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_PROGRESS_DEFAULT:
                TransparentProgressDialog progressDlg = new TransparentProgressDialog(BaseActivity.this, R.drawable.spinner);
                progressDlg.setCancelable(false);
                progressDlg.setCanceledOnTouchOutside(false);
                return progressDlg;
            default:
                return super.onCreateDialog(id);

        }
    }

    /**
     * @author PvTai
     * @description custom progress dialog
     */
    private class TransparentProgressDialog extends Dialog {
        private ImageView iv;

        public TransparentProgressDialog(Context context, int resourceIdOfImage) {
            super(context, R.style.TransparentProgressDialog);
            WindowManager.LayoutParams wlmp = getWindow().getAttributes();
            wlmp.gravity = Gravity.CENTER_HORIZONTAL;
            getWindow().setAttributes(wlmp);
            setTitle(null);
            setCancelable(false);
            setOnCancelListener(null);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            int size = (int) getResources().getDimension(
                    R.dimen.size_progress_dialog);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            iv = new ImageView(context);
            iv.setImageResource(resourceIdOfImage);
            layout.addView(iv, params);
            addContentView(layout, params);
        }

        @Override
        public void show() {
            super.show();
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(400);
            iv.setAnimation(anim);
            iv.startAnimation(anim);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        isFinished = true;
        super.finish();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isActivityLogeable(this)) {
//            activityHelper.onResume();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        if (isActivityLogeable(this)) {
//            activityHelper.onPause();
//        }
//        super.onPause();
//    }

    @Override
    public void finishActivity(int requestCode) {
        isFinished = true;
        super.finishActivity(requestCode);
    }

//    public void showNoNetworkDialog(
//            NetworkStateReceiver.ConnectionChangedListener connListener) {
//        DialogUtilities.showNoNetworkDialog(this);
//        Globals.lostConnectionListener = connListener;
//    }
//
//    protected FailAction failAction;
//    protected SuccessAction successAction;
//    protected ActivityHelper activityHelper;

    protected void onFailAction(String action) {
    }

    protected void onSuccessAction(String action) {
    }

//    public class FailAction implements Command {
//
//        @Override
//        public void execute(Bundle bundle) {
//            Exception e = (Exception) bundle
//                    .getSerializable(ServiceConsts.EXTRA_ERROR);
//            ErrorUtils.showError(BaseActivity.this, e);
//            ErrorUtils.logError("CORE",
//                    "BaseActivity=>FailAction=>closeProgressDialog");
//            onFailAction(bundle.getString(ServiceConsts.COMMAND_ACTION));
//        }
//    }
//
//    public class SuccessAction implements Command {
//
//        @Override
//        public void execute(Bundle bundle) {
//            onSuccessAction(bundle.getString(ServiceConsts.COMMAND_ACTION));
//        }
//    }
//
//    public void addAction(String action, Command command) {
//        activityHelper.addAction(action, command);
//    }
//
//    public boolean hasAction(String action) {
//        return activityHelper.hasAction(action);
//    }
//
//    public void removeAction(String action) {
//        activityHelper.removeAction(action);
//    }
//
//    public void updateBroadcastActionList() {
//        activityHelper.updateBroadcastActionList();
//    }
//
//    public FailAction getFailAction() {
//        return failAction;
//    }
//
//    public boolean isActivityLogeable(Activity activity) {
//        return (activity instanceof QBLogeable);
//    }

    public void leaveActivity(Activity activty, String classStr, boolean needFinish, boolean needClearTop) {
        String[] split = classStr.split(" ");
        Class<?> classType = null;
        try {
            classType = Class.forName(split[1]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (classType != null) {
            Intent intent = new Intent(this, classType);
            if (needClearTop) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
            startActivity(intent);
            if (needFinish)
                activty.finish();
        }
    }

    //startActivityForResult plus options to finish, clear top and other
    public void leaveActivityForResult(Activity activty, String classStr,
                                       boolean needFinish, boolean needClearTop, Parcelable parcelable,
                                       Serializable serializable, int requestCode) {
        String[] split = classStr.split(" ");
        Class<?> classType = null;
        try {
            classType = Class.forName(split[1]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (classType != null) {
            Intent intent = new Intent(this, classType);
            if (needClearTop) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
            intent.putExtra("VALUE", parcelable);
            intent.putExtra("VALUE1", serializable);
            activty.startActivityForResult(intent, requestCode);
            if (needFinish)
                activty.finish();
        }
    }

    //Close session, clear all prefences and move to ChoseLoginActivity
    public void logOut(Activity activty) {
        AppSession.getSession().closeAndClear();
        //TODO LogOut from Facebook
        Intent intent = new Intent(this, ChooseLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        activty.finish();
    }
}