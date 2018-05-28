package com.bdqn.syht.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.bdqn.syht.common.BaseAction;
import com.bdqn.syht.crm.domain.Customer;
import com.bdqn.syht.util.MailUtils;


@ParentPackage("json-default")
@Namespace("/*")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {
	//注入mq
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	/**发短信*/
	
	@Action(value="customer_sendSms")
	public String sendSms() throws IOException{
		//手机号保存在Customer对象
		//生成短信验证码
		String randomCode = RandomStringUtils.randomNumeric(4);
		//将短信验证码保存在session
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
		//打印验证码
		System.out.println("生成手机验证码为："+randomCode);
		//编辑短信内容
		final String msg = "尊敬的用户您好，本次获取的验证码为："+randomCode+",服务电话：400400400";
		//调用MQ服务,发送一条消息
		jmsTemplate.send("bos_sms",new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException{
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone",model.getTelephone());
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});
		return NONE;
	}
	
	//属性驱动
	private String checkcode;

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	
	//注入redis
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	/**短信验证，发送邮件*/
	
	@Action(value="customer_regist",results={@Result(name="success",type="redirect",location="signup-success.html"),
			@Result(name="input",type="redirect",location="signup.html")})
	public String regist(){
		//先校验短信验证码，如果不通过，调回注册页面
		//从session获取之前生成验证码
		String checkcodeSession = (String)ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
		if(checkcodeSession == null || !checkcodeSession.equals(checkcode)){
			System.out.println("短信验证码错误...");
			//短信验证码错误
			return INPUT;
		}
		//调用webService连接CRM 保存客户信息
		WebClient.create("http://localhost:9002/manager-crm/webServices/customerService/customer_regist")
		.type(MediaType.APPLICATION_JSON).post(model);
		//发送发送一封激活邮件
		//生成激活码
		String activecode = RandomStringUtils.randomNumeric(32);
		//将激活码保存到redis,设置24小时失效
		redisTemplate.opsForValue().set(model.getTelephone(), activecode,24,TimeUnit.HOURS);
		//调用MailUtils发送激活邮件
		String content = "尊敬的客户您好，请于24小时内，进行邮箱帐户的绑定，点击下面地址完成绑定：<br/><a href='"
				+ MailUtils.activeUrl + "?telephone=" + model.getTelephone()
				+ "&activecode=" + activecode + "'>速运快递邮箱绑定地址</a>";
		//调用工具类的方法发邮件
		MailUtils.sendMail("速运快递激活邮件", content, model.getEmail());
		return SUCCESS;
	}
	
	//属性驱动,消息验证码
	private String activecode;

	public void setActivecode(String activecode) {
		this.activecode = activecode;
	}
	
	/**账号激活*/
	
	@Action("customer_activeMail")
	public String activeMail() throws IOException{
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		//判断激活码是否有效
		String activecodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
		if(activecodeRedis == null || !activecodeRedis.equals(activecode)){
			//激活码无效
			ServletActionContext.getResponse().getWriter().println("激活码无效,请登录系统,重新绑定邮箱！");
		}else{
			//激活码有效
			//防止重复绑定
			//调用CRM webService 查询客户信息,判断是否已经绑定
			Customer customer = WebClient.create("http://localhost:9002/manager-crm/webServices/customerService/customer/telephone/"+model.getTelephone())
					.accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if(customer.getType()==null||customer.getType()!=1){
				//没有绑定,进行绑定
				WebClient.create(
						"http://localhost:9002/manager-crm/webServices/customerService/customer/updatetype/"
								+ model.getTelephone()).get();
				ServletActionContext.getResponse().getWriter().println("邮箱绑定成功");
			}else{
				//已经绑定过
				ServletActionContext.getResponse().getWriter().println("邮箱已经绑定过，无需重复绑定！");
			}
			//删除redis的激活码
			redisTemplate.delete(model.getTelephone());
		}
		return NONE;
	}
	
	/**登录*/
	@Action(value="customer_login",results={@Result(name="login",location="login.html",type="redirect"),
			@Result(name="success",location="index.html#/myhome",type="redirect")})
	public String login(){
		//调用webService接口登录返回用户对象
		Customer customer = WebClient.create("http://localhost:9002/manager-crm/webServices/customerService/customer/login?telephone="+model.getTelephone()+"&password="+model.getPassword())
				.accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if(customer == null){
			return LOGIN;
		}else{
			ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
			System.err.println(customer+">>>>>>>>>>>>>>>>>>>>");
			return SUCCESS;
		}
	}
}
