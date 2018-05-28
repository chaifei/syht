package com.bdqn.syht.service;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.common.PageBean;
import com.bdqn.syht.pojo.delivery.Promotion;

public interface PromotionService {
	
	// 根据page和rows 返回分页数据
	@Path("/promotion/pageQuery")
	@GET
	@Produces({ "application/xml", "application/json" })
	@Consumes({"application/xml","application/json"})
	public PageBean<Promotion> findPageQuery(@QueryParam("pageable") int page,
			@QueryParam("rows") int rows);
	
	// 根据id查询宣传活动
	@Path("/promotion/{promotionId}")
	@GET
	@Produces({ "application/xml", "application/json" })
	@Consumes({"application/xml","application/json"})
	public Promotion findPromotionById(@PathParam("promotionId") int promotionId);
	//保存宣传活动
	public void save(Promotion promotion);
	//分页查询
	public Page<Promotion> findPageData(Pageable pageable);
	//活动过期设置活动状态为‘2’
	public void updateStatus(Date date);
}
