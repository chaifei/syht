package com.bdqn.syht.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.transit.InOutStorageInfoRepository;
import com.bdqn.syht.dao.transit.TransitInfoRepository;
import com.bdqn.syht.pojo.transit.InOutStorageInfo;
import com.bdqn.syht.pojo.transit.TransitInfo;
import com.bdqn.syht.service.transit.InOutStorageInfoService;

/**
 * 出入库信息业务逻辑层接口实现类
 */
@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService{

	@Autowired
	private InOutStorageInfoRepository inOutStorageInfoRepository;
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;

	@Override
	public void saveInOutStorageInfo(String transitInfoId, InOutStorageInfo inOutStorageInfo) {
		
		//先保存出入库信息 
		inOutStorageInfoRepository.save(inOutStorageInfo);
		
		//查询TransitInfo 运输配送信息
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		
		//运输配送信息 封装 出入库信息，其实就是保存到了运输配送信息对象transitInfo的出入库集合数据
		transitInfo.getInOutStorageInfos().add(inOutStorageInfo);
		
		//修改状态
		if("到达网点".equals(inOutStorageInfo.getOperation())){
			//修改运输配送状态
			transitInfo.setStatus("到达网点");
			//更新网点地址  ,显示配送路径时使用
			transitInfo.setOutletAddress(inOutStorageInfo.getAddress());
		}
	}

}
