package com.bdqn.syht.pojo.transit;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;

import com.bdqn.syht.pojo.delivery.WayBill;


/**
 * @description: 运输配送信息
 * 
 * 整个物流的配送信息
 */
@Entity
@Table(name = "T_TRANSIT_INFO")
public class TransitInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_ID")
	private Integer id; //配送信息ID

	@OneToOne
	@JoinColumn(name = "C_WAYBILL_ID")
	private WayBill wayBill; //运单

	@OneToMany(fetch=FetchType.EAGER) //出入库信息
	@JoinColumn(name = "C_TRANSIT_INFO_ID")
	@OrderColumn(name = "C_IN_OUT_INDEX")//关注集合对象的顺序,保证顺序在数据库表建立索引数据表,存放list集合下标
	private List<InOutStorageInfo> inOutStorageInfos = new ArrayList<InOutStorageInfo>();
	
	@OneToOne
	@JoinColumn(name = "C_DELIVERY_INFO_ID")
	private DeliveryInfo deliveryInfo;

	@OneToOne
	@JoinColumn(name = "C_SIGN_INFO_ID")
	private SignInfo signInfo;

	@Column(name = "C_STATUS")
	// 出入库中转、到达网点、开始配送、正常签收、异常
	private String status;

	@Column(name = "C_OUTLET_ADDRESS")
	private String outletAddress;

	
	@Transient
	public String getTransferInfo() {
		StringBuffer buffer = new StringBuffer();
		// 添加出入库信息
		for (InOutStorageInfo inOutStorageInfo : inOutStorageInfos) {
			buffer.append(inOutStorageInfo.getDescription() + "<br/>");
		}
		// 添加配送信息
		if (deliveryInfo != null) {
			buffer.append(deliveryInfo.getDescription() + "<br/>");
		}
		// 添加签收信息
		if (signInfo != null) {
			buffer.append(signInfo.getDescription() + "<br/>");
		}
		return buffer.toString();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WayBill getWayBill() {
		return wayBill;
	}

	public void setWayBill(WayBill wayBill) {
		this.wayBill = wayBill;
	}

	public List<InOutStorageInfo> getInOutStorageInfos() {
		return inOutStorageInfos;
	}

	public void setInOutStorageInfos(List<InOutStorageInfo> inOutStorageInfos) {
		this.inOutStorageInfos = inOutStorageInfos;
	}

	public DeliveryInfo getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	public SignInfo getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(SignInfo signInfo) {
		this.signInfo = signInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOutletAddress() {
		return outletAddress;
	}

	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}

}
