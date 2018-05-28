package com.bdqn.syht.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.transit.SignInfo;
import com.bdqn.syht.service.transit.SignInfoService;

/**
 * @Author 		chaifei
 * @Time   		2018年5月10日 下午2:59:30
 * @Describe	签收信息控制器
 */
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class SignInfoAction extends BaseAction<SignInfo>{

	@Autowired
	private SignInfoService signInfoService;
	
	//属性驱动: 隐藏域中的id
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}

	/**保存签收信息*/
	
	@Action(value="sign_save",results={@Result(name="success",type="redirect",location="./pages/transit/transitinfo.html")})
	public String saveSignInfo(){
		//向下传递
		signInfoService.saveSignInfo(transitInfoId,model);
		
		return SUCCESS;
	}
}
