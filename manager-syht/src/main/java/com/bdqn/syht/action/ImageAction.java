package com.bdqn.syht.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * 图片管理类
 */
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
@Controller
public class ImageAction extends BaseAction<Object>{
	//属性驱动
	private File imgFile;	//文件
	private String imgFileFileName;//文件名
	private String imgFileContentType;//文件类型
	
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}
	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}
	
	/**图片上传*/
	@Action(value="image_upload",results={@Result(name="success",type="json")})
	public String upload(){
		// 宣传图 上传、在数据表保存 宣传图路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
		// 生成5位随机数
		String random = RandomStringUtils.randomNumeric(5);
		// 获取图片后缀名
		String ext = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
		// 拼接新的图片名
		String newFileName = random + ext;
		// 创建用于保存图片的对象 (绝对路径)
		File destFile = new File(savePath +"/"+ newFileName);
		// 复制图片
		try {
			FileUtils.copyFile(imgFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//通知浏览器文件上传成功
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("error", 0);
		map.put("url", saveUrl+newFileName);//相对路径
		
		return SUCCESS;
	}
	
	/**图片回显*/
	@Action(value = "image_manage", results = { @Result(name = "success", type = "json") })
	public String manage() {
		// 根目录路径，可以指定绝对路径，比如 d:/xxx/upload/xxx.jpg
		String rootPath = ServletActionContext.getServletContext().getRealPath(
				"/")
				+ "upload/";
		// 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";

		// 遍历目录取的文件信息
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		// 当前上传目录
		File currentPathFile = new File(rootPath);
		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };
		//遍历上传路径文件
		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				//声明集合封装图片数据
				Map<String, Object> hash = new HashMap<String, Object>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(
							fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String> asList(fileTypes)
							.contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime",
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file
								.lastModified()));
				fileList.add(hash);
			}
		}
		//封装图片集合数据
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		//压入栈顶
		ActionContext.getContext().getValueStack().push(result);
		//返回视图
		return SUCCESS;
	}
}
