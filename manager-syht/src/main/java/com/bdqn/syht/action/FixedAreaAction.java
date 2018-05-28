package com.bdqn.syht.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.crm.domain.Customer;
import com.bdqn.syht.pojo.base.Courier;
import com.bdqn.syht.pojo.base.FixedArea;
import com.bdqn.syht.pojo.base.SubArea;
import com.bdqn.syht.pojo.base.TakeTime;
import com.bdqn.syht.service.CourierService;
import com.bdqn.syht.service.FixedAreaService;
import com.bdqn.syht.service.TakeTimeService;
import com.opensymphony.xwork2.ActionContext;

/**
 * 定区控制器
 */
@Namespace("/") //命名空间
@Controller		//控制器
@ParentPackage("json-default")//继承json
@Scope("prototype")//作用域
public class FixedAreaAction extends BaseAction<FixedArea>{
	//业务逻辑层注入
	@Autowired
	private FixedAreaService fixedAreaService;
	
	//快递员业务逻辑层注入
	@Autowired
	private CourierService courierService;
	
	//收派时间业务逻辑层注入
	@Autowired
	private TakeTimeService takeTimeService;
	
	//redis注入
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	/**多条件分页查询*/
	
	@Action(value="fixedArea_pageQuery",results={@Result(name="success",type="json")})
	public String findPageData(){
		Pageable pageable = new PageRequest(page-1,rows);
		Specification<FixedArea> specification = new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> criter, CriteriaBuilder cb) {
				//保存条件的集合,用于返回
				List<Predicate> list = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(model.getFixedAreaNum())){
					Predicate p1 = cb.like(root.get("fixedAreaNum").as(String.class), model.getFixedAreaNum());
					list.add(p1);
				}
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
					list.add(p2);
				}

				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		//查询分页数据
		Page<FixedArea> pageData = fixedAreaService.findPageData(specification,pageable);
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
	
	private String ids;//属性驱动,id串
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	/**查询所有定区*/
	
	@Action(value ="fixedArea_findAll",results = { @Result(name = "success",type = "json")})
	public String findAll(){
		//查询所有定区
		List<FixedArea> list = fixedAreaService.findFixedAreaList();
		//压入栈顶
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	/**删除分区(物理删除)*/
	
	@Action(value ="fixedArea_delBatch",results = { @Result(name = "success",type = "redirect",location = "./pages/base/fixed_area.html")})
	public String delAreas() {
		// 按,分隔ids
		String[] idArray = ids.split(",");
		// 调用业务层，批量作废
		fixedAreaService.delFixedAreas(idArray);
		return SUCCESS;
	}
	
	/**保存分区*/
	
	@Action(value="fixedArea_save",results={@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String saveArea(){	
		model.setOperator("管理员");
		model.setOperatingTime(new Date());
		model.setOperatingCompany("有关部门");
		//对数据模型进行保存
		fixedAreaService.saveFixedArea(model);
		//保存到redis缓存
		redisTemplate.opsForValue().set(model.getId()+"", JSON.toJSONString(model));
		return SUCCESS;
	}
	
	/**查询未关联快递员*/
	
	@Action(value="courier_findnoassociation",results={@Result(name="success",type="json")})
	public String findnoassociationCourier(){
		List<Courier> list = courierService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	/**查询收派时间*/
	
	@Action(value="takeTime_findAll",results={@Result(name="success",type="json")})
	public String findTakeTimeList(){
		//查询收派时间集合
		List<TakeTime> list = takeTimeService.findAll();
		//将list集合压入栈顶
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	/**关联快递员*/
	
	private Integer courierId;//快递员id属性驱动
	
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}
	
	private Integer takeTimeId;//收派时间id属性驱动
	
	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}
	
	//定区关联快递员
	@Action(value="fixedArea_associationCourierToFixedArea",results={@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String associationCourierToFixedArea(){
		fixedAreaService.associationCourierToFixedArea(model, courierId, takeTimeId);
		return SUCCESS;
	}
	
	
	
	/**webService查询为关联定区的用户列表*/
	@Action(value="fixedArea_findNoAssociationCustomers",results={@Result(name="success",type="json")})
	public String findNoAssociationCustomers(){
		Collection<? extends Customer> collection = WebClient.create("http://localhost:9002/manager-crm/webServices/customerService/noassociationcustomers")
				.accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);		
		return SUCCESS;
	}
	
	/**webService 查询已关联定区的用户列表*/
	@Action(value="fixedArea_findHasAssociationFixedAreaCustomers",results={@Result(name="success",type="json")})
	public String findHasAssociationFixedAreaCustomers(){
		Collection<? extends Customer> collection = 
				WebClient.create("http://localhost:9002/manager-crm/webServices/customerService/associationfixedareacustomers/"+model.getId())
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);		
		return SUCCESS;
	}
	
	/**绑定未关联的客户列表到定区*/
	private String[] customerIds;
	
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	/**客户关联定区*/
	
	@Action(value="fixedArea_associationCustomersToFixedArea",results={@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String assoviationCustomersToFixedArea(){
		String customerIdStr = StringUtils.join(customerIds,",");
		WebClient.create("http://localhost:9002/manager-crm/webServices/customerService/associationcustomerstofixedarea?customerIdStr="+customerIdStr+"&fixedAreaId="+model.getId())
		.put(null);
		return SUCCESS;
	}
	
	//查询定区关联的快递员集合
	
	private Integer fixedAreaId;
	
	public void setFixedAreaId(Integer fixedAreaId) {
		this.fixedAreaId = fixedAreaId;
	}

	@Action(value="find_courier",results={@Result(name="success",type="json")})
	public String findCourierList(){
		//根据id查找当前定区
		FixedArea fixedArea = fixedAreaService.findFixedAreaById(fixedAreaId);
		//查询当前定区关联快递员集合
		Set<Courier> couriers = fixedArea.getCouriers();
		//创建集合保存总行数和分页数据集合
		Map<String,Object> map = new HashMap<String,Object>();
		//保存总行数
		map.put("total", couriers.size());
		//保存分页数据集合
		map.put("rows", couriers);
		//将map集合压入栈顶
		ActionContext.getContext().getValueStack().push(map);
 		return SUCCESS;
	}
	
	//查询定区关联的分区集合
	@Action(value="find_sub_area",results={@Result(name="success",type="json")})
	public String findSubAreaList(){
		//根据id查找当前定区
		FixedArea fixedArea = fixedAreaService.findFixedAreaById(fixedAreaId);
		//查询当前定区关联快递员集合
		 Set<SubArea> subareas = fixedArea.getSubareas();
		//创建集合保存总行数和分页数据集合
		Map<String,Object> map = new HashMap<String,Object>();
		//保存总行数
		map.put("total", subareas.size());
		//保存分页数据集合
		map.put("rows", subareas);
		//将map集合压入栈顶
		ActionContext.getContext().getValueStack().push(map);
 		return SUCCESS;
	}
	
	/**查询关联定区的客户*/
	
	@Action(value="findcustomer",results={@Result(name="success",type="json")})
	public String findcustomerList(){	
		//查询当前定区关联客户集合
		Collection<? extends Customer> collection = 
				WebClient.create("http://localhost:9002/manager-crm/webServices/customerService/associationfixedareacustomers/"+fixedAreaId)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		//创建集合保存总行数和分页数据集合
		Map<String,Object> map = new HashMap<String,Object>();
		//保存总行数
		map.put("total", collection.size());
		//保存分页数据集合
		map.put("rows", collection);
		//将map集合压入栈顶
		ActionContext.getContext().getValueStack().push(map);
		System.err.println(collection.size());
 		return SUCCESS;
	}
}
