package com.example.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.quartz.domain.QuartzJob;
import com.example.quartz.domain.QuartzLog;
import com.example.quartz.service.dto.JobQueryCriteria;

import java.util.Set;

public interface QuartzJobService extends IService<QuartzJob> {

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @return
     */
    IPage<QuartzJob> queryAll(JobQueryCriteria criteria);

    /**
     * 分页查询日志
     *
     * @param criteria 条件
     * @return
     */
    IPage<QuartzLog> queryAllLog(JobQueryCriteria criteria);

    /**
     * 创建
     *
     * @param resources
     */
    void create(QuartzJob resources);

    /**
     * 编辑
     *
     * @param resources
     */
    void update(QuartzJob resources);

    /**
     * 删除任务
     *
     * @param ids
     */
    void delete(Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    QuartzJob findById(Long id);

    /**
     * 更改定时任务状态
     *
     * @param quartzJob
     */
    void updateIsPause(QuartzJob quartzJob);

    /**
     * 立即执行定时任务
     *
     * @param quartzJob
     */
    void execution(QuartzJob quartzJob);
}
