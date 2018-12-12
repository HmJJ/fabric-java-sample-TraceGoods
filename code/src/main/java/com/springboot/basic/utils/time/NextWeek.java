package com.springboot.basic.utils.time;

import java.util.Calendar;
import java.util.Date;

public class NextWeek {
	public static final int ERROR = -1;
	@SuppressWarnings("unused")
	private static final int ONE = 1;
	private Calendar date;

	public NextWeek(Date date) {
		this.date = DateUtils.getCalendar(date);
	}

	public Date someday(int field) throws NullPointerException {
		if (null == this.date) {
			throw new NullPointerException("\'date\' can\'t be null");
		} else {
			Calendar temp = (Calendar) this.date.clone();

			do {
				temp = DateUtils.add(temp, 5, 1);
			} while (temp.get(7) != field);

			return temp.getTime();
		}
	}

	public Date SUNDAY() throws NullPointerException {
		return this.someday(1);
	}

	public Date MONDAY() throws NullPointerException {
		return this.someday(2);
	}

	public Date TUESDAY() throws NullPointerException {
		return this.someday(3);
	}

	public Date WEDNESDAY() throws NullPointerException {
		return this.someday(4);
	}

	public Date THURSDAY() throws NullPointerException {
		return this.someday(5);
	}

	public Date FRIDAY() throws NullPointerException {
		return this.someday(6);
	}

	public Date SATURDAY() throws NullPointerException {
		return this.someday(7);
	}
}
