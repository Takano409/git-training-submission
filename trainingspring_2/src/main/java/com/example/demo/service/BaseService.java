package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.BaseRepository;
/*
 * このクラスがservice層であることをspringに伝える
 * springが自動的にbeanとして管理でき、@Autowiredでほかのクラスから呼べる
 */
@Service 
/*
 * このクラスで行うDB管理をトランザクションで管理する
 * 途中でエラーが起きたら全部の処理をロールバックする
 */
@Transactional(rollbackFor = Exception.class)
public class BaseService{
	/*
	 * //baseRepositoryという変数に自動でインスタンスを注入
	 * BaseRepositoryはJPAのリポジトリインターフェースで、DBとのやり取りを担当
	 */
	@Autowired 
	private BaseRepository baseRepository;
	
	/*
	 * ユーザー情報　全検索
	 * @return 検索結果
	 * ユーザーTBLの内容を全検索
	 * User　entityのリストを返す
	 */
	public List<User> searchAll(){
		return baseRepository.findAll();
	}
	
	/*
	 * ユーザー情報検索　主キー検索
	 * @return 検索結果
	 * 引数としてユーザーIDを受け取り、「それに対するUser　entityを返す
	 * 
	 */
	public User findById(Long id) {
		// baseRepositoryが継承しているJpaRepositoryのfindById()メソッド
		//Optional<User>を返す（nullが返るかもしれない値を安全に扱うためのラッパー）
		//.get()でOptionalから値を取り出す
		return baseRepository.findById(id).get();
	}
	
	
	/**
	 * ユーザー新規登録
	 * @param user ユーザー情報
	 */
	public void create(UserRequest userRequest) {
		Date now = new Date();
		User user = new User();
		user.setName(userRequest.getName());
		user.setAddress(userRequest.getAddress());
		user.setPhone(userRequest.getPhone());
		user.setCreateDate(now);
		user.setDeleteDate(now);
		baseRepository.save(user);
	}
	
	
	
	
	
	
	
	
	
}