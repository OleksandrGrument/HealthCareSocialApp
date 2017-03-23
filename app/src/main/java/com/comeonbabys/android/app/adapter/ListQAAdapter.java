package com.comeonbabys.android.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.db.dto.CommunityQADTO;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

import java.util.List;

/**
 * @author PvTai
 */
public class ListQAAdapter extends BaseAdapter {

	private List<CommunityQADTO> dataSource;
	private LayoutInflater inflater;
	Context mainActivity = null;

	public ListQAAdapter(List<CommunityQADTO> dataSource, Context ctx) {
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
			convertView = inflater.inflate(R.layout.item_qa, null);
			holder = new ViewHolder();
			holder.txtName = (TextViewCustom) convertView.findViewById(R.id.textName);
			holder.txtDate = (TextViewCustom) convertView.findViewById(R.id.textDate);
			holder.txtTraLoi = (TextViewCustom) convertView.findViewById(R.id.textTraLoi);

			convertView.setTag(holder);
			holder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CommunityQADTO cam = (CommunityQADTO) getItem(position);
		if (cam == null) {
			return null;
		}
		if (cam.isIs_private()) {
			holder.txtName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_lock_list, 0, 0, 0);
			holder.txtName.setCompoundDrawablePadding(5);
		} else {
			holder.txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			holder.txtName.setCompoundDrawablePadding(0);
		}
		holder.txtName.setText(cam.getTitle());
		holder.txtDate.setText(cam.getQuestion_date());
		if (cam.isIs_answered())
			holder.txtTraLoi.setText(mainActivity.getString(R.string.text_answered));
		else
			holder.txtTraLoi.setText(mainActivity.getString(R.string.text_not_answer));
		return convertView;
	}

	private static class ViewHolder {
		TextViewCustom txtName;
		TextViewCustom txtDate;
		TextViewCustom txtTraLoi;
	}
}