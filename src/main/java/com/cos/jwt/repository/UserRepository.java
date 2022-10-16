package com.cos.jwt.repository;

import com.cos.jwt.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고있.
// @Repository라는 어노테이션이 없어도 IoC가 됨.
public interface UserRepository extends JpaRepository<UserModel, Integer> {

    // JPA Query method

    // findBy규칙 => Username문법
    // select * from usermodel where username = ? => 해당 쿼리가 실행됨
    public UserModel findByUsername(String username);
}
