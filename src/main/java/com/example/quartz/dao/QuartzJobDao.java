package com.example.quartz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.quartz.domain.QuartzJob;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartzJobDao extends BaseMapper<QuartzJob> {

    @Select("select * from sys_quartz_job where is_pause != 1")
    List<QuartzJob> findByIsPauseIsFalse();
}
