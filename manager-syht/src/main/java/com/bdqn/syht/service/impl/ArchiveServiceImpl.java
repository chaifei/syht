package com.bdqn.syht.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdqn.syht.dao.base.ArchiveRepository;
import com.bdqn.syht.pojo.base.Archive;
import com.bdqn.syht.service.ArchiveService;

/**
 * 基本档案业务逻辑接口实现类
 */
@Service
@Transactional//事务
public class ArchiveServiceImpl implements ArchiveService{
	//dao层注入
	@Autowired
	private ArchiveRepository archiveRepository;
	
	@Override
	public Page<Archive> findPageData(Pageable pageable) {
		return archiveRepository.findAll(pageable);
	}

	@Override
	public void saveArchive(Archive archive) {
		archiveRepository.save(archive);
	}

	@Override
	public List<Archive> findArchiveList() {
		return archiveRepository.findAll();
	}

}
