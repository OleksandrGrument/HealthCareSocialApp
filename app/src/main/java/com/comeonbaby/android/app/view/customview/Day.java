package com.comeonbaby.android.app.view.customview;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;
import android.widget.BaseAdapter;

import com.comeonbaby.android.app.db.dto.NoteDTO;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Day implements Parcelable {

	int startDay;
	int monthEndDay;
	int day;
	int year;
	int month;
	Context context;
	BaseAdapter adapter;
	NoteDTO note;

	public Day(Context context, int day, int year, int month) {
		this.day = day;
		this.year = year;
		this.month = month;
		this.context = context;
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		int end = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(year, month, end);
		TimeZone tz = TimeZone.getDefault();
		monthEndDay = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
	}

	public Day(Parcel source) {
		startDay = source.readInt();
		monthEndDay = source.readInt();
		day = source.readInt();
		year = source.readInt();
		month = source.readInt();
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}

	/**
	 * Add an event to the day
	 * 
	 * @param event
	 */
	public void addNote(NoteDTO note) {
		this.note = note;
	}

	/**
	 * Set the start day
	 * 
	 * @param startDay
	 */
	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getStartDay() {
		return startDay;
	}

	/**
	 * Get all the events on the day
	 * 
	 * @return list of events
	 */
	public NoteDTO getNote() {
		return note;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(startDay);
		dest.writeInt(monthEndDay);
		dest.writeInt(day);
		dest.writeInt(year);
		dest.writeInt(month);
	}

	public static final Creator<Day> CREATOR = new Creator<Day>() {

		@Override
		public Day[] newArray(int size) {
			return new Day[size];
		}

		@Override
		public Day createFromParcel(Parcel source) {
			return new Day(source);
		}
	};
}
