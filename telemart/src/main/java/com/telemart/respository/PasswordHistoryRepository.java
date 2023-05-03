package com.telemart.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.telemart.entity.PasswordHistoryEntity;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistoryEntity,Object>{

	PasswordHistoryEntity findByEmailId(String emailId); 

}
