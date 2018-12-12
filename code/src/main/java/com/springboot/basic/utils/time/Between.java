package com.springboot.basic.utils.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Between {
	public static final int ERROR = -1;
	@SuppressWarnings("unused")
	private static final int ONE = 1;
	private Calendar start;
	private Calendar end;
	private List<Date> holidays;

	protected Between() {
	}

	public Between(Date start, Date end) {
		if (start.after(end)) {
			this.start = DateUtils.getCalendar(end);
			this.end = DateUtils.getCalendar(start);
		} else {
			this.start = DateUtils.getCalendar(start);
			this.end = DateUtils.getCalendar(end);
		}

	}

	public Between addHolidays(List<Date> holidays) {
		this.holidays = holidays;
		return this;
	}

	public int months() {
		if (null != this.start && null != this.end) {
			int months = this.end.get(2) - this.start.get(2);
			if (this.start.get(1) != this.end.get(1)) {
				Calendar temp = (Calendar) this.start.clone();

				do {
					months += temp.getActualMaximum(2) + 1;
					temp = DateUtils.add(temp, 1, 1);
				} while (temp.get(1) != this.end.get(1));
			}

			return months;
		} else {
			return -1;
		}
	}

	public int days() {
		if (null != this.start && null != this.end) {
			int days = this.end.get(6) - this.start.get(6);
			if (this.start.get(1) != this.end.get(1)) {
				Calendar temp = (Calendar) this.start.clone();

				do {
					days += temp.getActualMaximum(6);
					temp = DateUtils.add(temp, 1, 1);
				} while (temp.get(1) != this.end.get(1));
			}

			return days;
		} else {
			return -1;
		}
	}

	public int notWeekendDays() {
		if (null != this.start && null != this.end) {
			int _start_displace = DateUtils.isWeekend(this.start).booleanValue() ? 0 : 7 - this.start.get(7);
			int _end_displace = DateUtils.isWeekend(this.end).booleanValue() ? 0 : 7 - this.end.get(7) - 1;
			Date c_start = ((Calendar) this.start.clone()).getTime();
			Date c_end = ((Calendar) this.end.clone()).getTime();
			c_start = DateUtils.nextWeek(c_start).MONDAY();
			c_end = DateUtils.nextWeek(c_end).MONDAY();
			return (new Between(c_start, c_end)).days() / 7 * 5 + _start_displace - _end_displace;
		} else {
			return -1;
		}
	}

	public int workDays() {
		int days = this.notWeekendDays();
		if (days == -1) {
			return -1;
		} else {
			days += this.countday(this.workday());
			days -= this.countday(this.restday());
			return days;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<Date> workday() {
		ArrayList workday = new ArrayList();
		if (this.holidays == null) {
			return workday;
		} else {
			Iterator arg1 = this.holidays.iterator();

			while (arg1.hasNext()) {
				Date date = (Date) arg1.next();
				if (DateUtils.isWeekend(date).booleanValue()) {
					workday.add(date);
				}
			}

			return workday;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<Date> restday() {
		ArrayList restday = new ArrayList();
		if (this.holidays == null) {
			return restday;
		} else {
			Iterator arg1 = this.holidays.iterator();

			while (arg1.hasNext()) {
				Date date = (Date) arg1.next();
				if (!DateUtils.isWeekend(date).booleanValue()) {
					restday.add(date);
				}
			}

			return restday;
		}
	}

	@SuppressWarnings("rawtypes")
	protected int countday(List<Date> days) {
		int retval = 0;
		Iterator arg2 = days.iterator();

		while (true) {
			Calendar temp;
			do {
				do {
					if (!arg2.hasNext()) {
						return retval;
					}

					Date date = (Date) arg2.next();
					temp = DateUtils.getCalendar(date);
				} while (!temp.after(this.start) && temp.compareTo(this.start) != 0);
			} while (!this.end.after(temp) && this.end.compareTo(temp) != 0);

			++retval;
		}
	}
}