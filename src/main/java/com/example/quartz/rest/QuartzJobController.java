package com.example.quartz.rest;

import com.example.quartz.domain.QuartzJob;
import com.example.quartz.exception.BadRequestException;
import com.example.quartz.service.QuartzJobService;
import com.example.quartz.service.dto.JobQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author: sujing
 * @version: 1.0.0
 * @ClassName: QuartzJobController.java
 * @date: 2021/6/30 15:51
 */
@RestController
@RequestMapping("/quartzJob")
@Api(tags = "定时任务管理")
public class QuartzJobController {

    @Autowired
    private QuartzJobService quartzJobService;

    @ApiOperation("查询定时任务")
    @GetMapping(value = "/query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true),
            @ApiImplicitParam(name = "jobName", value = "任务名称", required = true)
    })
    public ResponseEntity<Object> query(JobQueryCriteria criteria) {
        return new ResponseEntity<>(quartzJobService.queryAll(criteria), HttpStatus.OK);
    }

    @ApiOperation("新增定时任务")
    @PostMapping(value = "/create")
    public ResponseEntity<Object> create(@Validated @RequestBody QuartzJob resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new quartzJob cannot already have an ID");
        }
        quartzJobService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改定时任务")
    @PutMapping(value = "/update")
    public ResponseEntity<Object> update(@Validated @RequestBody QuartzJob resources) {
        quartzJobService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("更改定时任务状态")
    @PutMapping(value = "/updateStatus")
    public ResponseEntity<Object> update(@PathVariable Long id) {
        quartzJobService.updateIsPause(quartzJobService.findById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("执行定时任务")
    @PutMapping(value = "/execJob")
    public ResponseEntity<Object> execution(@PathVariable Long id) {
        quartzJobService.execution(quartzJobService.findById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除定时任务")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        quartzJobService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("查询任务执行日志")
    @GetMapping(value = "/logs")
    public ResponseEntity<Object> queryJobLog(JobQueryCriteria criteria) {
        return new ResponseEntity<>(quartzJobService.queryAllLog(criteria), HttpStatus.OK);
    }
}
