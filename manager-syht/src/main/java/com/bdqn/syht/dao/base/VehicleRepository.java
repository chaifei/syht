package com.bdqn.syht.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bdqn.syht.pojo.base.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer>{
	
}
