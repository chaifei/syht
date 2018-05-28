package com.bdqn.syht.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bdqn.syht.pojo.base.Courier;
/**
 * 快递员数据访问层
 * 命名:实体类+Repository
 * 继承JpaRepository,参数:实体类,主键类型
 */
public interface CourierRepository extends JpaRepository<Courier, Integer>,
JpaSpecificationExecutor<Courier>{
	
	/**
	 * 作废,deltag改为1
	 */
	@Query(value = "update Courier set deltag='1' where id = ?")//用于修改的HQL语句
	@Modifying//修改注解
	public void updateDelTag(Integer id);

	/**
	 * 还原,deltag改为0 
	 */
	@Query(value = "update Courier set deltag='0' where id = ?")//用于修改的HQL语句
	@Modifying//修改注解
	public void updateDelTagForRestore(Integer id);
}
