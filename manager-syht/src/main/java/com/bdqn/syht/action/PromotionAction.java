package com.bdqn.syht.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.ServletActionContext;
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
import com.bdqn.syht.pojo.delivery.Promotion;
import com.bdqn.syht.service.PromotionService;

/**宣传活动控制器*/

@Namespace("/") //命名空间
@Controller		//控制器
@ParentPackage("json-default")//继承json
@Scope("prototype")//作用域
public class PromotionAction extends BaseAction<Promotion>{

	//业务逻辑层注入
	@Autowired
	private PromotionService promotionService;
	
	//redis注入
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	/**保存宣传任务*/
	
	//属性驱动
	private File titleImgFile;
	private String titleImgFileFileName;

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	@Action(value = "promotion_save", results = { @Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String savePromotion(){
		// 宣传图 上传、在数据表保存 宣传图路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
		// 生成5位随机数
		String random = RandomStringUtils.randomNumeric(5);
		// 获取图片后缀名
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		// 拼接新的图片名
		String newFileName = random + ext;
		// 创建用于保存图片的对象 (绝对路径)
		File destFile = new File(savePath +"/"+ newFileName);
		// 复制图片
		try {
			FileUtils.copyFile(titleImgFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 将保存路径 相对工程web访问路径，保存model中
		model.setTitleImg(saveUrl + newFileName);	
		//保存其他字段
		model.setStatus("1");
		model.setUpdateTime(new Date());
		model.setUpdateUnit("方兴公司");
		model.setUpdateUser("张杨");
		// 调用业务层，完成活动任务数据保存
		promotionService.save(model);
		// 保存到redis
		redisTemplate.opsForValue().set(model.getId()+"", JSON.toJSONString(model));
		// 返回视图结果
		return SUCCESS;
	}
	
	/**分页查询*/
	
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 构造分页查询参数
		Pageable pageable = new PageRequest(page - 1, rows);
		// 调用业务层 完成查询
		Page<Promotion> pageData = promotionService.findPageData(pageable);
		// 压入值栈
		pushPageDataToValueStack(pageData);
		// 返回视图结果
		return SUCCESS;
	}
}
