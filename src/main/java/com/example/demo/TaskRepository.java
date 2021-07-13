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
	List<Task> findByUserCodeOrderByDateAscTimeAsc(Integer code);
	List<Task> findByUserCodeOrderByPriNumAsc(Integer code);

}
