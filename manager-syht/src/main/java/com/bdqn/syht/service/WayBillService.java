package com.bdqn.syht.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.pojo.delivery.WayBill;


public interface WayBillService {
	//分页查询
	public Page<WayBill> findPageData(WayBill wayBill,Pageable pageable);
	
	//保存
	public int save(WayBill wayBill);
	
	//根据id查询订单
	public WayBill findByWayBillId(int wayBillId);
	
	//根据订单号查询订单
	public WayBill findByWayBillNum(String wayBillNum);
	
	public void syncIndex();
	
	//PDF导出
	public List<WayBill> findWayBills(WayBill waybill);
	
	public Page<WayBill> findPageDatas(Pageable pageable);
}
