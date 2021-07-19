package com.example.demo;


import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	List<Task> findByOrderByDateAscTimeAsc();
	List<Task> findByOrderByPriNumAsc();
	List<Task> findByUserCode(Integer code);
	List<Task> findByUserCodeAndTextLike(Integer userCode,String text);
	List<Task> findByUserCodeAndDate(Integer userCode,Date date);
	List<Task> findByUserCodeAndDateAndCategoryCode(Integer userCode,Date date,Integer categoryCode);
	List<Task> findByUserCodeOrderByDateAscTimeAsc(Integer code);
	List<Task> findByUserCodeOrderByPriNumAsc(Integer code);
	List<Task> findByCategoryCode(Integer categoryCode);
	List<Task> deleteByUserCode(Integer code);
	List<Task> deleteByCategoryCode(Integer code);
	List<Task> findByUserCodeOrderByCategoryCodeAscDateAscTimeAsc(Integer code);
	List<Task> findByUserCodeOrderByCategoryCodeAscPriNumAsc(Integer code);
	List<Task> findByCategoryCodeOrderByDateAscTimeAsc(Integer categoryCode);
	List<Task> findByCategoryCodeOrderByPriNumAsc(Integer categoryCode);

	//CalendaerController
	List<Task> findByUserCodeAndDateOrderByTimeAsc(Integer userCode,Date date);
	List<Task> findByUserCodeAndDateOrderByPriNumAsc(Integer userCode,Date date);
	List<Task> findByUserCodeAndDateAndTextLike(Integer userCode,Date date,String text);
	List<Task> findByUserCodeAndDateOrderByCategoryCodeAscDateAscTimeAsc(Integer code,Date date);
	List<Task> findByUserCodeAndDateOrderByCategoryCodeAscPriNumAsc(Integer code,Date date);


}
