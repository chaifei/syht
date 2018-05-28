package com.bdqn.syht.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.VehicleRepository;
import com.bdqn.syht.pojo.base.Vehicle;
import com.bdqn.syht.service.VehicleService;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService{

	@Autowired
	private VehicleRepository vehicleRepository;
	

	@Override
	public Page<Vehicle> findPageData(Pageable pageable) {
		return vehicleRepository.findAll(pageable);
	}
	
	@Override
	public void saveVehicle(Vehicle vehicle) {
		vehicleRepository.save(vehicle);
	}

	@Override
	public void delBatch(String[] idArray) {
		//调用DAO实现 update修改操作,将deltag 修改为1
		for (String idstr : idArray) {
			Integer id = Integer.parseInt(idstr);
			vehicleRepository.delete(id);
		}
	}
}
