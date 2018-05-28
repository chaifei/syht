package com.bdqn.syht.service.transit;

import com.bdqn.syht.pojo.transit.DeliveryInfo;

/**
 * @Author 		chaifei
 * @Time   		2018年5月10日 上午11:09:47
 * @Describe	配送信息业务逻辑层接口
 */
public interface DeliveryInfoService {
	//保存配送信息
	public void saveDeliveryInfo(String transitInfoId, DeliveryInfo deliveryInfo);
}
