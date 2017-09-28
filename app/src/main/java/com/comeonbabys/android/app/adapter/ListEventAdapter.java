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

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.db.dto.CommunityDTO;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

import java.util.ArrayList;
import java.util.List;


public class ListEventAdapter extends BaseAdapter implements Filterable {

	private List<CommunityDTO> dataSource;
	private LayoutInflater inflater;
	Context mainActivity = null;

	public ListEventAdapter(List<CommunityDTO> dataSource, Context ctx) {
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
			convertView = inflater.inflate(R.layout.item_event, null);
			holder = new ViewHolder();
			holder.txtName = (TextViewCustom) convertView.findViewById(R.id.textName);
			holder.txtDate = (TextViewCustom) convertView.findViewById(R.id.textDate);
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
		return convertView;
	}

	private static class ViewHolder {
		TextViewCustom txtName;
		TextViewCustom txtDate;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
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