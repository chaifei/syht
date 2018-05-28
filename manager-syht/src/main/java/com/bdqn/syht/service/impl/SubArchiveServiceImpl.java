package com.bdqn.syht.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.SubArchiveRepository;
import com.bdqn.syht.pojo.base.SubArchive;
import com.bdqn.syht.service.SubArchiveService;

/**
 * 子档案业务逻辑接口实现类
 */
@Service
@Transactional//事务
public class SubArchiveServiceImpl implements SubArchiveService{
	//dao层注入
	@Autowired
	private SubArchiveRepository subArchiveRepository;
	
	@Override
	public Page<SubArchive> findPageData(Pageable pageable) {
		return subArchiveRepository.findAll(pageable);
	}

	@Override
	public void saveSubArchive(SubArchive archive) {
		subArchiveRepository.save(archive);
	}

}
