package com.bdqn.syht.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {
	private static String smtp_host = "smtp.163.com";//网易
	private static String username = "chaifei410709108@163.com";//邮箱帐户
	private static String password = "chaifei32652177";//邮箱授权码
	
	private static String from = "chaifei410709108@163.com";//使用当前帐户
	public static String activeUrl = "http://localhost:9003/client-syht/customer_activeMail"; //消息平台接口
	
	public static void sendMail(String subject,String content,String to){
		//创建请求对象，并设置属性
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");
		//通过反射得到session
		Session session = Session.getInstance(props);
		//构造消息对象
		Message message = new MimeMessage(session);
		try{
			//设置发送者账号
			message.setFrom(new InternetAddress(from));
			//发送到的地址
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			//标题
			message.setSubject(subject);
			//设置中文编码
			message.setContent(content,"text/html;charset=utf-8");
			//设置传输参数
			Transport transport = session.getTransport();
			transport.connect(smtp_host,username,password);
			transport.sendMessage(message, message.getAllRecipients());
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}
}
