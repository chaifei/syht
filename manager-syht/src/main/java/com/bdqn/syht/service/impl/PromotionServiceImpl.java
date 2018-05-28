package com.bdqn.syht.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.common.PageBean;
import com.bdqn.syht.dao.base.PromotionRepository;
import com.bdqn.syht.pojo.delivery.Promotion;
import com.bdqn.syht.service.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl extends PageBean<Promotion> implements PromotionService{
	
	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public PageBean<Promotion> findPageQuery(int page, int rows) {
		//构造分页数据对象
		Pageable pageable = new PageRequest(page,rows);
		//调用jpa方法查询数据
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		//封装PageBean
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		System.out.println(pageBean.getPageData().get(0).getTitleImg());
		return pageBean;
	}

	@Override
	public Promotion findPromotionById(int promotionId) {
		return promotionRepository.findOne(promotionId);
	}

	@Override
	public void save(Promotion promotion) {
		promotionRepository.save(promotion);
	}

	@Override
	public Page<Promotion> findPageData(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}

	@Override
	public void updateStatus(Date date) {
		promotionRepository.updateStatus(date);
	}

}
