package com.bdqn.syht.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bdqn.syht.pojo.transit.DeliveryInfo;

/**
 * @Author 		chaifei
 * @Time   		2018年5月10日 上午11:08:07
 * @Describe	配送信息数据访问层接口
 */
public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer>{

}
