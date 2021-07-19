package com.example.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.quartz.dao.QuartzJobDao;
import com.example.quartz.dao.QuartzLogDao;
import com.example.quartz.domain.QuartzJob;
import com.example.quartz.domain.QuartzLog;
import com.example.quartz.exception.BadRequestException;
import com.example.quartz.service.QuartzJobService;
import com.example.quartz.service.dto.JobQueryCriteria;
import com.example.quartz.utils.quartz.QuartzManageUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author: sujing
 * @version: 1.0.0
 * @ClassName: QuartzJobServiceImpl.java
 * @date: 2021/6/30 15:51
 */
@Service
public class QuartzJobServiceImpl extends ServiceImpl<QuartzJobDao, QuartzJob> implements QuartzJobService {

    @Autowired
    private QuartzManageUtil quartzManage;

    @Autowired
    private QuartzJobDao quartzJobDao;

    @Autowired
    private QuartzLogDao quartzLogDao;

    @Override
    public IPage<QuartzJob> queryAll(JobQueryCriteria criteria) {
        String jobName = criteria.getJobName();
        Boolean isSuccess = criteria.getIsSuccess();

        QueryWrapper<QuartzJob> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(jobName)) {
            wrapper.eq("job_name", jobName);
        }
        if (null != isSuccess) {
            wrapper.eq("is_success", isSuccess);
        }
        Page<QuartzJob> page = new Page<>(criteria.getPageNum(), criteria.getPageSize());
        return quartzJobDao.selectPage(page, wrapper);
    }

    @Override
    public IPage<QuartzLog> queryAllLog(JobQueryCriteria criteria) {
        String jobName = criteria.getJobName();
        Boolean isSuccess = criteria.getIsSuccess();

        QueryWrapper<QuartzLog> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(jobName)) {
            wrapper.eq("job_name", jobName);
        }
        if (null != isSuccess) {
            wrapper.eq("is_success", isSuccess);
        }
        IPage<QuartzLog> page = new Page<>(criteria.getPageNum(), criteria.getPageSize());
        return quartzLogDao.selectPage(page, wrapper);
    }

    @Override
    public QuartzJob findById(Long id) {
        return quartzJobDao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(QuartzJob resources) {
        if (!CronExpression.isValidExpression(resources.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }
        this.save(resources);
        quartzManage.addJob(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(QuartzJob resources) {
        if (!CronExpression.isValidExpression(resources.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }
        this.save(resources);
        quartzManage.updateJobCron(resources);
    }

    @Override
    public void updateIsPause(QuartzJob quartzJob) {
        if (quartzJob.getIsPause()) {
            quartzManage.resumeJob(quartzJob);
            quartzJob.setIsPause(false);
        } else {
            quartzManage.pauseJob(quartzJob);
            quartzJob.setIsPause(true);
        }
        this.save(quartzJob);
    }

    @Override
    public void execution(QuartzJob quartzJob) {
        quartzManage.runJobNow(quartzJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            QuartzJob quartzJob = quartzJobDao.selectById(id);
            quartzManage.deleteJob(quartzJob);
            quartzJobDao.deleteById(id);
        }
    }
}
