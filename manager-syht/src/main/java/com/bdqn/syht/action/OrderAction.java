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
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.base.Courier;
import com.bdqn.syht.pojo.delivery.Order;
import com.bdqn.syht.service.CourierService;
import com.bdqn.syht.service.OrderService;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/") //命名空间
@Controller		//控制器
@ParentPackage("json-default")//继承json
@Scope("prototype")//作用域
public class OrderAction extends BaseAction<Order>{
	//业务逻辑层注入
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CourierService courierService;
	
	//value:接口名称,results:name:返回视图,type:返回数据类型
	@Action(value="neworder_pageQuery",results={@Result(name="success",type="json")})
	public String findPageData(){
		//构造分页对象page:当前页,rows页面容量
		Pageable pageable = new PageRequest(page-1,rows);
		//查询分页数据
		Page<Order> pageData = orderService.findPageData(pageable);
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
	
	//人工调度
	@Action(value="update_order_dispatcher",results={@Result(name="success",type="redirect",location="./pages/take_delivery/dispatcher.html")})
	public String updateOrderDispatcher(){
		//根据当前选中订单向下传递查出订单对象
		Order order = orderService.findByOrderId(model.getId());
		//查询出快递员
		Courier courier = courierService.findCourierById(model.getCourier().getId());
		//订单绑定快递员
		order.setCourier(courier);
		//保存修改信息
		orderService.updateOrderDispatcher(order);
		//生成工单，发短信通知快递员
		orderService.generateWorkBill(order);
		return SUCCESS;
	}
}
