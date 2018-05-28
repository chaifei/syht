package com.bdqn.syht.service.transit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.pojo.transit.TransitInfo;

/**
 * 配送信息业务逻辑层接口
 */
public interface TransitInfoService {
	
	//创建配送信息
	public void createTransits(String wayBillIds);
	
	//分页查询配送信息
	public Page<TransitInfo> findPageData(Pageable pageable);
}
