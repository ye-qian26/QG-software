package com.qg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qg.domain.Software;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SoftwareMapper extends BaseMapper<Software> {
    // 按 id 降序排列，并限制返回 8 条
    @Select("SELECT t1.* FROM software t1 " +
            "LEFT JOIN software t2 ON t1.name = t2.name AND t1.id < t2.id AND t1.status != #{status} AND t2.status != #{status} " +
            "WHERE t2.id IS NULL AND t1.status != #{status} " +
            "ORDER BY t1.id DESC " +
            "LIMIT 5")
    List<Software> selectTop10LatestPerNameExcludingStatus(@Param("status") int status);

    @Select("SELECT t1.* FROM software t1 " +
            "LEFT JOIN software t2 ON t1.name = t2.name AND t1.id < t2.id " +
            "AND t1.type = #{type} AND t2.type = #{type} " +  // 添加type条件
            "WHERE t2.id IS NULL " +
            "AND t1.status != #{status} " +  // 添加status条件
            "AND t1.type = #{type} " +      // 再次确保type匹配
            "ORDER BY t1.id DESC " +        // 按id降序
            "LIMIT 8")                      // 限制8条
    List<Software> selectTop8LatestPerName(
            @Param("status") int status,
            @Param("type") String type);

    @Select("SELECT * FROM software WHERE status != #{status} AND type = #{type} ORDER BY id DESC ")
    List<Software> selectByStatusAndTypeOrderByIdDesc(@Param("status") int status,@Param("type") String type);

    @Update("UPDATE software SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") int status);

    @Select("SELECT * FROM software WHERE name = #{name} ")
    List<Software> selectSoftwareVersion(@Param("name") String name);

    //同名软件只返回最新的版本
    @Select("SELECT t1.* FROM software t1 " +
            "LEFT JOIN software t2 " +
            "ON t1.name = t2.name AND t1.id < t2.id AND t1.author_id = t2.author_id " +
            "WHERE t2.id IS NULL AND t1.author_id = #{author_id}")
    List<Software> selectLastRecordsPerName(@Param("author_id") Long author_id);
}
