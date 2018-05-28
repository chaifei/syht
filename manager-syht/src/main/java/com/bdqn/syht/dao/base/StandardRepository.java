package com.bdqn.syht.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bdqn.syht.pojo.base.Standard;

/**
 * 收派标准数据访问层
 * 命名:实体类+Repository
 * 继承JpaRepository,参数:实体类,主键类型
 */
public interface StandardRepository extends JpaRepository<Standard, Integer>{


}
