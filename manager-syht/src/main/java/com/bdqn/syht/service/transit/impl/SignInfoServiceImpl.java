package com.bdqn.syht.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.transit.SignInfoRepository;
import com.bdqn.syht.dao.transit.TransitInfoRepository;
import com.bdqn.syht.pojo.transit.SignInfo;
import com.bdqn.syht.pojo.transit.TransitInfo;
import com.bdqn.syht.service.transit.SignInfoService;

/**
 * @Author 		chaifei
 * @Time   		2018年5月10日 下午2:56:35
 * @Describe	签收信息业务逻辑层接口实现类
 */
@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService{

	@Autowired
	private SignInfoRepository signInfoRepository;
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Override
	public void saveSignInfo(String transitInfoId, SignInfo signInfo) {
		//保存签收信息
		signInfoRepository.save(signInfo);
		//查询TransitInfo 运输配送信息
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		//运输配送信息 封装 签收信息
		transitInfo.setSignInfo(signInfo);
		//修改运输配送状态
		if("正常".equals(signInfo.getSignType())) {
			transitInfo.setStatus("正常签收");
		}else {
			transitInfo.setStatus("异常");
		}
	}

}
