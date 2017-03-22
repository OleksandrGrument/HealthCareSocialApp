package com.comeonbabys.android.app.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.common.ServerPath;
import com.comeonbabys.android.app.common.ServiceConsts;
import com.comeonbabys.android.app.db.dto.ComeOnGuideDTO;
import com.comeonbabys.android.app.db.dto.CommunityDTO;
import com.comeonbabys.android.app.view.customview.TextViewCustom;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HtmlContentFragment extends BaseContainerFragment implements OnClickListener {
	CommunityDTO communityDto;
	ComeOnGuideDTO comeOnDto;
	ScrollView scrollViewHtml;
	WebView webviewCustom;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_html_content, container, false);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle intent = getArguments();
		communityDto = (CommunityDTO) intent.getSerializable(ServiceConsts.EXTRA_COMMUNITY);
		comeOnDto = (ComeOnGuideDTO) intent.getSerializable(ServiceConsts.EXTRA_LIST_NANIN);
		LinearLayout layoutImage = (LinearLayout) getActivity().findViewById(R.id.layoutImage);
		TextViewCustom edtTitle = (TextViewCustom) getActivity().findViewById(R.id.txtTitle);
		edtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		scrollViewHtml = (ScrollView) getActivity().findViewById(R.id.scrollViewHtml);
		webviewCustom = (WebView) getActivity().findViewById(R.id.webviewCustom);
		webviewCustom.getSettings().setJavaScriptEnabled(true);
		webviewCustom.getSettings().setBuiltInZoomControls(true);
		webviewCustom.getSettings().setDisplayZoomControls(false);
		webviewCustom.getSettings().setLoadWithOverviewMode(true);
		webviewCustom.getSettings().setUseWideViewPort(true);
		webviewCustom.getSettings().setAllowFileAccess(true);

		webviewCustom.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webviewCustom.getSettings().setSupportZoom(false);
		webviewCustom.setVerticalScrollBarEnabled(false);
		webviewCustom.setHorizontalScrollBarEnabled(false);
		webviewCustom.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webviewCustom.getSettings().setDefaultFontSize((int) Globals.size);
		webviewCustom.setInitialScale(1);
		int minWidth = getResources().getDisplayMetrics().widthPixels;
		webviewCustom.setMinimumWidth(minWidth);
		
		if (communityDto != null) {
			edtTitle.setText(communityDto.getTitle());
			scrollViewHtml.setVisibility(View.VISIBLE);
			String content = communityDto.getContent();
			content = "<html><head><meta name=\"viewport\" content=\"width=100%, initial-scale=1\">" +
					"<style type=\"text/css\">"
					+ "p{text-align:justify; line-height:160%!important;}"
					+ "</style>"// color:green;
					+ "</head><body width=\"100%\"style=\"text-align:justify\"><p width=\"100%\">"
					+ content.replace("\r\n", "<br/>") + "</p><body></html>";
			webviewCustom.loadData(content, "text/html; charset=UTF-8", null);
			if (communityDto.getListImage() != null && communityDto.getListImage().size() > 0) {
				for (int i = 0; i < communityDto.getListImage().size(); i++) {
					ImageView imageView = new ImageView(getActivity());
					LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					param.topMargin = 5;
					param.bottomMargin = 5;
					imageView.setLayoutParams(param);
					imageView.setScaleType(ScaleType.FIT_CENTER);
					ImageLoader.getInstance().displayImage(ServerPath.SERVER_MEDIA + communityDto.getListImage().get(i).getImage(), imageView, Constants.GROUP_AVATAR_DISPLAY_OPTIONS);
					layoutImage.addView(imageView);
					Log.d("HtmlContentFragment!!!!",ServerPath.SERVER_MEDIA + communityDto.getListImage().get(i).getImage());
				}
			}

		} else if (comeOnDto != null) {
			edtTitle.setText(comeOnDto.getTitle());
			scrollViewHtml.setVisibility(View.GONE);
			String content = comeOnDto.getContent();
			webviewCustom.loadUrl(content);
//			int index = content.indexOf("<img");
//			String cut1 = content.substring(0, index + 4);
//			String cut2 = content.substring(index + 4);
//			content = cut1 + " width=\"100%\" " + cut2;
//			// content = content.replaceFirst(" ", " width=\"100%\" ");
//			webviewCustom.loadData(content, "text/html; charset=UTF-8", null);
		}
		webviewCustom.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webviewCustom.setWebChromeClient(new WebChromeClient() {
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
				webviewCustom.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				((BaseActivity) getActivity()).showProgress();
				Log.e("onPageStarted", url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				((BaseActivity) getActivity()).hideProgress();
				try {
					if (webviewCustom.getUrl() != null && !webviewCustom.getUrl().equals("")) {
						webviewCustom.getSettings().setBuiltInZoomControls(true);
						webviewCustom.getSettings().setDisplayZoomControls(false);
						webviewCustom.getSettings().setSupportZoom(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.e("onPageFinished", url);
			}

		});
		((ImageView) getActivity().findViewById(R.id.imgBack)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imgBack:
			((BaseContainerFragment) getParentFragment()).popFragment();
			break;
		default:
			break;
		}

	}
}
