package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
	HttpSession session;

	@RequestMapping("/category")
	public ModelAndView Cate(ModelAndView mv) {

	//	User u =(User)session.getAttribute("userInfo");

		//Itemテーブルからすべてのレコードを取得
				List<Category> cate=categoryRepository.findAll();

				//Thymeleafで表示する準備
				mv.addObject("cate", cate);

		//addCategory.htmlにフォワード
		mv.setViewName("category");
		return mv;

	}


	//カテゴリー登録画面表示
	@RequestMapping("/todo/category")
	public ModelAndView addCate(ModelAndView mv) {
		//addCategory.htmlにフォワード
		mv.setViewName("addCategory");
		return mv;

	}

	//カテゴリー登録
		@PostMapping("/todo/category")
		public ModelAndView addCate(
				ModelAndView mv,
				@RequestParam("category")String categoryName) {



			Category c=new Category(categoryName);

			List<Category> cate=categoryRepository.findByCategoryName(categoryName);

			 if (!(cate.size()==0) ){
					cate = new ArrayList<Category>();
					mv.addObject("msg", "入力されたカテゴリー名は既に登録されています");
					mv.setViewName("addCategory");
				}else {

                        categoryRepository.saveAndFlush(c);
						mv.addObject("msg", "登録が完了しました");

						mv.setViewName("category");//フォワード先
				}






			return mv;

		}

		//カテゴリー別の一覧表示
			@PostMapping("/category/list")
			public ModelAndView List(
					ModelAndView mv,
					@RequestParam("code")int categoryCode
					) {


				List<Task>t=taskRepository.findByCategoryCode(categoryCode);

				List<Category> cate=categoryRepository.findAll();

				//Thymeleafで表示する準備
				mv.addObject("cate", cate);

				mv.addObject("categoryCode", categoryCode);





				mv.addObject("t",t);

				mv.setViewName("cateList");

				return mv;

			}



}
