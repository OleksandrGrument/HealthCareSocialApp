package com.comeonbabys.android.app.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;


public class ScrollViewNoScroll extends ScrollView { 
	private boolean on = true;
	
    public ScrollViewNoScroll(Context context) { 
    	super(context);     
    } 
    
    public ScrollViewNoScroll(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);     
    } 
	
    public ScrollViewNoScroll(Context context, AttributeSet attrs) { 
    	super(context, attrs); 
    } 

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) { 
    	if(on)
    		return super.onInterceptTouchEvent(ev);
    	else
    	return false; 
    } 
    //turn on the scroll view   
    public void enable()
    {
    	on = true;   
	}
    //turn off the scroll view   
    public void disable()
    {
    	on = false;   
    }  
    
    @Override     
    protected void onScrollChanged(int x, int y, int oldx, int oldy) 
    {         
    	super.onScrollChanged(x, y, oldx, oldy);         
    }
} 



