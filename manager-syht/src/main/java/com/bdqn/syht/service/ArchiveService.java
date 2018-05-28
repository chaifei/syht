package com.bdqn.syht.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.pojo.base.Archive;

/**
 * 基本档案业务逻辑接口
 */
public interface ArchiveService {
	//返回分页数据
	public Page<Archive> findPageData(Pageable pageable);
	//保存基本档案
	public void saveArchive(Archive archive);
	//查询所有,用于查询上机编码
	public List<Archive> findArchiveList();
}
