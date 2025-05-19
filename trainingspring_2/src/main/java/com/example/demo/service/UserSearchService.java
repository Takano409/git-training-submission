package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.BaseRepository;

/**
 * ユーザー情報 Service
 */
@Service
public class UserSearchService {
    /**
     * ユーザー情報 Mapper
     */
    @Autowired
    private BaseRepository baseRepository;

    /**
     * ユーザー情報検索
　　　* @param userSearchRequest リクエストデータ
     * @return 検索結果
     */
    public User search(Long id) {
    	return baseRepository.findById(id).orElse(null);
    }
}