package com.example.demo.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.dto.UserSearchRequest;
import com.example.demo.entity.User;

/**
 * ユーザー情報 Mapper
 */
@Mapper
public interface UserMapper {
    /**
     * ユーザー情報検索
     * @param user 検索用リクエストデータ
     * @return ユーザー情報
     */
	@Select("SELECT * FROM user WHERE id = #{id}")
    User search(UserSearchRequest user);
}