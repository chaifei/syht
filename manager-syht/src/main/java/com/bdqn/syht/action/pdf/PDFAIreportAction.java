package com.bdqn.syht.action.pdf;

import java.util.List;

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

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class PDFAIreportAction extends BaseAction<WayBill>{

	@Autowired
	private WayBillService wayBillService;
	
	/**
	 * 使用 iReport导出PDF
	 * @throws java.lang.Exception 
	 * @throws java.lang.Exception 
	 */
	@Action("report_exportJasperPdf")
	public String exportJasperPdf() throws java.lang.Exception {
	
		Pageable pageable = new PageRequest(0,1);
		// 查询出 满足当前条件 结果数据
		Page<WayBill> pageData = wayBillService.findPageDatas(pageable);

		List<WayBill> wayBills = pageData.getContent();
		// 下载导出
		// 设置头信息
		ServletActionContext.getResponse().setContentType("application/pdf");
		String filename = "运单数据.pdf";
		String agent = ServletActionContext.getRequest()
				.getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition",
				"attachment;filename=" + filename);

		// 根据 jasperReport模板 生成pdf
		// 读取模板文件
		String jrxml = ServletActionContext.getServletContext().getRealPath(
				"/WEB-INF/jasper/waybill.jrxml");
		JasperReport report = JasperCompileManager.compileReport(jrxml);

		// 设置模板数据
		// Parameter变量
		//Map<String, Object> paramerters = new HashMap<String, Object>();
		//paramerters.put("company", "石家庄北大青鸟");
		// Field变量
		JasperPrint jasperPrint = JasperFillManager.fillReport(report,
				null, new JRBeanCollectionDataSource(wayBills));
		System.out.println(wayBills);
		// 生成PDF客户端
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
				ServletActionContext.getResponse().getOutputStream());
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);	
		exporter.exportReport();// 导出
		ServletActionContext.getResponse().getOutputStream().close();
		return NONE;
	}
	
	
}
