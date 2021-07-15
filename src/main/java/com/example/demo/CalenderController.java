package com.example.demo;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalenderController {

	@Autowired
	HttpSession session;

	@Autowired
	TaskRepository taskRepository;


	//カレンダー表示
	@RequestMapping("/todo/calender")
	public ModelAndView goClender(
			ModelAndView mv) {

		mv.setViewName("calender");


		return mv;

	}

	//日付ごとのタスク表示
	@GetMapping("/todo/calender/day")
	public ModelAndView ClenderDate(
			@RequestParam("date") String strDate,
			ModelAndView mv) {

		//ユーザ情報のセッションを受け取る
				User u =(User)session.getAttribute("userInfo");

		//Date型に変換
				strDate = strDate.replace("/","-");
				Date date = Date.valueOf(strDate);

				List<Task> td =taskRepository.findByUserCodeAndDate(u.getCode(),date);

		mv.addObject("td", td);
		mv.addObject("date",date);
		mv.setViewName("calDate");

		//実行済みタスク一覧表示
		return mv;

	}



}

