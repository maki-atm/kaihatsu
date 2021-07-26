package com.example.demo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController {

	@Autowired
	HttpSession session;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	CompletedRepository completedRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	Difference difference;

	@RequestMapping("/")
	public String login() {
		// セッション情報はクリアする
		session.invalidate();
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView doLogin(
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			ModelAndView mv) {

		//メアドから顧客データを検索して取得
		List<User> list = null;
		list = userRepository.findByEmail(email);

		//アドレスが一致しないとき
		if (list == null || list.size() == 0) {
			mv.addObject("message", "入力されたメールアドレスもしくはパスワードは正しくありません");
			mv.setViewName("login");
			return mv;
		}

		//アドレスが一致するとき
		//顧客情報のリストを取得
		User userInfo = list.get(0);

		if (!(userInfo.getPassword().equals(password))) {

			mv.addObject("message", "入力されたメールアドレスもしくはパスワードは正しくありません");
			mv.setViewName("login");
			return mv;

		} else {
			//セッションスコープに顧客情報とカテゴリ情報を格納する
			session.setAttribute("userInfo", userInfo);

			User u = (User) session.getAttribute("userInfo");
			List<Task> t = taskRepository.findByUserCodeOrderByDateAscTimeAsc(u.getCode());

			//今日の日付取得
			long miliseconds = System.currentTimeMillis();
			Date today = new Date(miliseconds);

			//今日のタスクの件数取得
			List<Task> d = taskRepository.findByUserCodeAndDate(u.getCode(), today);
			int countTask = d.size();

			//残り日数のリスト取得
			ArrayList<Difference> dlist = difference.getDifDay(t);

			List<Category> cate = categoryRepository.findByUserCode(u.getCode());

			//Mapの宣言
			Map<Integer, String> map = new HashMap<>();

			for (Category c : cate) {
				// MapにListのキーと値を追加
				map.put(c.getCategoryCode(), c.getCategoryName());
			}

			session.setAttribute("categoryMap", map);

			//Thymeleafで表示する準備
			mv.addObject("cate", cate);

			mv.addObject("list", dlist);

			//Thmeleafで表示する準備
			mv.addObject("countTask", countTask);
			mv.addObject("t", t);

			//index.htmlにフォワード
			mv.setViewName("index");

			return mv;

		}

	}

	@RequestMapping("/logout")
	public String logout() {
		// ログイン画面表示処理を実行するだけ
		return login();
	}

	//新規登録画面表示
	@RequestMapping("/newUser")
	public ModelAndView newUser(ModelAndView mv) {

		mv.setViewName("newUser");
		return mv;
	}

	//新規登録
	@PostMapping("/newUser")
	public ModelAndView newUser(
			ModelAndView mv,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password) {

		User u = new User(name, email, password);
		List<User> user = userRepository.findByEmail(u.getEmail());

		if (email != null) {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			if (regex.matcher(email).find()) {

				if (!(user.size() == 0)) {
					user = new ArrayList<User>();
					mv.addObject("msg", "既に登録済みです");
					mv.setViewName("newUser");

					return mv;

				} else {
					userRepository.saveAndFlush(u);
					Category c = new Category("（なし）", u.getCode());
					categoryRepository.saveAndFlush(c);

					mv.addObject("msg", "登録が完了しました");

					mv.setViewName("login");//フォワード先
					return mv;
				}

			} else {
				mv.addObject("msg", "メールアドレスを入力してください");
				mv.setViewName("/newUser");
			}
		}
		return mv;
	}

	//ユーザ情報表示画面遷移
	@GetMapping("/userInfo")
	public ModelAndView userInfo(
			ModelAndView mv) {

		User u = (User) session.getAttribute("userInfo");

		mv.setViewName("userInfo");//フォワード先

		return mv;
	}

	//ユーザ情報変更画面遷移
	@PostMapping("/editUser")
	public ModelAndView editUser(
			ModelAndView mv) {

		User u = (User) session.getAttribute("userInfo");

		mv.setViewName("editUser");//フォワード先

		return mv;
	}

	//ユーザ情報変更
	@PostMapping("/edit")
	public ModelAndView edit(
			@RequestParam("NAME") String name,
			@RequestParam("MAIL") String mail,
			@RequestParam("PASS") String password,
			ModelAndView mv) {

		User u = (User) session.getAttribute("userInfo");

		User user = new User(u.getCode(), name, mail,password);
		userRepository.saveAndFlush(user);

		//メアドから顧客データを検索して取得
		List<User> list = null;
		list = userRepository.findByEmail(mail);

		User userInfo = list.get(0);

		//セッションスコープに顧客情報とカテゴリ情報を格納する
		session.setAttribute("userInfo", userInfo);

		mv.addObject("msg", "変更が完了しました");
		mv.setViewName("userInfo");//フォワード先

		return userInfo(mv);
	}

	//パスワード再入力遷移
	@GetMapping("/check")
	public ModelAndView check(
			ModelAndView mv) {

		//	User u = (User) session.getAttribute("userInfo");

		mv.setViewName("check");//フォワード先

		return mv;
	}

	//パスワード再入力
	@PostMapping("/check")
	public ModelAndView check(
			@RequestParam("password") String password,
			ModelAndView mv) {

		User u = (User) session.getAttribute("userInfo");


		//顧客情報のリストを取得
		List<User> list = null;


		list = userRepository.findByEmail(u.getEmail());

		User userInfo = list.get(0);

		if (!(userInfo.getPassword().equals(password))) {

			mv.addObject("message", "入力されたパスワードは正しくありません");
			mv.setViewName("check");
			return mv;

		} else {

			mv.setViewName("editPassword");//フォワード先

			return mv;
		}
	}

//	//パスワード再入力遷移
//		@GetMapping("/edit/password")
//		public ModelAndView editPassword(
//				ModelAndView mv) {
//
//			//	User u = (User) session.getAttribute("userInfo");
//
//			mv.setViewName("editPassword");//フォワード先
//
//			return mv;
//		}
//
		//パスワード変更
				@PostMapping("/edit/password")
				public ModelAndView editPassword(
						@RequestParam("NAME") String name,
						@RequestParam("MAIL") String mail,
						@RequestParam("PASS") String password,
						ModelAndView mv) {

					//	User u = (User) session.getAttribute("userInfo");

					User u = (User) session.getAttribute("userInfo");

					User user = new User(u.getCode(), name, mail,password);
					userRepository.saveAndFlush(user);

					//メアドから顧客データを検索して取得
					List<User> list = null;
					list = userRepository.findByEmail(mail);

					User userInfo = list.get(0);

					//セッションスコープに顧客情報とカテゴリ情報を格納する
					session.setAttribute("userInfo", userInfo);

					mv.addObject("msg", "パスワードの変更が完了しました");

					mv.setViewName("userInfo");//フォワード先

					return mv;
				}



	//ユーザー削除画面遷移
	@GetMapping("/deleteUser")
	public ModelAndView godeleteUser(
			ModelAndView mv) {

		User u = (User) session.getAttribute("userInfo");

		mv.setViewName("deleteUser");//フォワード先

		return mv;
	}

	//ユーザー削除
	@Transactional
	@PostMapping("/deleteUser")
	public ModelAndView deleteUser(
			ModelAndView mv) {

		User u = (User) session.getAttribute("userInfo");

		//ユーザーが作成したタスク、実行済みタスク、カテゴリー削除
		List<Task> listT = taskRepository.findByUserCode(u.getCode());
		List<Completed> listC = completedRepository.findByUserCode(u.getCode());
		List<Category> listCA = categoryRepository.findByUserCode(u.getCode());

		if (!(listT.size() == 0)) {
			taskRepository.deleteByUserCode(u.getCode());
		}

		if (!(listC.size() == 0)) {
			completedRepository.deleteByUserCode(u.getCode());
		}

		if (!(listCA.size() == 0)) {
			categoryRepository.deleteByUserCode(u.getCode());
		}

		//ユーザー情報削除
		userRepository.deleteById(u.getCode());

		mv.setViewName("deleteUserFin");//フォワード先

		return mv;
	}
}
