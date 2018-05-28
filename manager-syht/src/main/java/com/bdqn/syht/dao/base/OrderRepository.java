package com.bdqn.syht.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bdqn.syht.pojo.delivery.Order;

public interface OrderRepository extends JpaRepository<Order,Integer>{
	
	public Order findByOrderNum(String orderNum);
}
