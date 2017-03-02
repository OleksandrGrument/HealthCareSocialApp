package com.comeonbaby.android.app.view.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

public class CheckBoxCustom extends CheckBox {
	public CheckBoxCustom(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public CheckBoxCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CheckBoxCustom(Context context) {
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
	
	@Override
	public void setPressed(boolean pressed) {
		// Make sure the parent is a View prior casting it to View
		if (pressed && getParent() instanceof View && ((View) getParent()).isPressed()) {
			return;
		}
		super.setPressed(pressed);
	}
}
