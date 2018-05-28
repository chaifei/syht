package com.bdqn.syht.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bdqn.syht.pojo.transit.InOutStorageInfo;

/**
 * 出入库信息数据访问层接口
 */
public interface InOutStorageInfoRepository extends JpaRepository<InOutStorageInfo, Integer>{

}
