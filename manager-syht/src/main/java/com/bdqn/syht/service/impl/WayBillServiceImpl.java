package com.bdqn.syht.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.WayBillRepository;
import com.bdqn.syht.index.WayBillIndexRepository;
import com.bdqn.syht.pojo.delivery.WayBill;
import com.bdqn.syht.service.WayBillService;

/**
 * 运单业务逻辑层实现类
 * @author chaifei
 * @time 2018年4月29日 下午2:00:09
 *
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService{

	//dao层注入
	@Autowired
	private WayBillRepository wayBillRepository;
	
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public Page<WayBill> findPageData(WayBill wayBill, Pageable pageable) {
		// 判断WayBill 中条件是否存在
		if (StringUtils.isBlank(wayBill.getWayBillNum())
				&& StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress())
				&& StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
			// 无条件查询 、查询数据库
			return wayBillRepository.findAll(pageable);
		} else {
			// 查询条件
			// must 条件必须成立 and
			// must not 条件必须不成立 not
			// should 条件可以成立 or
			BoolQueryBuilder query = new BoolQueryBuilder(); // 布尔查询 ，多条件组合查询
			// 向组合查询对象添加条件
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				// 运单号查询
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
						wayBill.getWayBillNum()).field("wayBillNum")
						.defaultOperator(Operator.AND);
				query.must(queryStringQueryBuilder);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				// 发货地 模糊查询
				// 情况一： 输入"北" 是查询词条一部分， 使用模糊匹配词条查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder(
						"sendAddress", "*" + wayBill.getSendAddress() + "*");

				// 情况二： 输入"北京市海淀区" 是多个词条组合，进行分词后 每个词条匹配查询
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
						wayBill.getSendAddress()).field("sendAddress")
						.defaultOperator(Operator.AND);

				// 两种情况取or关系
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(wildcardQuery);
				boolQueryBuilder.should(queryStringQueryBuilder);

				query.must(boolQueryBuilder);
			}
			if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
				// 收货地 模糊查询
				// 情况一： 输入"北" 是查询词条一部分， 使用模糊匹配词条查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder(
						"recAddress", "*" + wayBill.getRecAddress() + "*");

				// 情况二： 输入"北京市海淀区" 是多个词条组合，进行分词后 每个词条匹配查询
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
						wayBill.getRecAddress()).field("recAddress")
						.defaultOperator(Operator.AND);

				// 两种情况取or关系
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(wildcardQuery);
				boolQueryBuilder.should(queryStringQueryBuilder);

				query.must(boolQueryBuilder);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型 等值查询
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
						wayBill.getSendProNum()).field("sendProNum")
						.defaultOperator(Operator.AND);
				query.must(queryStringQueryBuilder);
			}
			if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
				// 签收状态查询
				QueryBuilder termQuery = new TermQueryBuilder("signStatus",
						wayBill.getSendProNum());
				query.must(termQuery);
			}

			SearchQuery searchQuery = new NativeSearchQuery(query);
			searchQuery.setPageable(pageable); // 分页效果
			// 有条件查询 、查询索引库
			return wayBillIndexRepository.search(searchQuery);
		}
	}
	
	//保存
	@Override
	public int save(WayBill wayBill) {
		
		// 判断运单号是否存在
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
		if (persistWayBill == null) {
			// 运单不存在
			int i = wayBillRepository.save(wayBill).getId();
			wayBillIndexRepository.save(wayBill);
			return i;
		} else {
			// 运单存在
			Integer id = persistWayBill.getId();
			try {
				BeanUtils.copyProperties(persistWayBill, wayBill);
				//把后面对象的数据,覆盖给前面的对象
				//防止标准运单ID被覆盖,所以要在复制其他字段完成之后,再重新赋值id.
				persistWayBill.setId(id);//保证运单在复制后的id是同一个,就是保证当前运单是持久态对象
				wayBillIndexRepository.save(persistWayBill);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			} 
			return id;
		}
	}

	@Override
	public WayBill findByWayBillId(int wayBillId) {
		return wayBillRepository.findOne(wayBillId);
	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}

	//数据库数据与索引库数据保持一致,写一个方法用于定时刷新保存索引库
	@Override
	public void syncIndex() {
		List<WayBill> wayBills = wayBillRepository.findAll();
		wayBillIndexRepository.save(wayBills);
	}


	@Override
	public List<WayBill> findWayBills(WayBill model) {
		return wayBillRepository.findAll();
	}

	@Override
	public Page<WayBill> findPageDatas(Pageable pageable) {
		return wayBillRepository.findAll(pageable);
	}
}
