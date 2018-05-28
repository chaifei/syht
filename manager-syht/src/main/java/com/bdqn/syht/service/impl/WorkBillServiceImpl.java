package com.bdqn.syht.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.WorkBillRepository;
import com.bdqn.syht.pojo.delivery.WorkBill;
import com.bdqn.syht.service.WorkBillService;

@Service
@Transactional
public class WorkBillServiceImpl implements WorkBillService{

	@Autowired
	private WorkBillRepository workBillRepository;
	
	@Override
	public Page<WorkBill> findPageData(Pageable pageable) {
		return workBillRepository.findAll(pageable);
	}

	@Override
	public WorkBill findByWorkBillId(int workBillId) {
		return workBillRepository.findOne(workBillId);
	}

	@Override
	public int save(WorkBill workBill) {
		return workBillRepository.save(workBill).getId();
	}

}
