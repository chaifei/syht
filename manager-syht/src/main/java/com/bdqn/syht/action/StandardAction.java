package com.bdqn.syht.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.base.Archive;
import com.bdqn.syht.pojo.base.Standard;
import com.bdqn.syht.service.StandardService;
import com.opensymphony.xwork2.ActionContext;

@Controller//控制器
@Namespace("/")//命名空间
@ParentPackage("json-default")//继承
@Scope("prototype")//作用域
public class StandardAction extends BaseAction<Standard>{
	
	//收派标准业务层注入
	@Autowired
	private StandardService standardService;
	//redis注入
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	

	
	/**
	 * 分页无条件查询
	 * @return
	 */
	//@Action声明Action，value：接口，@Result跳转不同的Action，name:返回视图，查询必须用type="json"
	@Action(value="standard_pageQuery",results={@Result(name="success",type="json")})
	public String findPageData(){
		//构造分页参数
		Pageable pageable = new PageRequest(page-1,rows);
		//调用Standard方法
		Page<Standard> pageDate = standardService.findPageData(pageable);
		//map集合
		Map<String,Object> map= new HashMap<String,Object>();
		//保存总行数
		map.put("total",pageDate.getNumberOfElements());
		//保存分页数据集合
		map.put("rows",pageDate.getContent());
		//压入栈顶
		ActionContext.getContext().getValueStack().push(map);
			
		return SUCCESS;
	} 
	
	
		
	/**
	 * 增加保存
	 * @return
	 */
	//@Action声明Action，value：接口，@Result跳转不同的Action，name:返回视图，redirect类型重定向一个页面，location：文件路径
	@Action(value="standard_save",results={@Result(name="success",type="redirect",location="./pages/base/standard.html")})
	public String saveStandar(){
		//属性赋值
		model.setOperatingTime(new Date());
		model.setOperatingCompany("后台");
		model.setOperator("管理员");
		//对数据模型进行保存
		standardService.saveStandard(model);
		return SUCCESS;
	}
	
	//value:接口名称,results:name:返回视图,type:返回数据类型
	@Action(value="standard_findAll",results={@Result(name="success",type="json")})
	public String findArchiveList(){
		//查询基础档案集合
		List<Standard> list = standardService.findStandardList();
		//将list集合压入栈顶
		ActionContext.getContext().getValueStack().push(list);
		//保存到redis缓存
		redisTemplate.opsForValue().set(model.getId()+"", JSON.toJSONString(model));
		return SUCCESS;
	}
	
}
