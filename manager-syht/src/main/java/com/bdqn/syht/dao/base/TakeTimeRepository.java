package com.bdqn.syht.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bdqn.syht.pojo.base.TakeTime;

/**
 * 收派时间管理数据访问层
 * @author
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer>{


}
