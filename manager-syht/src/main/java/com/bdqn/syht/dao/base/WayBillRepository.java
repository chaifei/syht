package com.bdqn.syht.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bdqn.syht.pojo.delivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill,Integer>{
	
	public WayBill findByWayBillNum(String wayBillNum);
}
