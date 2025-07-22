package com.qg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qg.domain.Software;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SoftwareMapper extends BaseMapper<Software> {
    // 按 id 降序排列，并限制返回 10 条
    @Select("SELECT * FROM software WHERE status = #{status} ORDER BY id DESC LIMIT 10")
    List<Software> selectTop10ByStatusOrderByIdDesc(@Param("status") int status);
}
