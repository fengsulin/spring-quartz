package com.lin.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.quartz.entity.QuartzJob;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface QuartzMapper extends BaseMapper<QuartzJob> {
    /**
     * 查询所有调度任务
     * @return
     */
    @Select("SELECT * FROM quartz_job")
    public List<QuartzJob> selectJobAll();

    /**
     * 根据调度Id查询调度任务信息
     * @param jobId
     * @return
     */
    @Select("SELECT * FROM quartz_job WHERE job_id = #{jobId}")
    public QuartzJob selectJobById(Long jobId);

    /**
     * 根据调度Id删除调度任务信息
     * @param jobId
     * @return
     */
    @Delete("DELETE FROM quartz_job WHERE job_id = #{jobId}")
    public int deleteJobById(Long jobId);

    @Update("UPDATE quartz_job SET status = #{status} WHERE job_id = #{jobId}")
    public int changeStatus(Long jobId,String status);
}
