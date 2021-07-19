package com.example.demo;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalenderController {

	@Autowired
	HttpSession session;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	Difference difference;


	//カレンダー表示
	@RequestMapping("/todo/calender")
	public ModelAndView goCalendar(
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

		List<Task> td =taskRepository.findByUserCodeAndDateOrderByTimeAsc(u.getCode(),date);

		//登録されているカテゴリー取得
		List<Category> cate=categoryRepository.findAll();


		//Thymeleafで表示する準備
		mv.addObject("cate", cate);
		mv.addObject("td", td);
		mv.addObject("date",date);
		mv.setViewName("calDate");


		return mv;

	}

	//検索
	@PostMapping("/todo/calendar/search")
	public ModelAndView search(
			ModelAndView mv,
			@RequestParam("keyword") String keyword,
			@RequestParam("date") Date date) {

		List<Task> task = null;

		User u = (User) session.getAttribute("userInfo");
		task = taskRepository.findByUserCodeAndTextLike(u.getCode(), "%" + keyword + "%");

		//登録されているカテゴリー取得
		List<Category> cate=categoryRepository.findAll();


		mv.addObject("cate", cate);
		mv.addObject("t", task);
		mv.addObject("keyword", keyword);
		mv.addObject("date",date);

		//フォワード
		mv.setViewName("calDate");

		return mv;
	}

		//並べ替え
	@PostMapping("/todo/calendar")
	public ModelAndView sortTask(ModelAndView mv,
			@RequestParam("sort") String sort,
			@RequestParam("date") Date date) {

		User u = (User) session.getAttribute("userInfo");
		List<Task> t = null;

		if (sort.equals("DATE")) {
			t = taskRepository.findByUserCodeAndDateOrderByTimeAsc(u.getCode(),date);
		} else if (sort.equals("PRIORITY")) {
			t = taskRepository.findByUserCodeOrderByPriNumAsc(u.getCode());
		}

		//登録されているカテゴリー取得
		List<Category> cate=categoryRepository.findAll();

		//Thmeleafで表示する準備
		mv.addObject("cate", cate);
		mv.addObject("t", t);
		mv.addObject("date",date);

		//フォワード
		mv.setViewName("calDate");

		return mv;

	}

}

