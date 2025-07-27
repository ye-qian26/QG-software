package com.qg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qg.domain.Subscribe;
import com.qg.vo.FanVO;
import com.qg.vo.SubscribeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface SubscribeMapper extends BaseMapper<Subscribe> {

    /**
     * 用户查看他关注的开发商的《开发商个人信息》
     *
     * @param userId
     * @return
     */
    @Select("SELECT u.id, u.name, u.avatar, " +
            "(SELECT COUNT(*) FROM subscribe WHERE developer_id = u.id AND is_deleted = 0) AS followers_count, " +
            "(SELECT COUNT(*) FROM software WHERE author_id = u.id AND is_deleted = 0) AS software_count " +
            "FROM user u " +
            "INNER JOIN subscribe s ON u.id = s.developer_id " +
            "WHERE s.user_id = #{userId} " +
            "AND u.is_deleted = 0 " +
            "AND s.is_deleted = 0")
    List<SubscribeVO> getMySubscribe(@Param("userId") Long userId);


    /**
     * 开发商查看他的粉丝的《粉丝个人信息》
     *
     * @param userId
     * @return
     */
    @Select("SELECT u.id, u.name, u.avatar, u.role " +
            "FROM user u " +
            "WHERE EXISTS (" +
            "  SELECT 1 FROM subscribe " +
            "  WHERE user_id = u.id " +
            "  AND developer_id = #{userId}" +
            "  AND is_deleted = 0 " +
            ")" +
            "AND u.is_deleted = 0 ")
    List<FanVO> getMyFan(@Param("userId") Long userId);
}
