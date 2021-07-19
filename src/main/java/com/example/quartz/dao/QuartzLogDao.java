package com.example.quartz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.quartz.domain.QuartzLog;
import org.springframework.stereotype.Repository;

@Repository
public interface QuartzLogDao extends BaseMapper<QuartzLog> {
}
