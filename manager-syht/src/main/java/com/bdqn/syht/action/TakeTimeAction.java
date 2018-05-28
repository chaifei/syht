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
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.base.TakeTime;
import com.bdqn.syht.service.TakeTimeService;
import com.opensymphony.xwork2.ActionContext;

@Controller//控制器
@Namespace("/")//命名空间
@ParentPackage("json-default")//继承
@Scope("prototype")//作用域
public class TakeTimeAction extends BaseAction<TakeTime>{
	//收派时间管理业务层注入
		@Autowired
		private TakeTimeService takeTimeService;
		
		
		/**
		 * 分页无条件查询
		 * @return
		 */
		//@Action声明Action，value：接口，@Result跳转不同的Action，name:返回视图，查询必须用type="json"
		@Action(value="takeTime_pageQuery",results={@Result(name="success",type="json")})
		public String findPageData(){
			//构造分页参数
			Pageable pageable = new PageRequest(page-1,rows);
			//调用service层的分页方法
			Page<TakeTime> pageDate = takeTimeService.findPageData(pageable);
			//map集合
			Map<String,Object> map= new HashMap<String,Object>();
			//返回行数
			map.put("total",pageDate.getNumberOfElements());
			//返回内容
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
		@Action(value="takeTime_save",results={@Result(name="success",type="redirect",location="./pages/base/take_time.html")})
		public String takeTimeService(){
			model.setOperator("管理员");
			model.setOperatingTime(new Date());
			model.setOperatingCompany("有关部门");
			model.setStatus('0');
			//调用service层的增加方法
			takeTimeService.saveTakeTime(model);
			
			return SUCCESS;
		}
		
		/**
		 * 物理删除
		 * @return
		 */
		//声明一个对象,并set它
		private String ids;
		public void setIds(String ids){
			this.ids=ids;
		}
		//@Action声明Action，value：接口，@Result跳转不同的Action，name:返回视图，redirect类型重定向一个页面，location：文件路径
		@Action(value="takeTime_delBatch",results={@Result(name="success",type="redirect",location="./pages/base/take_time.html")})
		public String delBatch(){
			//按,分隔ids
			String[] idArray = ids.split(",");
			//调用业务层，批量作废
			takeTimeService.delBatch(idArray);
			
			return SUCCESS;
		}


}
