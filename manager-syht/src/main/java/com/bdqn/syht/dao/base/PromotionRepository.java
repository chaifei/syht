package com.bdqn.syht.dao.base;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bdqn.syht.pojo.delivery.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer>{
	
	@Query("update Promotion set status='2' where endDate<? and status='1'")
	@Modifying
	public void updateStatus(Date date);
}
