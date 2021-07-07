package com.example.demo;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="completed")
public class Completed {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer code;

	private String task;

	private Date date;

	private Time time;

	private String place;

	private String priority;


	public Completed() {

	}

	public Completed( String task, Date date, Time time, String place, String priority) {
		this.task = task;
		this.date = date;
		this.time = time;
		this.place = place;
		this.priority = priority;
	}

	public Completed(Integer code, String task, Date date, Time time, String place, String priority) {
		this.code = code;
		this.task = task;
		this.date = date;
		this.time = time;
		this.place = place;
		this.priority = priority;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}



}