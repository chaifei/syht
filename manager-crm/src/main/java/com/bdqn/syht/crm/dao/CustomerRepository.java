package com.bdqn.syht.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bdqn.syht.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	public List<Customer> findByFixedAreaIdIsNull();

	public List<Customer> findByFixedAreaId(String fixedAreaId);

	@Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
	@Modifying
	public void clearFixedAreaId(String fixedAreaId);

	@Query("update Customer set fixedAreaId = ? where id = ?")
	@Modifying
	public void updateFixedAreaId(String fixedAreaId, Integer id);

	public Customer findByTelephone(String telephone);

	@Query("update Customer set type=1 where telephone= ?")
	@Modifying
	public void updateType(String telephone);
	
	@Query("select fixedAreaId from Customer where address=?")
	public String findFixedAreaIdByAddress(String address);

	public Customer findByTelephoneAndPassword(String telephone, String password);

	@Query("from Customer where fixedAreaId =?")
	public Customer associationFixedareaCustomersFindOne(String fixedAreaId);

}
