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
import com.comeonbabys.android.app.db.dto.CityDTO;
import com.comeonbabys.android.app.view.customview.CheckBoxCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;


import java.util.List;

/**
 * @author PvTai
 */
public class ListCityAdapter extends BaseAdapter {

	private List<CityDTO> dataSource;
	private LayoutInflater inflater;
	Context mainActivity = null;
	private float edittextSize;
	private Long selected;
	OnEventControlListener onclick;
	int type;

	public ListCityAdapter(List<CityDTO> dataSource, Context ctx,
						   float edittextSize, Long selected,
						   OnEventControlListener onclick, int type) {
		this.dataSource = dataSource;
		this.inflater = LayoutInflater.from(ctx);
		mainActivity = ctx;
		this.edittextSize = edittextSize;
		this.selected = selected;
		this.onclick = onclick;
		this.type = type;
	}

	//Вернуть колличество городов в списке
	@Override
	public int getCount() {
		return (dataSource != null) ? dataSource.size() : 0;
	}

	//Вернуть город с определенным индексом?
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
		Log.d("ListCameraAdapter", "getView: start..");
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_single_choice, null);
			holder = new ViewHolder();
			holder.txtName = (TextViewCustom) convertView.findViewById(R.id.textName);
			holder.checkbox = (CheckBoxCustom) convertView.findViewById(R.id.checkboxChoice);
			convertView.setTag(holder);
			holder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, edittextSize);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CityDTO cam = (CityDTO) getItem(position);
		if (cam == null) {
			holder.txtName.setTag(null);
			return null;
		}
		holder.txtName.setTag(cam);
		holder.txtName.setText(cam.getName());
		if (selected == cam.getId())
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