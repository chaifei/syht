package com.bdqn.syht.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.transit.DeliveryInfoRepository;
import com.bdqn.syht.dao.transit.TransitInfoRepository;
import com.bdqn.syht.pojo.transit.DeliveryInfo;
import com.bdqn.syht.pojo.transit.TransitInfo;
import com.bdqn.syht.service.transit.DeliveryInfoService;

/**
 * @Author 		chaifei
 * @Time   		2018年5月10日 上午11:24:40
 * @Describe	配送信息业务逻辑层接口实现类
 */
@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService{

	@Autowired
	private DeliveryInfoRepository deliveryInfoRepository;
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Override
	public void saveDeliveryInfo(String transitInfoId, DeliveryInfo deliveryInfo) {
		//保存配送信息
		deliveryInfoRepository.save(deliveryInfo);
		//查询TransitInfo 运输配送信息
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		//运输配送信息 封装 配送信息
		transitInfo.setDeliveryInfo(deliveryInfo);
		//设置运输状态为
		transitInfo.setStatus("开始配送");
	}

}
