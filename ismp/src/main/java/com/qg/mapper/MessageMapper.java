package com.qg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qg.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * @param userId
     * @param messageId 标记信息为已读
     */
    @Update("UPDATE `message` SET `is_read` = 1" +
            " WHERE id = #{messageId} AND receiver_id = #{userId} AND is_read = 0")
    Integer read(@Param("userId") Long userId, @Param("messageId") Long messageId);


}
