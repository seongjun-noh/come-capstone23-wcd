package com.wcd.userservice.repository;

import com.wcd.userservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// CrudRepository 인터페이스를 상속함으로써, UserRepository는 UserEntity에 대한 CRUD 연산을 수행할 수 있음
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByLoginId(String loginId);
}
