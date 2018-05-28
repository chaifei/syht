package com.bdqn.syht.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.crm.domain.Customer;
import com.bdqn.syht.pojo.base.Area;
import com.bdqn.syht.pojo.delivery.Order;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class OrderAction extends BaseAction<Order>{

	private String sendAreaInfo; //发件人省市区
	
	private String recAreaInfo;//收件人省市区
	
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	/**客户下单*/
	@Action(value="order_add",results={@Result(name="success",type="redirect",location="index.html")})
	public String add(){
		//构造区域对象
		Area sendArea = new Area();
		//切割前台传递的发件人省市区字符串，保存到数组中
		String[] sendAreaData = sendAreaInfo.split("/");
		//封装发件人省市区
		sendArea.setProvince(sendAreaData[0]);
		sendArea.setCity(sendAreaData[1]);
		sendArea.setDistrict(sendAreaData[2]);

		//封装收件人省市区
		Area recArea = new Area();
		
		//切割前台传递的收件人省市区字符串，保存到数组中
		String[] recAreaData = recAreaInfo.split("/");
		recArea.setProvince(recAreaData[0]);
		recArea.setCity(recAreaData[1]);
		recArea.setDistrict(recAreaData[2]);

		//二次封装省市区
		model.setRecArea(recArea);
		model.setSendArea(sendArea);

		//获取session域中保存的用户
		Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");

		model.setCustomer_id(customer.getId());
		
		//调用webService
		WebClient.create("http://localhost:9001/manager-syht/webServices/orderService/order").type(MediaType.APPLICATION_JSON).post(model);

		return SUCCESS;
	}
}
