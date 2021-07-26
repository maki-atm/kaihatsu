package com.example.demo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalendarController {

	@Autowired
	HttpSession session;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	CompletedRepository completedRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	Difference difference;


	//カレンダー表示
	@RequestMapping("/todo/calendar")
	public ModelAndView goCalendar(
			ModelAndView mv) {

		mv.setViewName("calendar");
		return mv;

	}

	//日付ごとのタスク表示
	@GetMapping("/todo/calendar/day")
	public ModelAndView CalendarDate(
			@RequestParam("date") String strDate,
			ModelAndView mv) {

		//ユーザ情報のセッションを受け取る
		User u =(User)session.getAttribute("userInfo");

		//Date型に変換
		strDate = strDate.replace("/","-");
		Date date = Date.valueOf(strDate);

		List<Task> td =taskRepository.findByUserCodeAndDateOrderByTimeAsc(u.getCode(),date);


		//Thymeleafで表示する準備

		mv.addObject("td", td);
		mv.addObject("date",date);
		mv.setViewName("calDate");


		return mv;

	}

	//前日のタスク表示
		@GetMapping("/todo/calendar/theDayBefore")
		public ModelAndView CalendarYesterDay(
				@RequestParam("BEFORE") String strDateB,
				ModelAndView mv) {

			//ユーザ情報のセッションを受け取る
			User u =(User)session.getAttribute("userInfo");

			//LocalDate型に変換
			strDateB = strDateB.replace("-","/");
			LocalDate ld= LocalDate.parse(strDateB, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			ld=ld.minusDays(1);

			 java.sql.Date date = java.sql.Date.valueOf(ld);

		//	LocalDate yesterday = Date.valueOf(ld);


			List<Task> td =taskRepository.findByUserCodeAndDateOrderByTimeAsc(u.getCode(),date);


			//Thymeleafで表示する準備

			mv.addObject("td", td);
			mv.addObject("date",date);
			mv.setViewName("calDate");


			return mv;

		}

		//翌日のタスク表示
		@GetMapping("/todo/calendar/nextDay")
		public ModelAndView CalendarTomorrow(
				@RequestParam("NEXT") String strDateN,
				ModelAndView mv) {

			//ユーザ情報のセッションを受け取る
			User u =(User)session.getAttribute("userInfo");

			//Date型に変換
			strDateN = strDateN.replace("-","/");
			LocalDate ld= LocalDate.parse(strDateN, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			ld=ld.plusDays(1);

			 java.sql.Date date = java.sql.Date.valueOf(ld);

		//	LocalDate yesterday = Date.valueOf(ld);


			List<Task> td =taskRepository.findByUserCodeAndDateOrderByTimeAsc(u.getCode(),date);

			//Thymeleafで表示する準備

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

		List<Task> td = null;

		User u = (User) session.getAttribute("userInfo");
		td = taskRepository.findByUserCodeAndTextLike(u.getCode(), "%" + keyword + "%");


		mv.addObject("td", td);
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
		List<Task> td = null;

		if (sort.equals("DATE")) {
			td = taskRepository.findByUserCodeAndDateOrderByTimeAsc(u.getCode(),date);
		} else if (sort.equals("PRIORITY")) {
			td = taskRepository.findByUserCodeAndDateOrderByPriNumAsc(u.getCode(),date);
		}else if(sort.equals("CATEGORY_D")) {
			td = taskRepository.findByUserCodeAndDateOrderByCategoryCodeAscDateAscTimeAsc(u.getCode(),date);
		}else if(sort.equals("CATEGORY_P")) {
			td = taskRepository.findByUserCodeAndDateOrderByCategoryCodeAscPriNumAsc(u.getCode(),date);
		}



		//Thmeleafで表示する準備
		mv.addObject("td", td);
		mv.addObject("date",date);
		mv.addObject("sort", sort);

		//フォワード
		mv.setViewName("calDate");

		return mv;

	}

	//タスク一覧から削除
		@PostMapping("/calendar/delete")
		public ModelAndView addTask(
				ModelAndView mv,
				@RequestParam("code") int code,
				@RequestParam("date") String date) {
			//指定したコードのユーザ情報を削除
			Optional<Task> record = taskRepository.findById(code);

			if (!record.isEmpty()) {
				taskRepository.deleteById(code);
			}

			return CalendarDate(date,mv);

		}


	//実行済みタスクへ移動
		@PostMapping("/calendar/{task.code}/completed")
		public ModelAndView compTask(
				ModelAndView mv,
				@PathVariable(name = "task.code") int code) {
			Task t = null;

			//指定したコードのタスク情報を取得
			Optional<Task> recode = taskRepository.findById(code);

			if (!recode.isEmpty()) {
				t = recode.get();
			}

			Completed comp = new Completed(t.getText(), t.getDate(), t.getTime(), t.getPlace(), t.getPriority(),
					t.getRemarks(), t.getColor(), t.getPriNum(), t.getUserCode(),t.getCategoryCode());

			completedRepository.saveAndFlush(comp);

			//指定したコードのユーザ情報を削除
			Optional<Task> record = taskRepository.findById(code);

			if (!record.isEmpty()) {
				taskRepository.deleteById(code);
			}

			String date=t.getDate().toString();

			return CalendarDate(date,mv);

		}
}

