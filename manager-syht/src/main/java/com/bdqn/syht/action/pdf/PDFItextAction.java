package com.bdqn.syht.action.pdf;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.pojo.delivery.WayBill;
import com.bdqn.syht.service.WayBillService;
import com.bdqn.syht.utils.FileUtils;


import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class PDFItextAction extends BaseAction<WayBill>{

	
	@Autowired
	private WayBillService wayBillService;
	
	@Action("report_exportPdf")
	public String exportPdf() throws IOException, DocumentException {
		// 查询出 满足当前条件 结果数据
		List<WayBill> wayBills = wayBillService.findWayBills(model);

		// 下载导出
		// 设置头信息
		ServletActionContext.getResponse().setContentType("application/pdf");
		String filename = "运单数据.pdf";
		String agent = ServletActionContext.getRequest()
				.getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition",
				"attachment;filename=" + filename);

		// 生成PDF文件
		Document document = new Document();
		//通过反射创建对象并执行流
		PdfWriter.getInstance(document, ServletActionContext.getResponse()
				.getOutputStream());
		//声明打开PDF对象并开始写入数据
		document.open();
		// 写PDF数据
		// 向document 生成pdf表格
		Table table = new Table(7);
		table.setWidth(80); // 宽度
		table.setBorder(1); // 边框
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // 水平对齐方式
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式

		// 设置表格字体
		
		//运用本机winodows字体
		BaseFont cn = BaseFont.createFont("C://Windows//Fonts//SIMHEI.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
		Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);

		// 写表头
		table.addCell(buildCell("运单号", font));
		table.addCell(buildCell("寄件人", font));
		table.addCell(buildCell("寄件人电话", font));
		table.addCell(buildCell("寄件人地址", font));
		table.addCell(buildCell("收件人", font));
		table.addCell(buildCell("收件人电话", font));
		table.addCell(buildCell("收件人地址", font));

		// 写数据
		for (WayBill wayBill : wayBills) {
			table.addCell(buildCell(wayBill.getWayBillNum(), font));
			table.addCell(buildCell(wayBill.getSendName(), font));
			table.addCell(buildCell(wayBill.getSendMobile(), font));
			table.addCell(buildCell(wayBill.getSendAddress(), font));
			table.addCell(buildCell(wayBill.getRecName(), font));
			table.addCell(buildCell(wayBill.getRecMobile(), font));
			table.addCell(buildCell(wayBill.getRecAddress(), font));
		}
		// 将表格加入文档
		document.add(table);

		document.close();

		return NONE;
	}

	private Cell buildCell(String content, Font font)
			throws BadElementException {
		Phrase phrase = new Phrase(content, font);
		return new Cell(phrase);
	}
}
