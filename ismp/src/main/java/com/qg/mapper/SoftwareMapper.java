package com.qg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qg.domain.Software;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SoftwareMapper extends BaseMapper<Software> {
    // 按 id 降序排列，并限制返回 10 条
    @Select("SELECT * FROM software WHERE status != #{status} ORDER BY id DESC LIMIT 10")
    List<Software> selectTop10ByStatusOrderByIdDesc(@Param("status") int status);

    @Select("SELECT * FROM software WHERE status != #{status} AND type = #{type} ORDER BY id DESC LIMIT 10")
    List<Software> selectTop10ByStatusAndTypeOrderByIdDesc(@Param("status") int status, @Param("type") String type);

    @Select("SELECT * FROM software WHERE status != #{status} AND type = #{type} ORDER BY id DESC ")
    List<Software> selectByStatusAndTypeOrderByIdDesc(@Param("status") int status, @Param("type") String type);

    @Update("UPDATE software SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") int status);

    @Select("SELECT * FROM software WHERE name = #{name} ")
    List<Software> selectSoftwareVersion(@Param("name") String name);

    /**
     * 获取用户的所有已预约软件
     *
     * @param userId
     * @return
     */
    @Select("SELECT s.* FROM software s " +
            "JOIN equipment e ON s.id = e.software_id " +
            "WHERE e.user_id = #{userId} AND e.status = 0 " +
            "GROUP BY e.id " +
            "ORDER BY published_time")
    IPage<Software> getAllOrderSoftware(Page<Software> page, @Param("userId") Long userId);

    /**
     * 获取用户的所有已购买软件
     *
     * @param userId
     * @return
     */
    @Select("SELECT s.* FROM software s " +
            "JOIN equipment e ON s.id = e.software_id " +
            "WHERE e.user_id = #{userId} AND e.status = 1 " +
            "GROUP BY e.id " +
            "ORDER BY published_time")
    IPage<Software> selectPurchased(Page<Software> page, @Param("userId") Long userId);

    /**
     * 管理员查看所有用户的预约软件
     *
     * @return
     */
    @Select("SELECT s.* FROM software s " +
            "JOIN equipment e ON s.id = e.software_id " +
            "WHERE e.status = 0 " +
            "GROUP BY e.id " +
            "ORDER BY published_time")
    IPage<Software> adminGetAllOrderSoftware(Page<Software> page);

    /**
     * 根据软件名称模糊匹配
     *
     * @param name
     * @return
     */
    @Select("SELECT * FROM software WHERE name LIKE CONCAT('%', #{name}, '%') AND is_deleted = 0")
    List<Software> getSoftwareByFuzzyName(@Param("name") String name);
}
