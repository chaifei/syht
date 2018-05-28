package com.bdqn.syht.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bdqn.syht.pojo.base.SubArchive;

/**
 * 子档案业务逻辑接口
 */
public interface SubArchiveService {
	//返回子档案分页数据
	public Page<SubArchive> findPageData(Pageable pageable);
	//保存子档案
	public void saveSubArchive(SubArchive archive);
}
