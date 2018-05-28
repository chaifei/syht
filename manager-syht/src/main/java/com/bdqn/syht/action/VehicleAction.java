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
import com.bdqn.syht.pojo.base.Vehicle;
import com.bdqn.syht.service.VehicleService;
import com.opensymphony.xwork2.ActionContext;

@Controller //控制器
@Namespace("/")//命名空间
@ParentPackage("json-default")//继承
@Scope("prototype")//作用域
public class VehicleAction extends BaseAction<Vehicle>{
	//班车业务层注入
	@Autowired
	private VehicleService vehicleService;
	
	/**
	 * 带条件分页列表数据查询
	 */
	@Action(value="vehicle_pageQuery",results={@Result(name="success",type="json")})
	public String findVehicleList(){
		//构造分页参数
		Pageable pageable = new PageRequest(page-1, rows);
		//创建集合
		Page<Vehicle> pageData = vehicleService.findPageData(pageable);
		//保存
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total",pageData.getNumberOfElements());
		map.put("rows",pageData.getContent());
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	/**
	 * 添加班车
	 */
	@Action(value="vehicle_save",results={@Result(name="success",type="redirect",location="./pages/base/vehicle.html")})
	public String saveVehicle(){
		vehicleService.saveVehicle(model);
		return SUCCESS;
	}
	
	/**
	 * 删除班车
	 */
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@Action(value="vehicle_delBatch",results={@Result(name="success",location="./pages/base/vehicle.html", type = "redirect")})
	public String delBatch(){
		//按,分隔ids
		String[] idArray = ids.split(",");
		//调用业务层,批量删除
		vehicleService.delBatch(idArray);
		return SUCCESS;
	}
}
