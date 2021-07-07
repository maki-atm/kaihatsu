package com.example.demo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
			@RequestParam("date") Date date,
			@RequestParam("time") int time,
			@RequestParam("place") String place,
			@RequestParam("priority") String priority) {
		//task,date,time,place,priorityを元にタスク情報を登録
		Task tasK = new Task(task, date, place, priority);
		taskRepository.saveAndFlush(tasK);

		return displayTask(mv);

	}

	//タスク変更画面表示
	@RequestMapping("/todo/{code}/edit")
	public ModelAndView editTask(
			ModelAndView mv,
			@RequestParam("code") int code) {
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
			@RequestParam("date") Date date,
			@RequestParam("time") Time time,
			@RequestParam("place") String place,
			@RequestParam("priority") String priority) {

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

		//戻すボタン押下時にtask,date,time,place,priorityを元にタスク情報を登録
		Task tasK = new Task(tasK.getTask(), tasK.getDate(), tasK.getPlace(), tasK.getPriority());
		taskRepository.saveAndFlush(tasK);

		//指定したコードのタスク情報を取得
		Optional<Task> recode = taskRepository.findById(code);

		Completed comp = new Completed(task, date, time, place, priority);

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
