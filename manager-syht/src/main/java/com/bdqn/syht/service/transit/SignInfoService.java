package com.bdqn.syht.service.transit;

import com.bdqn.syht.pojo.transit.SignInfo;
/**
 * @Author 		chaifei
 * @Time   		2018年5月10日 下午2:46:41
 * @Describe	签收信息业务逻辑层接口
 */
public interface SignInfoService {
	//保存签收信息
	public void saveSignInfo(String transitInfoId,SignInfo signInfo);
}
