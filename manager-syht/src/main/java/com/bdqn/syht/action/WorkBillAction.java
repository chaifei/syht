package com.bdqn.syht.action;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.base.Courier;
import com.bdqn.syht.pojo.delivery.WorkBill;
import com.bdqn.syht.service.CourierService;
import com.bdqn.syht.service.OrderService;
import com.bdqn.syht.service.WorkBillService;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/") //命名空间
@Controller		//控制器
@ParentPackage("json-default")//继承json
@Scope("prototype")//作用域
public class WorkBillAction extends BaseAction<WorkBill>{
	
	@Autowired
	private WorkBillService workBillService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CourierService courierService;
	
	@Autowired  
    @Qualifier("jmsQueueTemplate")  
    private JmsTemplate jmsTemplate;
	
	//value:接口名称,results:name:返回视图,type:返回数据类型
	@Action(value="workBill_pageQuery",results={@Result(name="success",type="json")})
	public String findPageData(){
		//构造分页对象page:当前页,rows页面容量,new Sort:排序
		Pageable pageable = new PageRequest(page-1,rows,new Sort(new Order(Direction.DESC,"id")));
		//查询分页数据
		Page<WorkBill> pageData = workBillService.findPageData(pageable);
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
	
	/**追单*/
	//当前工单id
	private Integer workBillId;
	
	public void setWorkBillId(Integer workBillId) {
		this.workBillId = workBillId;
	}

	@Action(value="workBill_append",results={@Result(name="success",type="json")})
	public String workBillAppend(){
		final WorkBill workBill = workBillService.findByWorkBillId(workBillId);
		workBill.setType("追");
		workBill.setAttachbilltimes(workBill.getAttachbilltimes()+1);
		int i = workBillService.save(workBill);
		Map<String,Object> map = new HashMap<String,Object>();
		if(i>0){
			map.put("success", true);
			map.put("msg", "追单成功！");
			//发送短信
			//调用MQ服务,发送一条信息
			jmsTemplate.send("bos_sms",new MessageCreator(){
				@Override
				public Message createMessage(Session session) throws JMSException{
					//封装短信
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("telephone", workBill.getCourier().getTelephone());
					mapMessage.setString("msg", "短信序号："+workBill.getSmsNumber()+"，取件地址："+workBill.getOrder().getSendAddress()
					+"，联系人："+workBill.getOrder().getSendName()+"，手机："+workBill.getOrder().getSendMobile()
					+"，快递员捎话："+workBill.getOrder().getSendMobileMsg());
					return mapMessage;
				}
			});
		}else{
			map.put("success", false);
			map.put("msg", "追单失败！");
		}
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	/**销单*/
	
	@Action(value="workBill_cancel",results={@Result(name="success",type="json")})
	public String workBillCancel(){
		final WorkBill workBill = workBillService.findByWorkBillId(workBillId);
		workBill.setType("销");
		workBill.setPickstate("已取消");
		workBill.getOrder().setOrderType("5");//已取消
		int i = workBillService.save(workBill);
		Map<String,Object> map = new HashMap<String,Object>();
		if(i>0){
			map.put("success", true);
			map.put("msg", "销单成功！");
			//发送短信
			//调用MQ服务,发送一条信息
			jmsTemplate.send("bos_sms",new MessageCreator(){
				@Override
				public Message createMessage(Session session) throws JMSException{
					//封装短信
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("telephone", workBill.getCourier().getTelephone());
					mapMessage.setString("msg", "短信序号："+workBill.getSmsNumber()+"，取件地址："+workBill.getOrder().getSendAddress()
					+"，联系人："+workBill.getOrder().getSendName()+"，手机："+workBill.getOrder().getSendMobile()+"，订单已取消！");
					return mapMessage;
				}
			});
		}else{
			map.put("success", false);
			map.put("msg", "销单失败！");
		}
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	/**查台转单*/
	
	@Action(value="update_order_courier",results={@Result(name="success",type="redirect",location="./pages/take_delivery/change_work_order.html")})
	public String updateOrderCourier(){
		//根据当前选中工单向下传递查出工单对象
		WorkBill workBill = workBillService.findByWorkBillId(model.getId());
		//修改当前工单状态为‘已取消’,类型为‘销’
		workBill.setPickstate("已取消");
		workBill.setType("销");
		//保存修改信息
		workBillService.save(workBill);
		
		//根据id查出快递员对象
		Courier courier = courierService.findCourierById(model.getCourier().getId());
		//根据订单id查询出订单
		workBill.getOrder().setCourier(courier);
		//生成工单，给新的快递员发短信
		orderService.generateWorkBill(workBill.getOrder());
		return SUCCESS;
	}
	
	/**重发*/
	
	@Action(value="again_message",results={@Result(name="success",type="json")})
	public String againMessage(){
		//根据当前选中工单向下传递查出工单对象
		final WorkBill workBill = workBillService.findByWorkBillId(workBillId);
		//发送短信
		//调用MQ服务,发送一条信息
		jmsTemplate.send("bos_sms",new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException{
				//封装短信
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", workBill.getCourier().getTelephone());
				mapMessage.setString("msg", "短信序号："+workBill.getSmsNumber()+"，取件地址："+workBill.getOrder().getSendAddress()
				+"，联系人："+workBill.getOrder().getSendName()+"，手机："+workBill.getOrder().getSendMobile()+"，订单已取消！");
				return mapMessage;
			}
		});
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("msg", "发送成功！");
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}

}
