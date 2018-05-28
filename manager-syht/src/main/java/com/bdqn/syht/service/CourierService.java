package com.bdqn.syht.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.bdqn.syht.pojo.base.Courier;

/**
 * 快递员设置业务逻辑层接口
 */
public interface CourierService {
	//返回分页数据
	public Page<Courier> findPageData(Specification<Courier> specification,Pageable pageable);
	//保存快递员
	public void saveCourier(Courier courier);
	//作废
	public void delBatch(String[] idArray);
	//还原
	public void doRestore(String[] idArray);
	//查询所有未关联未作废快递员
	public List<Courier> findAll();
	//查询所有快递员
	public List<Courier> findCourierList();
	//根据快递员id查询快递员
	public Courier findCourierById(int courierId);
}
