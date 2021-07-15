package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="category")
public class Category {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="category_code")
	private Integer categoryCode;

	@Column(name="category_name")
	private String categoryName;

	@Column(name="user_code")
	private int userCode;


	public Category() {

	}



	public Category(Integer categoryCode, String categoryName, int userCode) {
		super();
		this.categoryCode = categoryCode;
		this.categoryName = categoryName;
		this.userCode = userCode;
	}

	public Category( String categoryName, int userCode) {
		this.categoryName = categoryName;
		this.userCode = userCode;
	}



	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}



}
