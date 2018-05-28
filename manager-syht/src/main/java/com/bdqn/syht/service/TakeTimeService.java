package com.bdqn.syht.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.pojo.base.TakeTime;

/**
 * 2018年4月16日 16:28:00
 *@author 户跃祖
 *收派时间管理业务层接口
 */
public interface TakeTimeService {
		//分页方法
		public Page<TakeTime> findPageData(Pageable pageable);
		//新增方法
		public void saveTakeTime(TakeTime takeTime);
		//物理删除方法
		public void delBatch(String[] idArray);
		//查询所有
		public List<TakeTime> findAll();
}
