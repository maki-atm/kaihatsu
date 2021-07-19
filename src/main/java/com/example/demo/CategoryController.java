package com.example.demo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CategoryController {
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	CompletedRepository completedRepository;

	@Autowired
	HttpSession session;

	@Autowired
	Difference difference;

	//カテゴリー一覧表示
	@RequestMapping("/category")
	public ModelAndView Cate(
			ModelAndView mv) {

		User u =(User)session.getAttribute("userInfo");

		//カテゴリーテーブルからすべてのレコードを取得
		List<Category> cate=categoryRepository.findByUserCode(u.getCode());


				session.getAttribute("userInfo");

		//Thymeleafで表示する準備
		mv.addObject("cate", cate);

		//addCategory.htmlにフォワード
		mv.setViewName("category");
		return mv;

	}


	//カテゴリー登録画面表示
	@RequestMapping("/todo/category")
	public ModelAndView addCate(ModelAndView mv) {

		User u = (User) session.getAttribute("userInfo");

		//addCategory.htmlにフォワード
		mv.setViewName("addCategory");
		return mv;

	}

	//カテゴリー登録
		@PostMapping("/todo/category")
		public ModelAndView addCate(
				ModelAndView mv,
				@RequestParam("category")String categoryName
) {

			User u = (User) session.getAttribute("userInfo");

			Category c=new Category(categoryName,u.getCode());

			List<Category> cate=categoryRepository.findByCategoryNameAndUserCode(categoryName,u.getCode());


			 if (!(cate.size()==0) ){
					cate = new ArrayList<Category>();
					mv.addObject("msg", "入力されたカテゴリー名は既に登録されています");
					mv.setViewName("addCategory");
				}else {

                        categoryRepository.saveAndFlush(c);
                        List<Category> cate2=categoryRepository.findByUserCode(u.getCode());
                        Map<Integer, String> map = new HashMap<>();

                        for(Category cg : cate2) {
        		            // MapにListのキーと値を追加
        		            map.put(cg.getCategoryCode(), cg.getCategoryName());
        		        }

                        session.setAttribute("categoryMap", map);
						mv.addObject("msg", "登録が完了しました");

						return Cate(mv);
				}






			return mv;

		}

		//カテゴリー別の一覧表示
			@RequestMapping("/category/list")
			public ModelAndView List(
					ModelAndView mv,
					@RequestParam("code")int categoryCode
					) {


				List<Task>t=taskRepository.findByCategoryCode(categoryCode);

				List<Category> cate=categoryRepository.findAll();

				User u = (User) session.getAttribute("userInfo");

				//今日の日付取得
				long miliseconds = System.currentTimeMillis();
				Date today = new Date(miliseconds);

				//今日のタスク件数取得
				List<Task> d = taskRepository.findByUserCodeAndDate(u.getCode(), today);
				int countTask = d.size();

				//残り日数のリスト取得
				ArrayList<Difference> list = difference.getDifDay(t);

				//Thymeleafで表示する準備
				mv.addObject("list", list);
				mv.addObject("countTask", countTask);
				mv.addObject("cate", cate);

				mv.addObject("categoryCode", categoryCode);

				mv.addObject("t",t);

				mv.setViewName("cateList");

				return mv;

			}

			//カテゴリー別の一覧表示並べ替え
			@PostMapping("/category/list")
			public ModelAndView List(
					ModelAndView mv,
					@RequestParam("code")int categoryCode,
					@RequestParam("sort") String sort
					) {


				User u = (User) session.getAttribute("userInfo");
				List<Task> t =null;

				if (sort.equals("DATE")) {
					t = taskRepository.findByCategoryCodeOrderByDateAscTimeAsc(categoryCode);
				} else if (sort.equals("PRIORITY")) {
					t= taskRepository.findByCategoryCodeOrderByPriNumAsc(categoryCode);
				}





				//今日の日付取得
				long miliseconds = System.currentTimeMillis();
				Date today = new Date(miliseconds);

				//今日のタスク件数取得
				List<Task> d = taskRepository.findByUserCodeAndDate(u.getCode(), today);
				int countTask = d.size();

				//残り日数のリスト取得
				ArrayList<Difference> list = difference.getDifDay(t);

				//Thymeleafで表示する準備
			mv.addObject("list", list);
				mv.addObject("countTask", countTask);

				mv.addObject("categoryCode", categoryCode);

				mv.addObject("t",t);

				mv.setViewName("cateList");

				return mv;

			}

			//カテゴリーの削除
			@RequestMapping("/category/delete")
			@Transactional
			public ModelAndView Delete(
					ModelAndView mv,
					@RequestParam("categoryCode")int categoryCode) {

				List<Task>record=taskRepository.findByCategoryCode(categoryCode);
				List<Completed>rec=completedRepository.findByCategoryCode(categoryCode);
				List<Category>r=categoryRepository.findByCategoryCode(categoryCode);

				if (!record.isEmpty() ) {
					taskRepository.deleteByCategoryCode(categoryCode);
				}

				if(! rec.isEmpty()) {
					completedRepository.deleteByCategoryCode(categoryCode);
				}

				if(! r.isEmpty()) {
					categoryRepository.deleteById(categoryCode);
				}





				return Cate(mv);
			}



}
