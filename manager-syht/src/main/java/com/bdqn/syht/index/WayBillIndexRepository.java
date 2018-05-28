package com.bdqn.syht.index;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bdqn.syht.pojo.delivery.WayBill;

//运单elasticsearch
public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill, Integer>{

}
