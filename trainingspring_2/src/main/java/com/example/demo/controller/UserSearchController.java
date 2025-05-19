package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.service.UserSearchService;


/**
 * ユーザー検索　Controller
 */
@Controller
public class UserSearchController{
	/**
	 * ユーザー情報　Service
	 */
	
	@Autowired
	UserSearchService userSearchService;
	
	/**
	 * ユーザー情報検索画面を表示
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@GetMapping(value = "/user/search")
	public String displaySearch() {
		return "/user/search";
	}
	
	/**
	 * ユーザー情報検索
	 * @param userSearchRequest
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@PostMapping(value = "/user/id_search")
	public String search(@RequestParam("id") Long id, Model model) {
		User user = userSearchService.search(id);
		model.addAttribute("userinfo", user);
		return "user/search";
	}
}