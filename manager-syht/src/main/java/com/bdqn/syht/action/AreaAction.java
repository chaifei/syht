package com.bdqn.syht.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.base.Area;
import com.bdqn.syht.service.AreaService;
import com.bdqn.syht.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/") //命名空间
@Controller		//控制器
@ParentPackage("json-default")//继承json
@Scope("prototype")//作用域
public class AreaAction extends BaseAction<Area>{
	
	//业务逻辑层注入
	@Autowired
	private AreaService areaService;
	
	//redis注入
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	/**多条件分页查询*/
	
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String findPageData(){
		Pageable pageable = new PageRequest(page-1,rows);
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> criter, CriteriaBuilder cb) {
				//保存条件的集合,用于返回
				List<Predicate> list = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(model.getProvince())){
					Predicate p1 = cb.like(root.get("province").as(String.class), "%"+model.getProvince()+"%");
					list.add(p1);
				}
				if(StringUtils.isNotBlank(model.getCity())){
					Predicate p2 = cb.like(root.get("city").as(String.class), "%"+model.getCity()+"%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(model.getDistrict())){
					Predicate p3 = cb.like(root.get("district").as(String.class), "%"+model.getDistrict()+"%");
					list.add(p3);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		//查询分页数据
		Page<Area> pageData = areaService.findPageData(specification,pageable);
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
	
	/**删除区域(物理删除)*/
	
	@Action(value ="area_delete",results = { @Result(name = "success",type = "redirect",location = "./pages/base/area.html")})
	public String delAreas() {
		// 按,分隔ids
		String[] idArray = ids.split(",");
		// 调用业务层，批量作废
		areaService.delAreas(idArray);
		return SUCCESS;
	}
	
	/**查询所有区域*/
	
	@Action(value="area_findAll",results={@Result(name="success",type="json")})
	public String findAreaList(){
		//查询基础档案集合
		List<Area> list = areaService.findAreaList();
		//将list集合压入栈顶
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	/**一键上传Excel*/
	
	//属性驱动, 映射前台传递的参数
	private File file;
	
	public void setFile(File file) {
		this.file = file;
	}
	
	@Action(value = "area_batchImport")
	public String batchImport() throws IOException {
		
		//每一行都是一条记录,所以要把所有记录都封装到集合里
		List<Area> areas = new ArrayList<Area>();
		// 编写解析代码逻辑
		// 基于.xls 格式解析 HSSF
		// 1、 加载Excel文件对象
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		// 2、 读取一个sheet
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		// 3、 读取sheet中每一行
		for (Row row : sheet) {
			// 一行数据 对应 一个区域对象
			if (row.getRowNum() == 0) {
				// 第一行 跳过
				continue;
			}
			// 跳过空行
			if (row.getCell(0) == null
					|| StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			//开始封装对象, 把读取的块级元素赋值给对象的属性.
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			// 基于pinyin4j生成城市编码和简码
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			// 简码
			String[] headArray = PinYin4jUtils.getHeadByString(province + city
					+ district);
			StringBuffer buffer = new StringBuffer();
			for (String headStr : headArray) {
				buffer.append(headStr);
			}
			String shortcode = buffer.toString();
			area.setShortcode(shortcode);
			// 城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(citycode);

			areas.add(area);
		}
		// 调用业务层
		areaService.saveAreaList(areas);

		return NONE;
	}
}
