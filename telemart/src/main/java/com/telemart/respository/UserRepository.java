package com.telemart.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.telemart.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Object>{

	UserEntity findByEmailId(String emailId);

}
