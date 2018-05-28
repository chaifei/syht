package com.bdqn.syht.pojo.transit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 配送信息
 * 
 * 当货物到达网点后,需要指定快递员进行配送
 */
@Entity
@Table(name = "T_DELIVERY_INFO")
public class DeliveryInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_ID")
	private Integer id;

	@Column(name = "C_COURIER_NUM")
	private String courierNum; //派送员工号

	@Column(name = "C_COURIER_NAME")
	private String courierName; //派送员姓名

	@Column(name = "C_DESCRIPTION")
	private String description; // 描述

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCourierNum() {
		return courierNum;
	}

	public void setCourierNum(String courierNum) {
		this.courierNum = courierNum;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
