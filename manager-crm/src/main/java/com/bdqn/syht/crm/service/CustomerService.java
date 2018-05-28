package com.bdqn.syht.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.bdqn.syht.crm.domain.Customer;


public interface CustomerService {
	
		//查询所有未关联的用户列表
		@Path("/noassociationcustomers")
		@GET
		@Produces({ "application/xml", "application/json" })
		public List<Customer> findNoAssociationCustomers();
		
		// 查询所有已关联定区的客户列表
		@Path("/associationfixedareacustomers/{fixedareaid}")
		@GET
		@Produces({ "application/xml", "application/json" })
		public List<Customer> findHasAssociationFixedAreaCustomers(
				@PathParam("fixedareaid") String fixedAreaId);
		
		
		// 将客户关联到定区上 ， 将所有客户id 拼成字符串 1,2,3
		@Path("/associationcustomerstofixedarea")
		@PUT
		public void associationCustomersToFixedArea(
				@QueryParam("customerIdStr") String customerIdStr,
				@QueryParam("fixedAreaId") String fixedAreaId);
		
		//查询已关联定区的单个客户
		@Path("/associationFixedareaCustomersFindOne/{fixedareaid}")
		@GET
		@Produces({ "application/xml", "application/json" })
		public Customer associationFixedareaCustomersFindOne(
				@PathParam("fixedareaid") String fixedAreaId);
		
		//用户注册后的保存功能
		@Path("/customer_regist")
		@POST
		@Consumes({"application/xml","application/json"})
		public void regist(Customer customer);
		
		
		//通过手机号,查询用户是否存在
		@Path("/customer/telephone/{telephone}")
		@GET
		@Consumes({"application/xml","application/json"})
		public Customer findByTelephone(@PathParam("telephone") String telephone);
		
		//绑定用户
		@Path("/customer/updatetype/{telephone}")
		@GET
		public void updateType(@PathParam("telephone") String telephone);
		
		//通过地址查找客户关联的定区ID
		@Path("/customer/findFixedAreaIdByAddress")
		@GET
		public String findFixedAreaIdByAddress(@QueryParam("address")String address);
		
		//用户登陆
		@Path("/customer/login")
		@GET
		@Consumes({"application/json","application/xml"})
		@Produces({"application/json","application/xml"})
		public Customer login(@QueryParam("telephone")String telephone,@QueryParam("password")String password);

}
