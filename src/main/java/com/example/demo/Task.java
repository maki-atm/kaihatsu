package com.example.demo;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="task")
public class Task {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer code;

	private String text;

	private Date date;

	private Time time;

	private String place;

	private String priority;

	private String color;


	@Column(name="pri_num")
	private int priNum;

	@Column(name="user_code")
	private int userCode;


	public Task() {

	}

 public Task( String text, Date date, Time time, String place, String priority, String color,
			int priNum, int userCode) {


		this.text = text;
		this.date = date;
		this.time = time;
		this.place = place;
		this.priority = priority;
		this.color = color;
		this.priNum = priNum;
		this.userCode = userCode;

	}

	public Task(Integer code, String text, Date date, Time time, String place, String priority , String color,int priNum,int userCode) {
		this.code = code;
		this.text = text;
		this.date = date;
		this.time = time;
		this.place = place;
		this.priority = priority;
		this.color = color;
		this.priNum = priNum;
		this.userCode = userCode;

	}

	public Integer getCode() {
		return code;
	}

	public String getText() {
		return text;
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

	public int getPriNum() {
		return priNum;
	}

	public void setPriNum(int priNum) {
		this.priNum= priNum;
	}

	public int getUserCode() {
		return userCode;
	}

	public void setUserCode(int userCode) {
		this.userCode= userCode;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}






}