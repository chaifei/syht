package com.bdqn.syht.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.pojo.delivery.WorkBill;

public interface WorkBillService {
	//返回分页数据
	public Page<WorkBill> findPageData(Pageable pageable);
	//根据id获得工单对象
	public WorkBill findByWorkBillId(int workBillId);
	//保存工单
	public int save(WorkBill workBill);
}
