package com.bdqn.syht.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.pojo.base.Standard;
/**
 * 2018年4月16日 16:28:00
 *@author 户跃祖
 *收派标准业务层接口
 */
public interface StandardService {
	
	//分页方法
	public Page<Standard> findPageData(Pageable pageable);
	
	//新增方法
	public void saveStandard(Standard standard);
	
	//查询所有,用于联查
	public List<Standard> findStandardList();
}
