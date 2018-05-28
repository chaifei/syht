package com.bdqn.syht.action;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.delivery.Order;
import com.bdqn.syht.pojo.delivery.WayBill;
import com.bdqn.syht.service.OrderService;
import com.bdqn.syht.service.WayBillService;
import com.opensymphony.xwork2.ActionContext;

/**
 * 运单控制器
 * @author chaifei
 * @time 2018年4月29日 下午2:03:52
 *
 */
@Namespace("/")
@Controller
@ParentPackage("json-default")
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill>{

	@Autowired
	private WayBillService wayBillService;
	
	@Autowired
	private OrderService orderService;
	
	//redis注入
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	/**分页查询*/
	
//	@Action(value="waybill_pageQuery",results={@Result(name="success",type="json")})
//	public String findPageData(){
//		//构造分页对象
//		Pageable pageable = new PageRequest(page-1, rows);
//		//查询分页数据
//		Page<WayBill> pageData = wayBillService.findPageData(pageable);
//		//创建集合保存总行数和分页数据集合
//		Map<String,Object> map = new HashMap<String,Object>();
//		//保存总行数
//		map.put("total", pageData.getNumberOfElements());
//		//保存分页数据集合
//		map.put("rows", pageData.getContent());
//		//将map集合压入栈顶
//		ActionContext.getContext().getValueStack().push(map);
//		return SUCCESS;
//	}
	
	@Action(value = "waybill_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 无条件查询   按照ID降序排列
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
		// 调用业务层进行查询
		Page<WayBill> pageData = wayBillService.findPageData(model, pageable);
		// 压入值栈返回
		pushPageDataToValueStack(pageData);

		return SUCCESS;
	}
	
	/**运单快速录入*/
	
	@Action(value="waybill_save",results={@Result(name="success",type="json")})
	public String wayBillQuick(){
		model.setSignStatus(1);//运单状态： 1 待发货、 2 派送中、3 已签收、4 异常
		model.setDelTag("否");//1、新增修改单据状态为“否” 2、作废时需将状态置为“是” 3、取消作废时需要将状态置为“否”
		model.setWayBillType("正常单据");//运单类型（类型包括：正常单据、异单、调单）
		// 去除 没有id的order对象
		if (model.getOrder() != null
				&& (model.getOrder().getId() == null || model.getOrder()
						.getId() == 0)) {
			model.setOrder(null);//如果数据重叠,优先使用运单数据
		}
		//对数据模型进行保存
		int i = wayBillService.save(model);
		Map<String,Object> result = new HashMap<String,Object>();
		if(i>0){
			result.put("success", true);
			result.put("msg", "保存成功");
			redisTemplate.opsForValue().set(model.getId()+"", JSON.toJSONString(model));
		}else{
			result.put("success", false);
			result.put("msg", "保存失败");
		}	
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
	/**运单信息回显*/
	
	private String ordernum; 

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	@Action(value="order_findByOrderNum",results={@Result(name="success",type="json")})
	public String findByOrderNum(){
		System.err.println(ordernum);
		//查询出订单
		Order order = orderService.findByOrderNum(ordernum);
		System.err.println(order);
		//封装到map集合
		Map<String,Object> result = new HashMap<String,Object>();
		if(order != null){
			result.put("success", true);
			result.put("orderData", order);
		}
		
		//将map集合压入栈顶
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
	private String wayBillnum;

	public void setWayBillnum(String wayBillnum) {
		this.wayBillnum = wayBillnum;
	}

	@Action(value="waybill_findByWayBillNum",results={@Result(name="success",type="json")})
	public String findByWayBillNum(){
		System.err.println(wayBillnum);
		//查询出快速录入的运单
		WayBill wayBill = wayBillService.findByWayBillNum(wayBillnum);
		//封装到map集合
		Map<String,Object> result = new HashMap<String,Object>();
		if(wayBill != null){
			result.put("success", true);
			result.put("wayBillData", wayBill);
		}
		//将map集合压入栈顶
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
