package com.example.quartz.utils.quartz;

import com.example.quartz.dao.QuartzLogDao;
import com.example.quartz.domain.QuartzJob;
import com.example.quartz.domain.QuartzLog;
import com.example.quartz.service.QuartzJobService;
import com.example.quartz.thread.ThreadPoolExecutorUtil;
import com.example.quartz.utils.ThrowableUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: sujing
 * @version: 1.0.0
 * @ClassName: ExecutionJobTask.java
 * @date: 2021/6/30 11:09
 */
@Async
@SuppressWarnings({"unchecked", "all"})
public class ExecutionJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionJob.class);

    private final static ThreadPoolExecutor EXECUTOR = ThreadPoolExecutorUtil.getPoll();

    @Autowired
    private QuartzLogDao quartzLogDao;

    @Autowired
    private QuartzJobService quartzJobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //get JOB_KEY
        QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(QuartzJob.JOB_KEY);

        QuartzLog log = new QuartzLog();
        log.setJobName(quartzJob.getJobName());
        log.setBeanName(quartzJob.getBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            LOG.info("-----------------------任务开始执行，任务名称：" + quartzJob.getJobName());

            //多线程实例化要执行的定时任务
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
            Future<?> future = EXECUTOR.submit(task);

            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            log.setIsSuccess(true);
            LOG.info("-----------------------任务执行完毕，任务名称：" + quartzJob.getJobName() + ", 执行时间：" + times + "毫秒");

        } catch (Exception e) {
            LOG.info("-----------------------任务执行失败，任务名称：" + quartzJob.getJobName());
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            log.setIsSuccess(false);
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));

            // 任务如果失败了则暂停
            if (quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()) {
                quartzJob.setIsPause(false);
                quartzJobService.updateIsPause(quartzJob);//更新状态
            }
        } finally {
            quartzLogDao.insert(log);
        }
    }
}
