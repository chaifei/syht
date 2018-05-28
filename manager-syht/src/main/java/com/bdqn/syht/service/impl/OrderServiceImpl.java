package com.bdqn.syht.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.AreaRepository;
import com.bdqn.syht.dao.base.FixedAreaRepository;
import com.bdqn.syht.dao.base.OrderRepository;
import com.bdqn.syht.dao.base.WorkBillRepository;
import com.bdqn.syht.pojo.base.Area;
import com.bdqn.syht.pojo.base.Courier;
import com.bdqn.syht.pojo.base.FixedArea;
import com.bdqn.syht.pojo.base.SubArea;
import com.bdqn.syht.pojo.delivery.Order;
import com.bdqn.syht.pojo.delivery.WorkBill;
import com.bdqn.syht.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
	
	@Autowired  
    private FixedAreaRepository fixedAreaRepository;  
  
    @Autowired  
    private OrderRepository orderRepository;  
  
    @Autowired  
    private AreaRepository areaRepository;  
  
    @Autowired  
    private WorkBillRepository workBillRepository;  
  
    @Autowired  
    @Qualifier("jmsQueueTemplate")  
    private JmsTemplate jmsTemplate;
    
    //保存订单,指派快递员，自动分单，人工分单
	@Override
	public void saveOrder(Order order) {
		order.setOrderNum(UUID.randomUUID().toString());//设置订单号
		order.setOrderTime(new Date());//设置下单时间
		order.setStatus("1");//待取件
		// Area没有id是瞬时态 不能保存到数据库 要封装后才能添加  
        // 1. 获取到寄件人和收件人的省市区信息  
        // 寄件人 省市区 
		Area area = order.getSendArea();
		Area persistArea = areaRepository.findByProvinceAndCityAndDistrict(area.getProvince(), area.getCity(), area.getDistrict());
		// 收件人 省市区
		Area rexArea = order.getRecArea();
		Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(rexArea.getProvince(), rexArea.getCity(), rexArea.getDistrict());
		// 二次封装
		order.setSendArea(persistArea);
		order.setRecArea(persistRecArea);
		// 第一种情况：假如寄件人的地址和CRM地址库里面的地址完全匹配；则分配快递员，生成工单
		
		// 自动分单逻辑，基于crm地址库完全匹配，获取定区，匹配快递员
		String fixedAreaId = WebClient.create("http://localhost:9002/manager-crm/webServices/customerService/customer/findFixedAreaIdByAddress?address="  
                + order.getSendAddress()).accept(MediaType.APPLICATION_JSON).get(String.class);
		// 第二种情况：只有定区的信息，则根据定区来匹配快递员，匹配快递员成功的话就进行分单，生成工单
		if(fixedAreaId!=null){
			//如果有这个定区，定区id不为空，根据id寻找定区
			//这个定区肯定关联了一个快递员
			FixedArea fixedArea = fixedAreaRepository.findOne(Integer.parseInt(fixedAreaId));
			Courier courier = fixedArea.getCouriers().iterator().next();
			if(courier != null){
				//如果关联了快递员，自动分单成功
				saveOrder(order,courier);
				//生成工单 发送短信给快递员
				generateWorkBill(order);
				return;
			}
		}
		//第三种情况：有分区关键字的信息，则通过分区关键字，找到分区，关联上定区，找到快递员，进行分单，生成工单
		//自动分单的逻辑，通过省市区，查询分区关键字，匹配地址，基于分区实现自动分单
		for(SubArea subArea : persistArea.getSubareas()){//拿到寄件人的分区地址
			//当前客户的下单地址，是否包含分区关键字
			if(order.getSendAddress().contains(subArea.getKeyWords())){
				//包含的话，找到分区，找到定区，找到快递员
				Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
				if(iterator.hasNext()){
					Courier courier = iterator.next();
					if(courier != null){
						//自动分单成功
						saveOrder(order,courier);
						//生成工单发送短信
						generateWorkBill(order);
						return;
					}
				}
			}
		}
		//第四中情况：能匹配到分区辅助关键字信息，则通过辅助关键字，找到分区，关联上定区找到快递员进行分单，生成工单
		for(SubArea subArea : persistArea.getSubareas()){
			// 当前客户的下单地址是否包含分区辅助关键字  
            if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {  
                // 找到分区,找到定区,找到快递员  
                Iterator<Courier> iterator = subArea.getFixedArea()  
                        .getCouriers().iterator();  
                if (iterator.hasNext()) {  
                    Courier courier = iterator.next();  
                    if (courier != null) {  
                        // 自动分单成功  
                        System.out.println("自动分单成功");  
                        // 生成工单  
                        generateWorkBill(order);  
                        return;  
                    }  
                }  
            } 
		}
		//进入人工分单
		order.setOrderType("2");
		orderRepository.save(order);
	}
	
	//生成工单 发短信给快递员
	public void generateWorkBill(final Order order){
		//生成工单
		WorkBill workBill = new WorkBill();
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		final String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber);//短信序号
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		workBillRepository.save(workBill);
		
		//发送短信
		//调用MQ服务,发送一条信息
		jmsTemplate.send("bos_sms",new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException{
				//封装短信
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				mapMessage.setString("msg", "短信序号："+smsNumber+"，取件地址："+order.getSendAddress()
				+"，联系人："+order.getSendName()+"，手机："+order.getSendMobile()
				+"，快递员捎话："+order.getSendMobileMsg());
				return mapMessage;
			}
		});
		//修改工单状态
		workBill.setPickstate("已通知");
	}
	
	//自动分单保存
	private void saveOrder(Order order,Courier courier){
		//将快递员关联到订单上
		order.setCourier(courier);
		//设置自动分单
		order.setOrderType("1");
		//保存订单
		orderRepository.save(order);
	}

	//分页查询
	@Override
	public Page<Order> findPageData(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public Order findByOrderId(int orderId) {
		return orderRepository.findOne(orderId);
	}

	@Override
	public void updateOrderDispatcher(Order order) {
		orderRepository.save(order);		
	}

	@Override
	public Order findByOrderNum(String orderNum) {
		return orderRepository.findByOrderNum(orderNum);
	}
}
