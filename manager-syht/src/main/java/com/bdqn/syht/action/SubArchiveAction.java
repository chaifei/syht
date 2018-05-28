package com.bdqn.syht.action;

import java.util.Date;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.base.SubArchive;
import com.bdqn.syht.service.SubArchiveService;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/") //命名空间
@Controller		//控制器
@ParentPackage("json-default")//继承json
@Scope("prototype")//作用域
public class SubArchiveAction extends BaseAction<SubArchive>{
	//业务逻辑层注入
	@Autowired
	private SubArchiveService subArchiveService;
	
	//redis注入
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	//value:接口名称,results:name:返回视图,type:返回数据类型
	@Action(value="sub_archive_pageQuery",results={@Result(name="success",type="json")})
	public String findPageData(){
		//构造分页对象
		Pageable pageable = new PageRequest(page-1,rows);
		//查询分页数据
		Page<SubArchive> pageData = subArchiveService.findPageData(pageable);
		
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
	
	//value:接口名称,results:name:返回视图,type:重定向,location:重定向地址
	@Action(value="sub_archive_save",results={@Result(name="success",type="redirect",location="./pages/base/archives.html")})
	public String saveArchive(){
		model.setOperatingTime(new Date());
		model.setOperatingCompany("后台");
		model.setOperator("管理员");
		//对数据模型进行保存
		subArchiveService.saveSubArchive(model);
		redisTemplate.opsForValue().set(model.getId()+"", JSON.toJSONString(model));
		return SUCCESS;
	}
}
