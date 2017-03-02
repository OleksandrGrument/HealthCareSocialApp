package com.comeonbaby.android.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.db.dto.ReportDTO;
import com.comeonbaby.android.app.utils.ConstsCore;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

import java.util.Calendar;
import java.util.List;

/**
 * @author PvTai
 */
public class ListReportAdapter extends BaseAdapter {

	private List<ReportDTO> dataSource;
	private LayoutInflater inflater;
	Context mainActivity = null;
	int type;// 1: month, 2: week
	int year, month;

	public ListReportAdapter(List<ReportDTO> dataSource, Context ctx, int type, int year, int month) {
		this.dataSource = dataSource;
		this.inflater = LayoutInflater.from(ctx);
		mainActivity = ctx;
		this.type = type;
		this.year = year;
		this.month = month;
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
			convertView = inflater.inflate(R.layout.item_report, null);
			holder = new ViewHolder();
			holder.txtDate = (TextViewCustom) convertView.findViewById(R.id.textDate);
			holder.txtRating = (TextViewCustom) convertView.findViewById(R.id.textRating);
			holder.txtNote = (TextViewCustom) convertView.findViewById(R.id.textNote);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ReportDTO cam = (ReportDTO) getItem(position);
		if (cam == null) {
			return null;
		}

		if (type == 1) {
			if (cam.getStatusMonth() == ReportDTO.EXELENT) {
				holder.txtRating.setBackgroundResource(R.drawable.bg_rating_report_excellent);
				holder.txtNote.setText(R.string.text_report_excellent);
				holder.txtRating.setText(R.string.text_report_excellent1);
			} else if (cam.getStatusMonth() == ReportDTO.GOOD) {
				holder.txtRating.setBackgroundResource(R.drawable.bg_rating_report_good);
				holder.txtNote.setText(R.string.text_report_good);
				holder.txtRating.setText(R.string.text_report_good1);
			} else {
				holder.txtRating.setBackgroundResource(R.drawable.bg_rating_report_bad);
				holder.txtNote.setText(R.string.text_report_bad);
				holder.txtRating.setText(R.string.text_report_bad1);
			}
			holder.txtDate.setText(year + "." + (cam.getMonth() > 9
					? cam.getMonth()
					: "0" + cam.getMonth()));
		} else {
			if (cam.getStatusWeek() == ReportDTO.EXELENT) {
				holder.txtRating.setBackgroundResource(R.drawable.bg_rating_report_excellent);
				holder.txtNote.setText(R.string.text_report_excellent);
				holder.txtRating.setText(R.string.text_report_excellent1);
			} else if (cam.getStatusWeek() == ReportDTO.GOOD) {
				holder.txtRating.setBackgroundResource(R.drawable.bg_rating_report_good);
				holder.txtNote.setText(R.string.text_report_good);
				holder.txtRating.setText(R.string.text_report_good1);
			} else {
				holder.txtRating.setBackgroundResource(R.drawable.bg_rating_report_bad);
				holder.txtNote.setText(R.string.text_report_bad);
				holder.txtRating.setText(R.string.text_report_bad1);
			}

			holder.txtDate.setText(year + mainActivity.getString(R.string.text_year) + " " + month + mainActivity.getString(R.string.text_month) + " " + cam.getWeek() + mainActivity.getString(R.string.text_week));
		}
		return convertView;
	}

	private static class ViewHolder {
		TextViewCustom txtDate;
		TextViewCustom txtRating;
		TextViewCustom txtNote;
	}
}