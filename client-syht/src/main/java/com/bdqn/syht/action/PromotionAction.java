package com.bdqn.syht.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.common.PageBean;
import com.bdqn.syht.pojo.delivery.Promotion;
import com.opensymphony.xwork2.ActionContext;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class PromotionAction extends BaseAction<Promotion>{

	/**分页查询活动数据*/
	
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//基于webService获取manager-syht 的活动列表数据信息
		PageBean<Promotion> pageBean = WebClient.create("http://localhost:9001/manager-syht/webServices/promotionService/promotion/pageQuery?page="+page+"&rows="+rows)
				.accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}
	
	/**页面静态化处理逻辑*/
	
	@Action(value="promotion_showDetail")
	public String showDetail() throws IOException,TemplateException{
		//先判断id对应的html是否存在，如果存在直接返回
		String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
		//构造文件对象
		File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
		//如果html文件不存在，查询数据库，结合freemarker模板生成页面
		if(!htmlFile.exists()){
			//构造对象，设置版本号
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
			configuration.setDirectoryForTemplateLoading(new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates")));
			//获取模板对象
			Template template = configuration.getTemplate("promotion_detail.ftl");
			//动态数据
			Promotion promotion = WebClient.create("http://localhost:9001/manager-syht/webServices/promotionService/promotion/"+model.getId())
					.accept(MediaType.APPLICATION_JSON).get(Promotion.class);
			//创建map集合保存活动信息
			Map<String,Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("promotion", promotion);
			System.err.println(promotion.getTitleImg());
			//合并输出
			template.process(parameterMap, new OutputStreamWriter(new FileOutputStream(htmlFile),"utf-8"));
		}
		//存在，直接将文件返回
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
		return NONE;
	}
}
