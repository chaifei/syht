package com.bdqn.syht.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.transit.InOutStorageInfo;
import com.bdqn.syht.service.transit.InOutStorageInfoService;

/**
 * 出入库信息控制器
 */
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class InOutStorageInfoAction extends BaseAction<InOutStorageInfo>{

	@Autowired
	private InOutStorageInfoService inOutStorageInfoService;
	
	//属性驱动: 隐藏域中的id
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}
	
	/**保存出入库信息*/
	
	@Action(value="inoutstore_save",results={@Result(name="success",type="redirect",location="./pages/transit/transitinfo.html")})
	public String saveInOutStorageInfo(){
		
		//向下传递
		inOutStorageInfoService.saveInOutStorageInfo(transitInfoId,model);
		
		return SUCCESS;
	}
	
}
