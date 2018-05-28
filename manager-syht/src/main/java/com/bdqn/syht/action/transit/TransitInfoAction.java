package com.bdqn.syht.action.transit;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.transit.TransitInfo;
import com.bdqn.syht.service.transit.TransitInfoService;
import com.opensymphony.xwork2.ActionContext;

/**
 * 配送信息控制器
 */
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class TransitInfoAction extends BaseAction<TransitInfo>{

	
	@Autowired
	private TransitInfoService transitInfoService;
	
	//接收到 运单id集合
	private String wayBillIds;
	
	
	public void setWayBillIds(String wayBillIds) {
		this.wayBillIds = wayBillIds;
	}


	/**开启中转配送*/
	@Action(value="transit_create",results={@Result(name="success",type="json")})
	public String createTransitInfo(){
		
		//调用业务层,保存transitInfo信息
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			transitInfoService.createTransits(wayBillIds);
			//成功
			result.put("success", true);
			result.put("msg", "开始中转配送成功");
		} catch (Exception e) {
			//失败
			result.put("success", false);
			result.put("msg", "开始中转配送失败");
		}
		
		ActionContext.getContext().getValueStack().push(result);
		
		return SUCCESS;
	}
	
	/**查询分页列表数据*/
	@Action(value = "transit_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 分页查询
		Pageable pageable = new PageRequest(page - 1, rows);
		// 调用业务层 查询分页数据
		Page<TransitInfo> pageData = transitInfoService.findPageData(pageable);

		// 压入值栈返回
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
}
