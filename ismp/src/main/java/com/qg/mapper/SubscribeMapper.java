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

            // DISTINCT确保只计算一次
            "COUNT(DISTINCT s1.id) AS followers_count, " +
            "COUNT(DISTINCT sw.id) AS software_count " +
            "FROM user u " +
            // s用于获取用户关注的所有开发者
            "LEFT JOIN " +
            "subscribe s ON u.id = s.developer_id " +

            // s1用于计算粉丝数
            "LEFT JOIN " +
            "subscribe s1 ON u.id = s1.developer_id " +

            // sw用于计算该开发者的软件数量
            "LEFT JOIN " +
            "software sw ON u.id = sw.author_id AND sw.is_deleted = 0 " +

            // s用于保留该用户关注的开发商
            "WHERE s.user_id = #{userId} " +
            "GROUP BY u.id, u.name, u.avatar")
    List<SubscribeVO> getMySubscribe(@Param("userId") Long userId);


    /**
     * 开发商查看他的粉丝的《粉丝个人信息》
     *
     * @param userId
     * @return
     */
    @Select("SELECT u.id, u.name, u.avatar, u.role " +
            "FROM user u " +
        
            "INNER JOIN subscribe s ON u.id = s.user_id " +
            "WHERE s.developer_id = #{userId}")
    List<FanVO> getMyFan(@Param("userId") Long userId);
}
