package com.comeonbaby.android.app.model;

import java.io.Serializable;

public class Report implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7024225973074241011L;

	private int type;// 0: excellent, 1: good, 2: bad
	private int month;
	private int year;
	private int week;

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Report(int type, int month, int year) {
		this.type = type;
		this.month = month;
		this.year = year;
	}

	public Report(int type, int month, int year, int week) {
		this.type = type;
		this.month = month;
		this.year = year;
		this.week = week;
	}
}