package com.comeonbabys.android.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.OnEventControlListener;
import com.comeonbabys.android.app.view.customview.CheckBoxCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

/**
 * @author PvTai
 */
public class ListChoiceAdapter extends BaseAdapter {

	private String[] dataSource;
	private LayoutInflater inflater;
	Context mainActivity = null;
	private float edittextSize;
	private String selected;
	OnEventControlListener onclick;
	int type;

	public ListChoiceAdapter(String[] dataSource, Context ctx,
							 float edittextSize, String selected,
							 OnEventControlListener onclick, int type) {
		this.dataSource = dataSource;
		this.inflater = LayoutInflater.from(ctx);
		mainActivity = ctx;
		this.edittextSize = edittextSize;
		this.selected = selected;
		this.onclick = onclick;
		this.type = type;
	}

	@Override
	public int getCount() {
		return (dataSource != null) ? dataSource.length : 0;
	}

	@Override
	public Object getItem(int position) {
		return (dataSource != null) ? dataSource[position] : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("ListCameraAdapter", "getView: start..");
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_single_choice, null);
			holder = new ViewHolder();
			holder.txtName = (TextViewCustom) convertView
					.findViewById(R.id.textName);
			holder.checkbox = (CheckBoxCustom) convertView
					.findViewById(R.id.checkboxChoice);
			convertView.setTag(holder);
			holder.txtName
					.setTextSize(TypedValue.COMPLEX_UNIT_SP, edittextSize);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final String cam = (String) getItem(position);
		if (cam == null) {
			holder.txtName.setTag(null);
			return null;
		}
		holder.txtName.setTag(cam);
		holder.txtName.setText(cam);
		if (selected.equals(cam))
			holder.checkbox.setChecked(true);
		else
			holder.checkbox.setChecked(false);
		holder.checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onclick.onEvent(type, null, cam);
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		TextViewCustom txtName;
		CheckBoxCustom checkbox;
	}
}