package com.bdqn.syht.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.CourierRepository;
import com.bdqn.syht.dao.base.FixedAreaRepository;
import com.bdqn.syht.dao.base.TakeTimeRepository;
import com.bdqn.syht.pojo.base.Courier;
import com.bdqn.syht.pojo.base.FixedArea;
import com.bdqn.syht.pojo.base.TakeTime;
import com.bdqn.syht.service.FixedAreaService;
/**
 * 定区业务逻辑接口实现类
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService{

	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	
	@Autowired
	private CourierRepository courierRepository;
	
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	//返回分页数据
	@Override
	public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable) {
		return fixedAreaRepository.findAll(specification, pageable);
	}

	//删除定区
	@Override
	public void delFixedAreas(String[] idArray) {
			
	}

	//查询所有定区
	@Override
	public List<FixedArea> findFixedAreaList() {
		return fixedAreaRepository.findAll();
	}

	//保存定区
	@Override
	public void saveFixedArea(FixedArea fixedArea) {
		fixedAreaRepository.save(fixedArea);
	}
	
	//定区关联快递员,快递员关联收派时间
	@Override
	public void associationCourierToFixedArea(FixedArea fixedArea, Integer courierId, Integer takeTimeId) {
		//查询当前定区
		FixedArea currentFixedArea = fixedAreaRepository.findOne(fixedArea.getId());
		//查询当前要关联的快递员
		Courier courier = courierRepository.findOne(courierId);
		//查询当前要关联的收派时间
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		//定区绑定快递员
		currentFixedArea.getCouriers().add(courier);
		//快递员关联收派时间
		courier.setTakeTime(takeTime);
	}
	
	//根据id查询定区
	@Override
	public FixedArea findFixedAreaById(Integer id) {
		return fixedAreaRepository.findOne(id);
	}
	
}
