package com.bdqn.syht.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bdqn.syht.pojo.base.Area;

/**
 * 区域数据访问层
 * 命名:实体类+Repository
 * 继承JpaRepository,参数:实体类,主键类型
 */
public interface AreaRepository extends JpaRepository<Area, String>,
JpaSpecificationExecutor<Area>{
	//根据省市区查询区域
	public Area findByProvinceAndCityAndDistrict(String province,String city,String district);
}
