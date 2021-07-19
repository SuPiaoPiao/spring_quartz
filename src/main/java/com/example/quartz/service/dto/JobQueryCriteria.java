package com.example.quartz.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Zheng Jie
 * @date 2019-6-4 10:33:02
 */
@Data
public class JobQueryCriteria {
    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "状态")
    private Boolean isSuccess;

    @ApiModelProperty(value = "当前页")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = 10;
}
