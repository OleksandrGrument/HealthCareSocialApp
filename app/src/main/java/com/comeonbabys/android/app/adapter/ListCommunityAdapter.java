package com.comeonbabys.android.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.db.dto.CommunityDTO;
import com.comeonbabys.android.app.view.customview.TextViewCustom;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PvTai
 */
public class ListCommunityAdapter extends BaseAdapter implements Filterable {

	private List<CommunityDTO> dataSource;
	private LayoutInflater inflater;
	Context mainActivity = null;

	public ListCommunityAdapter(List<CommunityDTO> dataSource, Context ctx) {
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
			convertView = inflater.inflate(R.layout.item_mysuccess, null);
			holder = new ViewHolder();
			holder.txtName = (TextViewCustom) convertView.findViewById(R.id.textName);
			holder.txtDate = (TextViewCustom) convertView.findViewById(R.id.textDate);
			holder.txtUsername = (TextViewCustom) convertView.findViewById(R.id.textUsername);
			holder.txtComment = (TextViewCustom) convertView.findViewById(R.id.textComment);
			holder.txtContent = (TextViewCustom) convertView.findViewById(R.id.textContent);
			holder.imgThumnail = (ImageView) convertView.findViewById(R.id.imgThumbnail);
			convertView.setTag(holder);
			holder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CommunityDTO cam = (CommunityDTO) getItem(position);
		if (cam == null) {
			return null;
		}
		holder.txtName.setText(cam.getTitle());
		holder.txtDate.setText(cam.getDate_created());
		holder.txtUsername.setText(cam.getUser().getProfileDTO().getNickname());
		holder.txtComment.setText(String.format(mainActivity.getString(R.string.text_like), "" + cam.getLike_count()));
		holder.txtContent.setText(cam.getContent());

		//TODO ЗДЕСЬ ГРУЗЯТСЯ КАРТИНКИ
		String thumbURL;
		if (cam.getListImage() != null && cam.getListImage().size() > 0) {
			thumbURL = com.comeonbabys.android.app.requests.Constants.IMAGES_URL + cam.getListImage().get(0).getImage();
			ImageLoader.getInstance().displayImage(thumbURL, holder.imgThumnail, Constants.GROUP_AVATAR_DISPLAY_OPTIONS);
		}
		else
			ImageLoader.getInstance().displayImage("", holder.imgThumnail, Constants.GROUP_AVATAR_DISPLAY_OPTIONS);
		return convertView;
	}

	private static class ViewHolder {
		TextViewCustom txtName;
		TextViewCustom txtDate;
		TextViewCustom txtUsername;
		TextViewCustom txtComment;
		TextViewCustom txtContent;
		ImageView imgThumnail;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.values != null) {
					dataSource = (List<CommunityDTO>) results.values;
				}
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<CommunityDTO> list = new ArrayList<CommunityDTO>();
				for (int i = 0; i < dataSource.size(); i++) {
					if (String.valueOf(dataSource.get(i).getContent_type()).equals(constraint)) {
						list.add(dataSource.get(i));
					}
				}
				results.count = list.size();
				results.values = list;
				return results;
			}
		};
		return filter;
	}
}