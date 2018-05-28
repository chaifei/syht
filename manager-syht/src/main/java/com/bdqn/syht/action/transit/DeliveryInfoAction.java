package com.bdqn.syht.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.transit.DeliveryInfo;
import com.bdqn.syht.service.transit.DeliveryInfoService;

/**
 * @Author 		chaifei
 * @Time   		2018年5月10日 上午11:45:32
 * @Describe	配送信息控制器
 */
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class DeliveryInfoAction extends BaseAction<DeliveryInfo>{

	@Autowired
	private DeliveryInfoService deliveryInfoService;
	
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}

	@Action(value="delivery_save",results={@Result(name="success",type="redirect",location="./pages/transit/transitinfo.html")})
	public String saveDeliveryInfo(){
		
		deliveryInfoService.saveDeliveryInfo(transitInfoId,model);
		
		return SUCCESS;
	}
}
