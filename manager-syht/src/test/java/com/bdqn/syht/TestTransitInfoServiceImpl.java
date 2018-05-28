package com.bdqn.syht;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.dao.transit.TransitInfoRepository;
import com.bdqn.syht.pojo.transit.TransitInfo;
import com.bdqn.syht.service.transit.TransitInfoService;

public class TestTransitInfoServiceImpl extends SpringTestBase{
	
	@Autowired
	private TransitInfoRepository transitInfoService;
	
	@Test
    public void testPing() {
		
		List<TransitInfo> findPageData = transitInfoService.findAll();
		System.err.println(findPageData.size());
    }
}
