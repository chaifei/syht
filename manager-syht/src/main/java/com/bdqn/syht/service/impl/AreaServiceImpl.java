package com.bdqn.syht.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.AreaRepository;
import com.bdqn.syht.pojo.base.Area;
import com.bdqn.syht.service.AreaService;

/**
 * 区域业务逻辑接口实现类
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService{
	
	//dao层注入
	@Autowired
	private AreaRepository areaRepository;
	
	/** 多条件分页查询 */
	@Override
	public Page<Area> findPageData(Specification<Area> specification, Pageable pageable) {
		return areaRepository.findAll(specification, pageable);
	}

	/** 批量物理删除 */
	@Override
	public void delAreas(String[] idArray) {
		for(String aid : idArray){
			areaRepository.delete(aid);
		}
	}

	/** 查询所有区域  */
	@Override
	public List<Area> findAreaList() {
		return areaRepository.findAll();
	}

	/** Excel导入,批量添加 */
	@Override
	public void saveAreaList(List<Area> areas) {
		for(Area area : areas){
			areaRepository.save(area);
		}
	}

}
