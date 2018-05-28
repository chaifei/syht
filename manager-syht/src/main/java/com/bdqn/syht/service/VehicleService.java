package com.bdqn.syht.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bdqn.syht.pojo.base.Vehicle;

public interface VehicleService {
	//查询分页
	public Page<Vehicle> findPageData(Pageable pageable);
	//添加班车
	public void saveVehicle(Vehicle vehicle);
	//删除班车
	public void delBatch(String[] idArray);
}
