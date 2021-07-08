package com.example.demo;


import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

	//タスク情報一覧表示
	@RequestMapping("/todo")
	public ModelAndView displayTask(ModelAndView mv) {
		//全タスク情報取得
		List<Task> t = taskRepository.findAll();

		//Thmeleafで表示する準備
		mv.addObject("t", t);


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
			@RequestParam("priority") String priority) {
		//task,date,time,place,priorityを元にタスク情報を登録

		System.out.println("date=" + strDate);
		System.out.println("time=" + strTime);
		//日付の「/」を「-」に置換して、Date型に変換
		Date date = Date.valueOf(strDate);

		//Time型に変換

		Time time = Time.valueOf(strTime+":00");

		Task t = new Task(text, date, time, place, priority);
		taskRepository.saveAndFlush(t);

		return displayTask(mv);

	}


	//タスク一覧から削除
	@PostMapping("/todo/delete")
	public ModelAndView addTask(
			ModelAndView mv,
			@RequestParam("code")int code
	) {
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
			@PathParam("task.code") int code) {
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
			@PathParam("t.code") int code,
			@RequestParam("text") String text,
			@RequestParam("date") String strDate,
			@RequestParam("time") String strTime,
			@RequestParam("place") String place,
			@RequestParam("priority") String priority) {

		//日付の「/」を「-」に置換して、Date型に変換
			Date date = Date.valueOf(strDate);

		//Time型に変換
			Time time = Time.valueOf(strTime+":00");

		//指定したコードのタスク情報を変更
		Task t = new Task(code, text, date, time, place, priority);
		taskRepository.saveAndFlush(t);

		return displayTask(mv);

	}

	//実行済みタスクへ移動
	@RequestMapping("/todo/{task.code}/completed")
	public ModelAndView compTask(
			ModelAndView mv,
			@RequestParam("task.code") int code) {
		Task t = null;

		//指定したコードのタスク情報を取得
		Optional<Task> recode = taskRepository.findById(code);


		if (!recode.isEmpty()) {
			t = recode.get();
		}

		Completed comp = new Completed(t.getText(), t.getDate(), t.getTime(), t.getPlace(), t.getPriority());


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

		//全タスク情報取得
		List<Completed> t = completedRepository.findAll();

		//Thmeleafで表示する準備
		mv.addObject("t", t);

		//compTask.htmlへフォワード
		mv.setViewName("compTask");

		return mv;

	}

	//実行済みタスク一覧から未実行タスクへ戻す
	@PostMapping("/todo/completed")
	public ModelAndView completedTask(
			ModelAndView mv,
			@RequestParam("code") int code) {

		Task t = null;


		//指定したコードのタスク情報を取得
		Optional<Task> recode = taskRepository.findById(code);

		if (!recode.isEmpty()) {
			t = recode.get();
		}

		//エラー中が直らない・・・
		//戻すボタン押下時にtask,date,time,place,priorityを元にタスク情報を登録
		Task task = new Task(t.getText(), t.getDate(), t.getTime(),t.getPlace(), t.getPriority());
		taskRepository.saveAndFlush(task);


//		Completed comp = new Completed(tasK.getTask(), tasK.getDate(), tasK.getTime(),tasK.getPlace(), tasK.getPriority());

		//実行済みテーブルから指定したコードのタスク情報を削除
		Optional<Completed> record = completedRepository.findById(code);

		if (!record.isEmpty()) {
			taskRepository.deleteById(code);
		}

		return displayTask(mv);

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

}
