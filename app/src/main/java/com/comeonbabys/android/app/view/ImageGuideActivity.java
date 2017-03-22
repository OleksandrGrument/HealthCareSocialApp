package com.comeonbabys.android.app.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.comeonbabys.android.app.db.dto.Guide;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ImageGuideActivity extends BaseActivity implements View.OnClickListener{

    public static final String EXTRA_GUIDE_NUM = "guideNo";

    @Override
    protected void onCreateContent(Bundle savedInstanceState) {
        setContentView(R.layout.activity_image_guide);

        int guideNo = (Integer) getIntent().getExtras().get(EXTRA_GUIDE_NUM);

        Guide guide = Guide.guide.get(guideNo);
        ImageView photo = (ImageView) findViewById(R.id.icon);
        ImageLoader.getInstance().displayImage(guide.getUrl_image(), photo, Constants.PROFILE_AVATAR_MALE_DISPLAY_OPTIONS);

        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {

    }
}
