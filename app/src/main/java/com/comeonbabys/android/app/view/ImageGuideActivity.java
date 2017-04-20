package com.comeonbabys.android.app.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.comeonbabys.android.app.db.dto.Guide;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.comeonbabys.android.app.db.dto.Recipe.recipes;


public class ImageGuideActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_GUIDE_NUM = "guideNo";
    Guide guide;

    @Override
    protected void onCreateContent(Bundle savedInstanceState) {
        setContentView(R.layout.activity_image_guide);

        int guideNo = (Integer) getIntent().getExtras().get(EXTRA_GUIDE_NUM);

        guide = Guide.guide.get(guideNo);
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

    public void onClickInImage(View v) {
        if (!"".equals(guide.getUrl())) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(guide.getUrl()));
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(this, "No link", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {

    }
}
