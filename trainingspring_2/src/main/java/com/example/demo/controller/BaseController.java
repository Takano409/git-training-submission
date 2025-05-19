package com.example.demo.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.service.BaseService;


/*
 * ユーザー情報　controller
 */
@Controller
public class BaseController{
	
	/**
	 * ユーザー情報　service
	 * 
	 */
	@Autowired //依存性注入のアノテーション
	private BaseService baseService; // Springがインスタンス化
	
	/**
	 * ユーザー情報一覧画面を表示
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@GetMapping(value = "/user/list")
	public String displayList(Model model) {
		//全ユーザー情報を取得しリストに代入
		//Serviceクラスで定義しているメソッド
		List<User> userlist = baseService.searchAll();
		//userlistをテンプレートの/user/listに渡す
		model.addAttribute("userlist", userlist);
		return "user/list";
		
	}
	
	/**
	 * ユーザー新規登録画面を表示
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@GetMapping(value = "/user/add")
	public String displayAdd(Model model) {
		model.addAttribute("userRequest", new UserRequest());
		return "user/add";
	}
	/**
	 * ユーザー情報詳細画面を表示
	 * @param id 表示するユーザーID
	 * @param model Model
	 * return ユーザー情報詳細画面
	 */
	@GetMapping(value = "/user/{id}")
	public String displayView(@PathVariable Long id, Model model) {
		User user = baseService.findById(id);
		model.addAttribute("userData", user);
		return "user/view";
	}
	
	/**
	 * ユーザー新規登録
	 * @param userRequest リクエストデータ
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
	/*
	 * @Validated UserRequestの中のバリデーションを実行する
	 * @ModelAttribute フォームから送られた値をUserRequestに自動でマッピングする
	 * BindingResult result バリデーションの結果をここに入れる
	 */
	public String create(@Validated @ModelAttribute UserRequest userRequest, BindingResult result, Model model) {
		if(result.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for(ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
		}
		
		//baseSreviceで定義しているcreateメソッドでDBに登録
		//登録したらユーザー一覧画面にリダイレクト
		baseService.create(userRequest);
		return "redirect:/user/list";
	}
	
	
}
