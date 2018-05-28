package com.bdqn.syht.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.TakeTimeRepository;
import com.bdqn.syht.pojo.base.TakeTime;
import com.bdqn.syht.service.TakeTimeService;

@Service//业务逻辑层
@Transactional//事务
public class TakeTimeServiceImpl implements TakeTimeService{
	//收派时间管理dao层注入
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public Page<TakeTime> findPageData(Pageable pageable) {
		
		return takeTimeRepository.findAll(pageable);
	}

	@Override
	public void saveTakeTime(TakeTime takeTime) {
		takeTimeRepository.save(takeTime);
	}

	@Override
	public void delBatch(String[] idArray) {
		//循环调用删除方法
		for(String idStr:idArray){
			//String类型转换为Integer类型
			Integer id = Integer.parseInt(idStr);
			//返回Repository层的删除方法
			takeTimeRepository.delete(id);
		}

	}

	@Override
	public List<TakeTime> findAll() {
		return takeTimeRepository.findAll();
	}

}
