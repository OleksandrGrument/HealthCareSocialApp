package com.comeonbaby.android.app.view.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextCustom extends EditText {
	
	public EditTextCustom(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public EditTextCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public EditTextCustom(Context context) {
		super(context);
	}
	
	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
//		int style = Typeface.NORMAL;
//		try {
//			style = getTypeface().getStyle();
//		}
//		catch (Exception e) {
//			style = Typeface.NORMAL;
//		}
//		if (style == Typeface.BOLD) {
//			super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/MyriadPro-Bold.ttf"));
//		}
//		else if (style == Typeface.ITALIC) {
//			super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/MyriadPro-Italic.ttf"));
//		}
//		else if (style == Typeface.BOLD_ITALIC) {
//			super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/MyriadPro-Bold_Italic.ttf"));
//		}
//		else {
//			super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/MyriadPro.ttf"));
//		}
	}
	
//	/*
//	 * (non-Javadoc)
//	 * @see android.widget.TextView#onFocusChanged(boolean, int, android.graphics.Rect)
//	 */
//	@Override
//	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//		super.onFocusChanged(focused, direction, previouslyFocusedRect);
//		if (focused) {
//			isFocusable = true;
//			setText(storeString);
//		}
//		else {
//			isFocusable = false;
//			String NOW = getText().toString();
//			if (NOW != null && NOW.length() >= MAX_LENGTH) {
//				dotsString = NOW.substring(0, MAX_LENGTH) + "...";
//				setText(dotsString);
//			}
//		}
//	}
//	
	/*
	 * (non-Javadoc)
	 * @see android.widget.TextView#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	protected void onTextChanged(final CharSequence text, int start, int lengthBefore, int lengthAfter) {
//		Log.e("TAI", "onTextChanged: " + text.toString());
//		Log.e("TAI", "onTextChanged: storeString " + storeString);
//		if(isFocusable == false){
//			if (!TextUtils.isEmpty(text)){
//				if (!text.toString().contains("...")) {
//					Log.e("TAI", "onTextChanged 1: " + text.toString());
//					storeString = text.toString();
//					String NOW = text.toString();
//					if (NOW != null && NOW.length() >= MAX_LENGTH) {
//						String text1 = NOW.substring(0, MAX_LENGTH) + "...";
//						setText(text1);
//					}
//					Log.e("TAI", "onTextChanged text: " + text.toString());
//				}
//			}
//		}
//		else{
//			post(new Runnable() {
//				@Override
//				public void run() {
//					if(!TextUtils.isEmpty(text))
//						setSelection(text.length());
//				}
//			});
//		}
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		//correctWidth(getWidth());
	}
	
//	public void correctWidth(int desiredWidth) {
//		Paint paint = new Paint();
//		Rect bounds = new Rect();
//		
//		paint.setTypeface(getTypeface());
//		float textSize = getTextSize();
//		paint.setTextSize(textSize);
//		String text = getText().toString();
//		paint.getTextBounds(text, 0, text.length(), bounds);
//		
//		while (bounds.width() > desiredWidth) {
//			textSize--;
//			paint.setTextSize(textSize);
//			paint.getTextBounds(text, 0, text.length(), bounds);
//		}
//		
//		setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//	}
}