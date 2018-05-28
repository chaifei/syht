package com.bdqn.syht.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bdqn.syht.pojo.base.Archive;

/**
 * 基本档案数据访问层
 * 命名:实体类+Repository
 * 继承JpaRepository,参数:实体类,主键类型
 */
public interface ArchiveRepository extends JpaRepository<Archive, Integer>{

}
