package com.bdqn.syht.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bdqn.syht.pojo.base.FixedArea;
/**
 * 定区数据访问层
 * 命名:实体类+Repository
 * 继承JpaRepository,参数:实体类,主键类型
 */
public interface FixedAreaRepository extends JpaRepository<FixedArea, Integer>,
JpaSpecificationExecutor<FixedArea>{
	
}
