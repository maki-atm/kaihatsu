package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

	@RequestMapping("/")
	public String login() {
		// セッション情報はクリアする
		session.invalidate();
		return "login";
	}

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView doLogin(
				@RequestParam("email") String email,
				@RequestParam("password") String password,
				ModelAndView mv
	) {

		//メアドから顧客データを検索して取得
			 List<User> list =null;
			 list = userRepository.findByEmail(email);

		//アドレスが一致しないとき
			 if(list == null || list.size()==0) {
				mv.addObject("message", "入力されたメールアドレスは登録されていません");
				mv.setViewName("login");
				return mv;
			 }

		//アドレスが一致するとき
			//顧客情報のリストを取得
			 User userInfo = list.get(0);

			 if( !(userInfo.getPassword()== password)) {

				 mv.addObject("message", "パスワードが一致しません");
				 mv.setViewName("login");
					 return mv;

			 }else {
				//セッションスコープに顧客情報とカテゴリ情報を格納する
				session.setAttribute("userInfo", userInfo);
				mv.setViewName("index");
				return mv;

			 }




//			session.setAttribute("categories", categoryRepository.findAll());

	}

	 @RequestMapping("/logout")
		public String logout() {
			// ログイン画面表示処理を実行するだけ
			return login();
		}

}
