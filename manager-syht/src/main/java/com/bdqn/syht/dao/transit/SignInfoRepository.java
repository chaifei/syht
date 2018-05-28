package com.bdqn.syht.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bdqn.syht.pojo.transit.SignInfo;
/**
 * @Author 		chaifei
 * @Time   		2018年5月10日 下午2:43:47
 * @Describe	签收信息数据访问层接口
 */
public interface SignInfoRepository extends JpaRepository<SignInfo, Integer>{

}
