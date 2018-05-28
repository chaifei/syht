package com.bdqn.syht.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.StandardRepository;
import com.bdqn.syht.pojo.base.Standard;
import com.bdqn.syht.service.StandardService;

/**
 * 收派标准业务层实现类
 */
@Service//业务逻辑层
@Transactional//事务
public class StandardServiceImpl implements StandardService{
	
	//收派标准dao层注入
	@Autowired
	private StandardRepository standardRepository;

	@Override
	public Page<Standard> findPageData(Pageable pageable) {
		return standardRepository.findAll(pageable);
	}

	@Override
	public void saveStandard(Standard standard) {
		standardRepository.save(standard);
	}

	@Override
	public List<Standard> findStandardList() {
		return standardRepository.findAll();
	}	

}
