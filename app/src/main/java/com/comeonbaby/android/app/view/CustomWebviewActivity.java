package com.comeonbaby.android.app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Constants;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

import java.lang.reflect.InvocationTargetException;

@SuppressLint("SetJavaScriptEnabled")
public class CustomWebviewActivity extends BaseActivity implements OnClickListener {

	private WebView webView;
	private ProgressBar progressBar;
	private ImageView imgBack;
	private TextViewCustom edtTitle;

	private String mRefUrl;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_custom_webview);

		Intent intent = getIntent();
		mRefUrl = intent.getStringExtra(Constants.INTENT_URL);
		webView = (WebView) findViewById(R.id.webviewCustom);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		String str_title = intent.getStringExtra(Constants.INTENT_STRING_TITLE);

		edtTitle = (TextViewCustom) findViewById(R.id.txtTitle);
		edtTitle.setText(str_title);
		edtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		imgBack = (ImageView) findViewById(R.id.imgBack);
		imgBack.setOnClickListener(this);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setAllowFileAccess(true);

		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setSupportZoom(false);
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		if (intent.getBooleanExtra(Constants.EX_URL_FROM_FILE, false)) {
			webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			webView.getSettings().setDefaultFontSize((int) Globals.size);
		} else {
			webView.setInitialScale(100);
		}

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.setWebChromeClient(new WebChromeClient() {
					private View mCustomView;

					@Override
					public void onShowCustomView(View view, CustomViewCallback callback) {
						if (mCustomView != null) {
							callback.onCustomViewHidden();
							return;
						}
						mCustomView = view;
					}
				});
				webView.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (!progressBar.isShown()) {
					progressBar.setVisibility(View.VISIBLE);
				}
				Log.e("onPageStarted", url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (progressBar.isShown())
					progressBar.setVisibility(View.GONE);
				try {
					if (webView.getUrl() != null && !webView.getUrl().equals("")) {
						webView.getSettings().setBuiltInZoomControls(true);
						webView.getSettings().setDisplayZoomControls(false);
						webView.getSettings().setSupportZoom(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.e("onPageFinished", url);
			}

		});

		new loadURLMethod().execute();
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			Class.forName("android.webkit.WebView").getMethod("onPause", (Class[]) null).invoke(webView, (Object[]) null);
		} catch (ClassNotFoundException cnfe) {
		} catch (NoSuchMethodException nsme) {
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		} catch (Exception e) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			Class.forName("android.webkit.WebView").getMethod("onResume", (Class[]) null).invoke(webView, (Object[]) null);
		} catch (ClassNotFoundException cnfe) {
		} catch (NoSuchMethodException nsme) {
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					webView.goBack();
				}
			});
			return true;
		}
		// If it wasn't the Back key or there's no web page history, bubble up
		// to the default
		// system behavior (probably exit the activity)
		return super.onKeyDown(keyCode, event);
	}

	class loadURLMethod extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			return true;
		}

		@Override
		public void onPostExecute(Boolean kq) {
			try {
				openURL(mRefUrl);
			} catch (Exception e) {
				e.printStackTrace();
				if (progressBar.isShown())
					progressBar.setVisibility(View.GONE);
			}
			onCancelled();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

	private void openURL(final String url) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				webView.loadUrl(url);
			}
		});
		webView.requestFocus();
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
}