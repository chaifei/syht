package com.bdqn.syht.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.pojo.delivery.Order;

public interface OrderService {
	//保存订单
	@Path("/order")
	@POST
	@Consumes({"application/xml","application/json"})
	public void saveOrder(Order order);
	//分页查询订单
	public Page<Order> findPageData(Pageable pageable);
	//根据id查询订单
	public Order findByOrderId(int orderId);
	//人工调度
	public void updateOrderDispatcher(Order order);
	//生成工单 发短信给快递员
	public void generateWorkBill(final Order order);
	//根据订单号查询订单
	public Order findByOrderNum(String orderNum);
}
