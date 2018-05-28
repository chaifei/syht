package com.bdqn.syht.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSON;

public class HttpSendPhoneUtils {

	private HttpSendPhoneUtils(){}
	
	public static String sendMessage(String telephone) throws HttpException, IOException{
		//创建通信对象
		HttpClient client = new HttpClient();
		//提交地址
		PostMethod post = new PostMethod("https://way.jd.com/chuangxin/VerCodesms");
		//设置中文编码
		post.addRequestHeader("Contect-Type","application/x-www-form-urlencoded;charset=UTF-8");
		//设置接口需要的参数
		NameValuePair[] data = {
				new NameValuePair("mobile",telephone),
				new NameValuePair("content","【成都创信】验证码为：5873,欢迎注册平台！"),
				new NameValuePair("appkey","d64cf571f8cea207baca0fcdfb473d4b")
		};
		//传递参数
		post.setRequestBody(data);
		//提交
		client.executeMethod(post);
		//打印
		System.out.println(post.getStatusCode());//200
		//接收返回值
		String result = new String(post.getResponseBodyAsString().getBytes("GBK"));
		//对返回的json串解析成map集合
		Map maps = (Map)JSON.parse(result);
		//遍历集合
		for(Object map:maps.entrySet()){
			//判断参数的键是否为code
			if("code".equals(((Map.Entry)map).getKey())){
				//接收值
				String code = (String)((Map.Entry)map).getKey();
				//如果值为10000表示短信发送成功，否则抛出运行时异常给前台，给用户返回失败信息
				if("10000".equals(code)){
					return code;
				}else{
					throw new RuntimeException("发短信，异常~ 异常参数:"+code);
				}
			}
		}
		return null;
	}
}
