package com.example.demo;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToDoController {
	@Autowired
	TaskRepository taskRepository;

	@Autowired
	CompletedRepository completedRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	@Autowired
	Difference difference;

	//タスク情報一覧表示
	@RequestMapping("/todo")
	public ModelAndView displayTask(ModelAndView mv) {

		User u = (User) session.getAttribute("userInfo");
		List<Task> t = taskRepository.findByUserCodeOrderByDateAscTimeAsc(u.getCode());

		//今日の日付取得
		long miliseconds = System.currentTimeMillis();
		Date today = new Date(miliseconds);

		//今日のタスク件数取得
		List<Task> d = taskRepository.findByUserCodeAndDate(u.getCode(), today);
		int countTask = d.size();

		//残り日数のリスト取得
		ArrayList<Difference> list = difference.getDifDay(t);


		mv.addObject("list", list);

		//Thmeleafで表示する準備
		mv.addObject("countTask", countTask);
		mv.addObject("t", t);

		//index.htmlにフォワード
		mv.setViewName("index");

		return mv;

	}

	//検索
	@PostMapping("/search")
	public ModelAndView search(
			ModelAndView mv,
			@RequestParam("keyword") String keyword) {
		List<Task> task = null;

		User u = (User) session.getAttribute("userInfo");
		task = taskRepository.findByUserCodeAndTextLike(u.getCode(), "%" + keyword + "%");

		//今日の日付取得
		long miliseconds = System.currentTimeMillis();
		Date today = new Date(miliseconds);

		//今日のタスク件数取得
		List<Task> d = taskRepository.findByUserCodeAndDate(u.getCode(), today);
		int countTask = d.size();

		//残り日数のリスト取得
		ArrayList<Difference> list=difference.getDifDay(task);

		mv.addObject("list", list);
		mv.addObject("countTask", countTask);
		mv.addObject("t", task);
		mv.addObject("keyword", keyword);

		mv.setViewName("index");

		return mv;
	}

	//並べ替え
	@PostMapping("/todo")
	public ModelAndView sortTask(ModelAndView mv,
			@RequestParam("sort") String sort) {

		User u = (User) session.getAttribute("userInfo");
		List<Task> t = null;

		if (sort.equals("DATE")) {
			t = taskRepository.findByUserCodeOrderByDateAscTimeAsc(u.getCode());
		} else if (sort.equals("PRIORITY")) {
			t = taskRepository.findByUserCodeOrderByPriNumAsc(u.getCode());
		}

		//今日の日付取得
		long miliseconds = System.currentTimeMillis();
		Date today = new Date(miliseconds);

		//今日のタスク件数取得
		List<Task> d = taskRepository.findByUserCodeAndDate(u.getCode(), today);
		int countTask = d.size();

		//残り日数のリスト取得
		ArrayList<Difference> list=difference.getDifDay(t);

		//Thmeleafで表示する準備
		mv.addObject("countTask", countTask);
		mv.addObject("t", t);
		mv.addObject("list", list);

		//index.htmlにフォワード
		mv.setViewName("index");

		return mv;

	}

	//タスク新規登録画面表示
	@RequestMapping("/todo/new")
	public ModelAndView addTask(ModelAndView mv) {
		//addTask.htmlにフォワード
		mv.setViewName("addTask");
		return mv;

	}

	//新規タスク登録
	@PostMapping("/todo/new")
	public ModelAndView addTask(
			ModelAndView mv,
			@RequestParam("text") String text,
			@RequestParam("date") String strDate,
			@RequestParam("time") String strTime,
			@RequestParam("place") String place,
			@RequestParam("priority") int priNum,
			@RequestParam("remarks") String remarks,
			@RequestParam("color") String color) {

		//優先度を数字で受け取り、対応した文字を格納
		String priority = null;

		if (priNum == 1) {
			priority = "高";
		} else if (priNum == 2) {
			priority = "中";
		} else if (priNum == 3) {
			priority = "低";
		}

		//Date型に変換
		Date date = Date.valueOf(strDate);

		//Time型に変換
		Time time = null;

		if (!strTime.equals("")) {
			time = Time.valueOf(strTime + ":00");
		} else {

		}

		//ユーザ情報のセッションを受け取る
		User u = (User) session.getAttribute("userInfo");

		//task,date,time,place,priorityを元にタスク情報を登録
		Task t = new Task(text, date, time, place, priority, remarks, color, priNum, u.getCode());

		taskRepository.saveAndFlush(t);

		return displayTask(mv);

	}

	//タスク一覧から削除
	@PostMapping("/todo/delete")
	public ModelAndView addTask(
			ModelAndView mv,
			@RequestParam("code") int code) {
		//指定したコードのユーザ情報を削除
		Optional<Task> record = taskRepository.findById(code);

		if (!record.isEmpty()) {
			taskRepository.deleteById(code);
		}

		//実行済みタスク一覧表示
		return displayTask(mv);

	}

	//タスク変更画面表示
	@RequestMapping("/todo/{task.code}/edit")
	public ModelAndView editTask(
			ModelAndView mv,
			@PathVariable("task.code") int code) {
		Task t = null;

		//指定したコードのタスク情報を取得
		Optional<Task> recode = taskRepository.findById(code);

		if (!recode.isEmpty()) {
			t = recode.get();
		}

		//Thmeleafで表示する準備
		mv.addObject("t", t);

		//editTask.htmlへフォワード
		mv.setViewName("editTask");

		return mv;

	}

	//タスク変更
	@PostMapping("/todo/{t.code}/edit")
	public ModelAndView editTask(
			ModelAndView mv,
			@PathVariable(name = "t.code") int code,
			@RequestParam("text") String text,
			@RequestParam("date") String strDate,
			@RequestParam("time") String strTime,
			@RequestParam("place") String place,
			@RequestParam("priority") int priNum,
			@RequestParam("remarks") String remarks,
			@RequestParam("color") String color) {

		//優先度を数字で受け取り、対応した文字を格納
		String priority = null;

		if (priNum == 1) {
			priority = "高";
		} else if (priNum == 2) {
			priority = "中";
		} else if (priNum == 3) {
			priority = "低";
		}

		//Date型に変換
		Date date = Date.valueOf(strDate);

		//strTimeの文字数取得
		int tm = strTime.length();

		Time time = null;
		//文字数が5（hh:㎜）のとき「:00」を追加（時間を変更したとき）
		//時間を変更していないときはそのまま（hh：㎜：ss）でOK
		if (tm == 0) {

		} else if (tm == 5) {
			time = Time.valueOf(strTime + ":00");
		}

		//Time型に変換

		//	Time time = Time.valueOf(strTime);

		//ユーザ情報のセッションを受け取る
		User u = (User) session.getAttribute("userInfo");

		//指定したコードのタスク情報を変更
		Task t = new Task(code, text, date, time, place, priority, remarks, color, priNum, u.getCode());
		taskRepository.saveAndFlush(t);

		return displayTask(mv);

	}

	//実行済みタスクへ移動
	@RequestMapping("/todo/{task.code}/completed")
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
				t.getRemarks(), t.getColor(), t.getPriNum(), t.getUserCode());

		completedRepository.saveAndFlush(comp);

		//指定したコードのユーザ情報を削除
		Optional<Task> record = taskRepository.findById(code);

		if (!record.isEmpty()) {
			taskRepository.deleteById(code);
		}

		return displayTask(mv);

	}

	//実行済みタスク一覧表示
	@RequestMapping("/todo/completed")
	public ModelAndView completedTask(
			ModelAndView mv) {

		User u = (User) session.getAttribute("userInfo");
		//全タスク情報取得
		List<Completed> t = completedRepository.findByUserCodeOrderByDateAscTimeAsc(u.getCode());

		//Thmeleafで表示する準備
		mv.addObject("t", t);

		//compTask.htmlへフォワード
		mv.setViewName("compTask");

		return mv;

	}

	//実行済みタスク一覧から未実行タスクへ戻す
	@PostMapping("/todo/{task.code}/completed2")
	public ModelAndView completedTask(
			ModelAndView mv,
			@PathVariable(name = "task.code") int code) {

		Completed t = null;

		//指定したコードのタスク情報を取得
		Optional<Completed> recode = completedRepository.findById(code);

		if (!recode.isEmpty()) {
			t = recode.get();
		}

		//戻すボタン押下時にtask,date,time,place,priorityを元にタスク情報を登録
		Task task = new Task(t.getText(), t.getDate(), t.getTime(), t.getPlace(), t.getPriority(), t.getColor(),
				t.getRemarks(), t.getPriNum(), t.getUserCode());
		taskRepository.saveAndFlush(task);

		//実行済みテーブルから指定したコードのタスク情報を削除
		//Optional<Completed> record = completedRepository.findById(code);

		if (!recode.isEmpty()) {
			completedRepository.deleteById(code);
		}

		return completedTask(mv);

	}

	//実行済みタスクから削除
	@PostMapping("/todo/completed/delete")
	public ModelAndView deleteTask(
			ModelAndView mv,
			@RequestParam("code") int code) {

		//指定したコードのユーザ情報を削除
		Optional<Completed> record = completedRepository.findById(code);

		if (!record.isEmpty()) {
			completedRepository.deleteById(code);
		}

		//実行済みタスク一覧表示
		return completedTask(mv);

	}


	//残り日数を格納する配列を返すメソッド
//	private ArrayList<Difference> getDifDay(List<Task> t) {
//
//		Difference d = new Difference();
//		//今日の日付取得
//			long miliseconds = System.currentTimeMillis();
//			Date today = new Date(miliseconds);
//
//		ArrayList<Difference> list = null;
//		long dif = 0;
//		for (Task i : t) {
//			//i.getDate();
//			LocalDate localD = i.getDate().toLocalDate();
//			LocalDate localT = today.toLocalDate();
//			d.setDif(ChronoUnit.DAYS.between(localT, localD));
//			if(dif>=0) {
//				Difference de=new Difference(d.getDif(),"日");
//
//				//String strDif= String.valueOf(dif);
//				list.add(de);
//			}else {
//
//			}
//
//
//
//
//		}
//		return list;
//	}




}
