package com.bdqn.syht.service.transit;

import com.bdqn.syht.pojo.transit.InOutStorageInfo;

/**
 * 出入库信息业务逻辑层接口
 */
public interface InOutStorageInfoService {
	//保存出入库信息
	public void saveInOutStorageInfo(String transitInfoId, InOutStorageInfo inOutStorageInfo);
}
