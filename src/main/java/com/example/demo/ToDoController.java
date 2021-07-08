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
		List<Task> tasK = taskRepository.findAll();

		//Thmeleafで表示する準備
		mv.addObject("task", tasK);
		

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
			@RequestParam("task") String task,
			@RequestParam("date") String strDate,
			@RequestParam("time") String strTime,
			@RequestParam("place") String place,
			@RequestParam("priority") String priority) {
		//task,date,time,place,priorityを元にタスク情報を登録

		//日付の「/」を「-」に置換して、Date型に変換
		Date date = Date.valueOf(strDate.replace("/", "-"));

		//Time型に変換
		Time time = Time.valueOf(strTime);

		Task tasK = new Task(task, date, time, place, priority);
		taskRepository.saveAndFlush(tasK);

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
			completedRepository.deleteById(code);
		}

		//実行済みタスク一覧表示
		return displayTask(mv);

	}

	//タスク変更画面表示
	@RequestMapping("/todo/{code}/edit")
	public ModelAndView editTask(
			ModelAndView mv,
			@PathParam("code") int code) {
		Task tasK = null;

		//指定したコードのタスク情報を取得
		Optional<Task> recode = taskRepository.findById(code);

		if (!recode.isEmpty()) {
			tasK = recode.get();
		}

		//Thmeleafで表示する準備
		mv.addObject("task", tasK);

		//editTask.htmlへフォワード
		mv.setViewName("editTask");

		return mv;

	}

	//タスク変更
	@PostMapping("/todo/{code}/edit")
	public ModelAndView editTask(
			ModelAndView mv,
			@RequestParam("code") int code,
			@RequestParam("task") String task,
			@RequestParam("date") String strDate,
			@RequestParam("time") String strTime,
			@RequestParam("place") String place,
			@RequestParam("priority") String priority) {

		//日付の「/」を「-」に置換して、Date型に変換
			Date date = Date.valueOf(strDate.replace("/", "-"));

		//Time型に変換
			Time time = Time.valueOf(strTime);

		//指定したコードのタスク情報を変更
		Task tasK = new Task(code, task, date, time, place, priority);
		taskRepository.saveAndFlush(tasK);

		return displayTask(mv);

	}

	//実行済みタスクへ移動
	@RequestMapping("/todo/{code}/completed")
	public ModelAndView compTask(
			ModelAndView mv,
			@RequestParam("code") int code) {
		Task tasK = null;

		//指定したコードのタスク情報を取得
		Optional<Task> recode = taskRepository.findById(code);


		if (!recode.isEmpty()) {
			tasK = recode.get();
		}

		Completed comp = new Completed(tasK.getTask(), tasK.getDate(), tasK.getTime(), tasK.getPlace(), tasK.getPriority());


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

		//compTask.htmlへフォワード
		mv.setViewName("compTask");

		return mv;

	}

	//実行済みタスク一覧表示
	@PostMapping("/todo/completed")
	public ModelAndView completedTask(
			ModelAndView mv,
			@RequestParam("code") int code) {

		Task task = null;


//指定したコードのタスク情報を取得
		Optional<Task> recode = taskRepository.findById(code);

		if (!recode.isEmpty()) {
			task = recode.get();
		}

		//エラー中が直らない・・・
		//戻すボタン押下時にtask,date,time,place,priorityを元にタスク情報を登録
		Task tasK = new Task(task.getTask(), task.getDate(), task.getTime(),task.getPlace(), task.getPriority());
		taskRepository.saveAndFlush(tasK);


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
