package com.comeonbabys.android.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.db.dto.CommentDTO;
import com.comeonbabys.android.app.view.CommunityDetailsActivity;
import com.comeonbabys.android.app.view.customview.TextViewCustom;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

import java.util.List;

import static android.R.attr.x;


public class ListCommentAdapter extends BaseAdapter {

	private List<CommentDTO> dataSource;
	private LayoutInflater inflater;
	Context mainActivity = null;

	public ListCommentAdapter(List<CommentDTO> dataSource, Context ctx) {
		this.dataSource = dataSource;
		this.inflater = LayoutInflater.from(ctx);
		mainActivity = ctx;
	}

	@Override
	public int getCount() {
		return (dataSource != null) ? dataSource.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return (dataSource != null) ? dataSource.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_comment, null);

		/*	convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT));*/
			holder = new ViewHolder();
			holder.txtName = (TextViewCustom) convertView.findViewById(R.id.textName);
			holder.txtDate = (TextViewCustom) convertView.findViewById(R.id.textDate);
			holder.txtContent = (TextViewCustom) convertView.findViewById(R.id.textContent);
			holder.imgThumnail = (ImageView) convertView.findViewById(R.id.imgAvatar);
			holder.imageRetry = (ImageView) convertView.findViewById(R.id.imageRetry);
			convertView.setTag(holder);

			holder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CommentDTO cam = (CommentDTO) getItem(position);
		if (cam == null) {
			return null;
		}
		holder.txtName.setText(cam.getNickname());
		holder.txtDate.setText(cam.getDate_created());
		holder.txtContent.setText(cam.getComment());
		//---------holder.imageRetry.setVisibility(cam.isFail() ? View.VISIBLE : View.GONE);
		holder.imageRetry.setOnClickListener(CommunityDetailsActivity.activity);
		holder.imageRetry.setTag(cam);
//		String imageUrl = "";
//		if (cam.getUser().getProfileDTO().getAvatar().contains("http://") || cam.getUser().getProfileDTO().getAvatar().contains("https://"))
//			imageUrl = cam.getUser().getProfileDTO().getAvatar();
//		else
//			imageUrl = ServerPath.SERVER_MEDIA + cam.getUser().getProfileDTO().getAvatar();
//		ImageLoader.getInstance().displayImage(imageUrl, holder.imgThumnail,
//				Constants.GROUP_AVATAR_COMMENT_DISPLAY_OPTIONS);

		if(!cam.getAvatar().isEmpty()) {
			String imgURL = com.comeonbabys.android.app.requests.Constants.IMAGES_URL + cam.getAvatar();
			ImageLoader.getInstance().displayImage(imgURL, holder.imgThumnail, Constants.GROUP_AVATAR_DISPLAY_OPTIONS);
		}

/*		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		convertView.setLayoutParams(params);*/

		return convertView;
	}

	private static class ViewHolder {
		TextViewCustom txtName;
		TextViewCustom txtDate;
		TextViewCustom txtContent;
		ImageView imgThumnail;
		ImageView imageRetry;
	}
}