package com.bdqn.syht.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.bdqn.syht.service.PromotionService;
/**
 * 定时设置宣传任务状态
 * @author 柴飞
 * 2018年4月27日 10:19:39
 */
public class PromotionJob implements Job{
	
	@Autowired
	private PromotionService promotionService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//每分钟执行一次,当前时间大于过期时间,设置过期状态为‘2’已结束
		promotionService.updateStatus(new Date());
	}

}
