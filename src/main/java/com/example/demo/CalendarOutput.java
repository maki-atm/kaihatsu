package com.example.demo;
import java.time.LocalDate;
import java.util.List;

public class CalendarOutput {

	 private List<List<LocalDate>> calendar;

	    private LocalDate firstDayOfMonth;

	    private int yearOfNextMonth;

	    private int monthOfNextMonth;

	    private int yearOfPrevMonth;

	    private int monthOfPrevMonth;

	    
	    
	    
		public List<List<LocalDate>> getCalendar() {
			return calendar;
		}

		public void setCalendar(List<List<LocalDate>> calendar) {
			this.calendar = calendar;
		}

		public LocalDate getFirstDayOfMonth() {
			return firstDayOfMonth;
		}

		public void setFirstDayOfMonth(LocalDate firstDayOfMonth) {
			this.firstDayOfMonth = firstDayOfMonth;
		}

		public int getYearOfNextMonth() {
			return yearOfNextMonth;
		}

		public void setYearOfNextMonth(int yearOfNextMonth) {
			this.yearOfNextMonth = yearOfNextMonth;
		}

		public int getMonthOfNextMonth() {
			return monthOfNextMonth;
		}

		public void setMonthOfNextMonth(int monthOfNextMonth) {
			this.monthOfNextMonth = monthOfNextMonth;
		}

		public int getYearOfPrevMonth() {
			return yearOfPrevMonth;
		}

		public void setYearOfPrevMonth(int yearOfPrevMonth) {
			this.yearOfPrevMonth = yearOfPrevMonth;
		}

		public int getMonthOfPrevMonth() {
			return monthOfPrevMonth;
		}

		public void setMonthOfPrevMonth(int monthOfPrevMonth) {
			this.monthOfPrevMonth = monthOfPrevMonth;
		}
	    
	    
	   
}
