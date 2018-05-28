package com.bdqn.syht.pojo.transit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 签收信息
 * 
 * 客户签收货物信息
 */
@Entity
@Table(name = "T_SIGN_INFO")
public class SignInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_ID")
	private Integer id;  //签收ID

	@Column(name = "C_SIGN_NAME")
	private String signName; //签收人名称

	@Column(name = "C_SIGN_TIME")
	private Date signTime; //签收时间

	@Column(name = "C_SIGN_TYPE")
	private String signType; //签收类型

	@Column(name = "C_ERROR_REMARK")
	private String errorRemark; //异常描述

	@Column(name = "C_DESCRIPTION")
	private String description; // 描述

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getErrorRemark() {
		return errorRemark;
	}

	public void setErrorRemark(String errorRemark) {
		this.errorRemark = errorRemark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
