package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedRepository extends JpaRepository<Completed, Integer> {
	List<Completed> findByOrderByDateAscTimeAsc();
	List<Completed> findByOrderByPriNumAsc();
	List<Completed> findByUserCodeOrderByDateAscTimeAsc(Integer code);
	List<Completed> findByUserCodeOrderByPriNumAsc(Integer code);
	List<Completed> findByUserCode(Integer code);
	List<Completed> deleteByUserCode(Integer code);

	}
