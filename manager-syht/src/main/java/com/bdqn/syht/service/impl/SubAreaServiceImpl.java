package com.bdqn.syht.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.SubAreaRepository;
import com.bdqn.syht.pojo.base.SubArea;
import com.bdqn.syht.service.SubAreaService;
/**
 * 分区业务逻辑接口实现类
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService{

	@Autowired
	private SubAreaRepository subAreaRepository;
	
	@Override
	public Page<SubArea> findPageData(Pageable pageable) {
		return subAreaRepository.findAll(pageable);
	}

	@Override
	public void delSubAreas(String[] idArray) {
		for(String said : idArray){
			subAreaRepository.delete(said);
		}
	}

	@Override
	public void saveSubAreaList(List<SubArea> areas) {
		subAreaRepository.save(areas);
	}

	@Override
	public void saveSubArea(SubArea subArea) {
		subAreaRepository.save(subArea);
	}

}
