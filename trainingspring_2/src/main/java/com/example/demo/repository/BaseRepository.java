package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

/*
 * springに、これがデータアクセス層のクラスだと伝える
 */
@Repository
/*
 * BaseRepositoryにJpaRepositoryを継承させる
 * findAll,findById,save,deleteByIdなどを使えるようになる
 * Userはentityクラス。テーブルと対応
 * LongはUser entityのIDの型
 */
public interface BaseRepository extends JpaRepository<User, Long>{
}

