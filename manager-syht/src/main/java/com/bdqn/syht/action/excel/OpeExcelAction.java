package com.bdqn.syht.action.excel;

import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.delivery.WayBill;
import com.bdqn.syht.service.WayBillService;
import com.bdqn.syht.utils.FileUtils;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class OpeExcelAction extends BaseAction<WayBill>{

	
	@Autowired
	private WayBillService wayBillService;
	
	/**
	 * 导出Excel表
	 * @throws Exception 
	 */
	@Action("report_exportXls")
	public String reportExcel() throws Exception{
		
		//Integer.MAX_VALUE 当前记录的最大值  Integer.MIN_VALUE 当前记录的最小值
		Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
		
		Page<WayBill> pageData = wayBillService.findPageDatas(pageable);
		//查询出满足当前条件的结果数据
		List<WayBill> wayBills = pageData.getContent();
		
		//生成Excel文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		//生成工作布
		HSSFSheet sheet = hssfWorkbook.createSheet("运单管理");
		//生成表头 在第一行
		HSSFRow headRow = sheet.createRow(0);
		
		headRow.createCell(0).setCellValue("运单号");
		headRow.createCell(1).setCellValue("寄件人");
		headRow.createCell(2).setCellValue("寄件人电话");
		headRow.createCell(3).setCellValue("寄件人地址");
		headRow.createCell(4).setCellValue("收件人");
		headRow.createCell(5).setCellValue("收件人电话");
		headRow.createCell(6).setCellValue("收件人地址");
		
		//表格数据
		for (WayBill wayBill : wayBills) {
			
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			
			dataRow.createCell(0).setCellValue(wayBill.getWayBillNum());
			dataRow.createCell(1).setCellValue(wayBill.getSendName());
			dataRow.createCell(2).setCellValue(wayBill.getSendMobile());
			dataRow.createCell(3).setCellValue(wayBill.getSendAddress());
			dataRow.createCell(4).setCellValue(wayBill.getRecName());
			dataRow.createCell(5).setCellValue(wayBill.getRecMobile());
			dataRow.createCell(6).setCellValue(wayBill.getRecAddress());
			
		}
		
		//下载导出
		//设置消息头信息
		ServletActionContext.getResponse().setContentType("application/vnd.ms-excel");
		
		String fileName = "运单管理.xls";
		
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		
		FileUtils.encodeDownloadFilename(fileName, agent);
		
		ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment;filename="+fileName);
		
		
		//设置流
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		
		hssfWorkbook.write(outputStream);
		
		//关闭流
		
		hssfWorkbook.close();
		
		return NONE;
	}
}
