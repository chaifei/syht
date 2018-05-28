package com.bdqn.syht.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.pojo.base.SubArea;

/**
 * 分区业务逻辑接口
 */
public interface SubAreaService {
	//返回分页数据
	public Page<SubArea> findPageData(Pageable pageable);
	//删除区域
	public void delSubAreas(String[] idArray);
	//Excel导入区域
	public void saveSubAreaList(List<SubArea> areas);
	//保存分区
	public void saveSubArea(SubArea subArea);
	
}
