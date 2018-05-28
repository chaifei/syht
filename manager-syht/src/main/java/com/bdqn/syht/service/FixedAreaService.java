package com.bdqn.syht.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.bdqn.syht.pojo.base.FixedArea;

/**
 * 定区管理业务逻辑层接口
 */
public interface FixedAreaService {
	//返回分页数据
	public Page<FixedArea> findPageData(Specification<FixedArea> specification,Pageable pageable);
	//删除定区
	public void delFixedAreas(String[] idArray);
	//查询所有定区
	public List<FixedArea> findFixedAreaList();
	//保存定区
	public void saveFixedArea(FixedArea fixedArea);
	//定区关联快递员,快递员关联收派时间
	public void associationCourierToFixedArea(FixedArea fixedArea,Integer courierId,Integer takeTimeId);
	//根据id查询定区
	public FixedArea findFixedAreaById(Integer id);
}
