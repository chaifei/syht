package com.bdqn.syht.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.bdqn.syht.pojo.base.Area;

/**
 * 区域业务逻辑接口
 */
public interface AreaService {
	//返回分页数据
	public Page<Area> findPageData(Specification<Area> specification,Pageable pageable);
	//删除区域
	public void delAreas(String[] idArray);
	//查询所有区域
	public List<Area> findAreaList();
	//Excel导入区域
	public void saveAreaList(List<Area> areas);
	
}
