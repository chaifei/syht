package com.bdqn.syht.mq;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import com.bdqn.syht.utils.HttpSendPhoneUtils;

@Service("smsConsumer")
public class SmsConsumer implements MessageListener{//实现消息监听，重写方法

	@Override
	public void onMessage(Message message) {
		//将参数转换类型
		MapMessage mapMessage = (MapMessage)message;
		//调用发短信
		try{
			//接收返回值
			String result = HttpSendPhoneUtils.sendMessage(mapMessage.getString("telephone"));
			if("10000".equals(result)){
				//发送成功,打印手机号和验证码
				System.out.println("发送短信成功，手机号："+mapMessage.getString("telephone")+",验证码："+mapMessage.getString("code"));
				System.out.println("code");
			}else{
				//发送失败,抛出一个运行时异常
				throw new RuntimeException("短信发送失败,信息码："+result);
			}
		}catch(IOException|JMSException e){
			e.printStackTrace();
		}
	}
}
