package com.bdqn.syht.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.base.Courier;
import com.bdqn.syht.service.CourierService;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/") //命名空间
@Controller		//控制器
@ParentPackage("json-default")//继承json
@Scope("prototype")//作用域
public class CourierAction extends BaseAction<Courier>{
	//业务逻辑层注入
	@Autowired
	private CourierService courierService;
	
	//redis注入
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	//value:接口名称,results:name:返回视图,type:返回数据类型
	@Action(value="courier_pageQuery",results={@Result(name="success",type="json")})
	public String findPageData(){
		//构造分页对象page:当前页,rows页面容量,new Sort:排序
		Pageable pageable = new PageRequest(page-1,rows,new Sort(new Order(Direction.DESC,"id")));
		//根据查询条件,构造Specification条件查询对象
		Specification<Courier> specification = new Specification<Courier>() {
			/**
			 * 构造条件查询方法,如果方法返回null  代表无条件查询
			 * Root 参数 获取条件表达式 
			 * CriteriaQuery 参数 构造简单查询条件返回,提供where方法
			 * CriteriaBuilder 参数 构造Predicate对象,条件对象,构造复杂查询效果
			 */
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> criter, CriteriaBuilder cb) {
				//保存条件的集合,用于返回
				List<Predicate> list = new ArrayList<Predicate>();
				//工号,如果不为空,保存到条件集合,等值查询
				if(StringUtils.isNotBlank(model.getCourierNum())){
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), model.getCourierNum());
					list.add(p1);
				}
				//所属单位,如果不为空,保存到条件集合,等值查询
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class),"%"+ model.getCompany()+"%");
					list.add(p2);
				}
				//类型,如果不为空,保存到条件集合,等值查询
				if(StringUtils.isNotBlank(model.getType())){
					Predicate p3 = cb.equal(root.get("type").as(String.class), model.getType());
					list.add(p3);
				}
				//多表查询收派标准,如果不为空,保存到条件集合,模糊查询
				Join<Object, Object> join = root.join("standard",JoinType.INNER);
				if(model.getStandard() != null && StringUtils.isNotBlank(model.getStandard().getName())){
					Predicate p4 = cb.like(join.get("name").as(String.class),"%"+ model.getStandard().getName()+"%");
					list.add(p4);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}	
		};
		
		//查询分页数据
		Page<Courier> pageData = courierService.findPageData(specification,pageable);
		//创建集合保存总行数和分页数据集合
		Map<String,Object> map = new HashMap<String,Object>();
		//保存总行数
		map.put("total", pageData.getNumberOfElements());
		//保存分页数据集合
		map.put("rows", pageData.getContent());
		//将map集合压入栈顶
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	//保存快递员
	//value:接口名称,results:name:返回视图,type:重定向,location:重定向地址
	@Action(value="courier_save",results={@Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String saveCourier(){
		//设置作废标志为'0'可用'1'禁用
		model.setDeltag('0');
		//对数据模型进行保存
		courierService.saveCourier(model);
		redisTemplate.opsForValue().set(model.getId()+"", JSON.toJSONString(model));
		return SUCCESS;
	}
	
	
	private String ids;//属性,id串
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	//作废快递员(逻辑删除)
	@Action(value ="courier_delBatch",results = { @Result(name = "success",type = "redirect",location = "./pages/base/courier.html")})
	public String delBatch() {
		// 按,分隔ids
		String[] idArray = ids.split(",");
		// 调用业务层，批量作废
		courierService.delBatch(idArray);
		return SUCCESS;
	}
	
	//还原快递员
	@Action(value="courier_restore",results={@Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String restore(){
		// 按,分隔ids
		String[] idArray = ids.split(",");
		// 调用业务层，批量还原
		courierService.doRestore(idArray);
		return SUCCESS;
	}
	
	//查询所有快递员
	@Action(value="courier_ajaxlist",results={@Result(name="success",type="json")})
	public String findCourierList(){	
		List<Courier> couriers = courierService.findCourierList();
		ActionContext.getContext().getValueStack().push(couriers);
		return SUCCESS;
	}
}
