package com.bdqn.syht.service.transit.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.WayBillRepository;
import com.bdqn.syht.dao.transit.TransitInfoRepository;
import com.bdqn.syht.index.WayBillIndexRepository;
import com.bdqn.syht.pojo.delivery.WayBill;
import com.bdqn.syht.pojo.transit.TransitInfo;
import com.bdqn.syht.service.transit.TransitInfoService;

/**
 * 配送信息业务逻辑层接口实现类
 */
@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService{

	@Autowired
	private TransitInfoRepository transitInfoRepository;

	@Autowired
	private WayBillRepository wayBillRepository;
	
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	
	@Override
	public void createTransits(String wayBillIds) {
		if(StringUtils.isNotBlank(wayBillIds)){
			//处理运单
			for (String wayBillId:wayBillIds.split(",")) {
				//通过id,查询运单 3个 查询第一个
				WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
				//判断运单状态是否为代发货
				if(wayBill.getSignStatus() == 1){
					//代发货
					//生成transitInfo信息 配送信息
					TransitInfo transitInfo = new TransitInfo();
					transitInfo.setWayBill(wayBill);//运单
					transitInfo.setStatus("出入库中转");//配送状态
					transitInfoRepository.save(transitInfo);//保存
					System.out.println("进入出入库中转");
					
					//更改运单状态
					wayBill.setSignStatus(2); //代发货-->派送中  
					wayBill.getOrder().setStatus("2");//订单状态  已取件--->  运输中
					wayBillRepository.save(wayBill);
					System.err.println("运单状态:派送中");
					
					// 同步索引库
					//wayBillIndexRepository.save(wayBill);
					
				}
			}
		}
	}

	@Override
	public Page<TransitInfo> findPageData(Pageable pageable) {
		return transitInfoRepository.findAll(pageable);
	}
}
