package com.bdqn.syht.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import com.bdqn.syht.pojo.base.Area;
import com.bdqn.syht.pojo.base.SubArea;
import com.bdqn.syht.service.AreaService;
import com.bdqn.syht.service.SubAreaService;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/") //命名空间
@Controller		//控制器
@ParentPackage("json-default")//继承json
@Scope("prototype")//作用域
public class SubAreaAction extends BaseAction<SubArea>{
	//业务逻辑层注入
	@Autowired
	private SubAreaService subAreaService;
	
	@Autowired
	private AreaService areaService;
	
	//redis注入
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	/**分页查询*/
	
	@Action(value="subArea_pageQuery",results={@Result(name="success",type="json")})
	public String findPageData(){
		Pageable pageable = new PageRequest(page-1,rows);
		//查询分页数据
		Page<SubArea> pageData = subAreaService.findPageData(pageable);
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
	
	private String ids;//属性驱动,id串
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	/**删除分区(物理删除)*/
	
	@Action(value ="sub_area_delBatch",results = { @Result(name = "success",type = "redirect",location = "./pages/base/subArea.html")})
	public String delAreas() {
		// 按,分隔ids
		String[] idArray = ids.split(",");
		// 调用业务层，批量作废
		subAreaService.delSubAreas(idArray);
		return SUCCESS;
	}
	
	/**保存分区*/
	
	@Action(value="sub_area_save",results={@Result(name="success",type="redirect",location="./pages/base/subArea.html")})
	public String saveArea(){	
		//对数据模型进行保存
		subAreaService.saveSubArea(model);
		//保存到redis缓存
		redisTemplate.opsForValue().set(model.getId()+"", JSON.toJSONString(model));
		return SUCCESS;
	}
	
	/**一键上传Excel*/
	
	//属性驱动, 映射前台传递的参数
	private File file;

	public void setFile(File file) {
		this.file = file;
	}
	
	@Action(value = "sub_area_batchImport")
	public String batchImport() throws IOException {
		//每一行都是一条记录,所以要把所有记录都封装到集合里
		List<SubArea> subAreas = new ArrayList<SubArea>();
		// 编写解析代码逻辑
		// 基于.xls 格式解析 HSSF
		// 1、 加载Excel文件对象
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		// 2、 读取一个sheet
		HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
		// 3、 读取sheet中每一行
		for(Row row : sheetAt){
			// 一行数据 对应 一个区域对象
			if(row.getRowNum()==0){
				//第一行跳过
				continue;
			}
			// 跳过空行
			if(row.getCell(0)==null||StringUtils.isBlank(row.getCell(0).getStringCellValue())){
				continue;
			}
			//查询所有区域
			List<Area> areas = areaService.findAreaList();
			//开始封装对象, 把读取的块级元素赋值给对象的属性.
			SubArea subArea = new SubArea();
			subArea.setId(row.getCell(0).getStringCellValue());
			for(Area area : areas){
				if(row.getCell(1).getStringCellValue().equals(area.getId())){
					subArea.setArea(area);
					break;
				}
			}
			subArea.setKeyWords(row.getCell(2).getStringCellValue());
			subArea.setStartNum(row.getCell(3).getStringCellValue());
			subArea.setEndNum(row.getCell(4).getStringCellValue());
			subArea.setSingle(row.getCell(5).getStringCellValue().charAt(0));
			subArea.setAssistKeyWords(row.getCell(6).getStringCellValue());
			subAreas.add(subArea);
		}
		subAreaService.saveSubAreaList(subAreas);
		return NONE;
	}
}
