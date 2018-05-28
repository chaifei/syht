package com.bdqn.syht.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.CourierRepository;
import com.bdqn.syht.pojo.base.Courier;
import com.bdqn.syht.service.CourierService;

/**
 * 快递员业务逻辑层实现类
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService{
	//dao注入
	@Autowired
	private CourierRepository courierRepository;
	
	//查询分页
	@Override
	public Page<Courier> findPageData(Specification<Courier> specification,Pageable pageable) {
		return courierRepository.findAll(specification, pageable);
	}
	//保存
	@Override
	public void saveCourier(Courier courier) {
		courierRepository.save(courier);
	}
	//作废
	@Override
	public void delBatch(String[] idArray) {
		for(String cid : idArray){
			Integer id = Integer.parseInt(cid);
			courierRepository.updateDelTag(id);
		}
	}
	//还原
	@Override
	public void doRestore(String[] idArray) {
		for(String cid : idArray){
			Integer id = Integer.parseInt(cid);
			courierRepository.updateDelTagForRestore(id);
		}
	}
	//查询未关联
	@Override
	public List<Courier> findAll() {
		//构造条件对象集合
		Specification<Courier> specification = new Specification<Courier>() {
			//重写查询条件方法
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> criter, CriteriaBuilder cb) {
				//保存条件的集合,用于返回
				List<Predicate> list = new ArrayList<Predicate>();
				//快递员关联定区为空
				Predicate p1 = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				list.add(p1);
				//快递员作废标记为‘0’可用
				Predicate p2 = cb.equal(root.get("deltag").as(Character.class), '0');
				list.add(p2);
				//返回条件数组
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		//返回所有符合条件的快递员
		return courierRepository.findAll(specification);
	}
	
	//查询所有快递员
	@Override
	public List<Courier> findCourierList() {
		return courierRepository.findAll();
	}
	
	//根据id查询快递员
	@Override
	public Courier findCourierById(int courierId) {
		return courierRepository.findOne(courierId);
	}

	
}
