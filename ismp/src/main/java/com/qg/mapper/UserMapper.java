package com.qg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qg.domain.User;

import com.qg.vo.AdminManageUserVO;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 更新用户头像
     *
     * @param userId
     * @param avatarUrl
     * @return
     */
    @Update("UPDATE `user` SET `avatar` = #{avatarUrl} WHERE id = #{userId}")
    Integer updateAvatar(@Param("userId") Long userId, @Param("avatarUrl") String avatarUrl);


    /**
     * 获取所有用户信息的 List集合
     * @return
     */
//    @Select("SELECT u.id, b.id as banId, u.name, u.avatar, u.role, " +
//
//            // 判断用户当前时间是否处于冻结期内
//            "CASE WHEN b.id IS NOT NULL " +
//            "     AND b.end_time > NOW() " +
//            "     AND b.is_deleted = 0 " +
//            "     THEN 0 " +        // 0表示被冻结
//            "     ELSE 1 " +        // 1表示正常
//            "END AS status, " +
//
//            "b.start_time as startTime, " +
//            "b.end_time as endTime, " +
//            "b.is_deleted AS isDeleted " +
//            "FROM user u " +
//            "LEFT JOIN ban b ON u.id = b.user_id " +
//            "GROUP BY u.id, b.id, u.name")
    @Select("SELECT u.id, " +
            "(SELECT id FROM ban WHERE user_id = u.id AND is_deleted = 0 ORDER BY end_time DESC LIMIT 1) as banId, " +
            "u.name, u.avatar, u.role, " +
            "CASE WHEN EXISTS (SELECT 1 FROM ban WHERE user_id = u.id AND is_deleted = 0 AND end_time > NOW()) " +
            "     THEN 0 " +
            "     ELSE 1 " +
            "END AS status, " +
            "(SELECT start_time FROM ban WHERE user_id = u.id AND is_deleted = 0 ORDER BY end_time DESC LIMIT 1) as startTime, " +
            "(SELECT end_time FROM ban WHERE user_id = u.id AND is_deleted = 0 ORDER BY end_time DESC LIMIT 1) as endTime, " +
            "(SELECT is_deleted FROM ban WHERE user_id = u.id AND is_deleted = 0 ORDER BY end_time DESC LIMIT 1) as isDeleted " +
            "FROM user u")
    List<AdminManageUserVO> getAllUser();


    /**
     * 用户名模糊查询
     * @param name
     * @return
     */
//    @Select("SELECT u.id, b.id as banId, u.name, u.avatar, u.role, " +
//
//            // 判断用户当前时间是否处于冻结期内
//            "CASE WHEN b.id IS NOT NULL " +
//            "     AND b.end_time > NOW() " +
//            "     AND b.is_deleted = 0 " +
//            "     THEN 0 " +        // 0表示被冻结
//            "     ELSE 1 " +        // 1表示正常
//            "END AS status, " +
//
//            "b.start_time as startTime, " +
//            "b.end_time as endTime, " +
//            "b.is_deleted AS isDeleted " +
//            "FROM user u " +
//            "LEFT JOIN ban b ON u.id = b.user_id " +
//            "WHERE u.name LIKE CONCAT('%', #{name}, '%') " +
//            "GROUP BY u.id, b.id, u.name")
    @Select("SELECT u.id, " +
            "(SELECT id FROM ban WHERE user_id = u.id AND is_deleted = 0 ORDER BY end_time DESC LIMIT 1) as banId, " +
            "u.name, u.avatar, u.role, " +
            "CASE WHEN EXISTS (SELECT 1 FROM ban WHERE user_id = u.id AND is_deleted = 0 AND end_time > NOW()) " +
            "     THEN 0 " +
            "     ELSE 1 " +
            "END AS status, " +
            "(SELECT start_time FROM ban WHERE user_id = u.id AND is_deleted = 0 ORDER BY end_time DESC LIMIT 1) as startTime, " +
            "(SELECT end_time FROM ban WHERE user_id = u.id AND is_deleted = 0 ORDER BY end_time DESC LIMIT 1) as endTime, " +
            "(SELECT is_deleted FROM ban WHERE user_id = u.id AND is_deleted = 0 ORDER BY end_time DESC LIMIT 1) as isDeleted " +
            "FROM user u " +
            "WHERE u.name LIKE CONCAT('%', #{name}, '%')")
    List<AdminManageUserVO> getUserByName(@Param("name") String name);
}

