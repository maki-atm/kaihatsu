package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByCategoryNameAndUserCode(String categoryName,int userCode);
	List<Category> findByUserCode(int userCode);
	List<Category> deleteByCategoryCode(int userCode);
	List<Category> deleteByUserCode(int userCode);
	List<Category> findByCategoryCode(int categoryCode);
	List<Category>findByCategoryCodeOrderByDateAscTimeAsc (int categoryCode);
	List<Category> findByCategoryCodeOrderByPriNumAsc(int categoryCode);




}
