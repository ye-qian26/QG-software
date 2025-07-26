package com.qg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qg.domain.Review;
import com.qg.vo.ReviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {


    /**
     * 查看某个软件下的所有评论
     * @param softwareId
     * @return
     */
    @Select("SELECT r.id, r.content, r.user_id AS userId" +
            ", r.content, r.time AS createTime" +
            ", r.is_deleted AS isDeleted, u.avatar, u.name AS username " +
            "FROM review r " +
            "INNER JOIN user u ON u.id = r.user_id " +
            "WHERE r.software_id = #{softwareId} and r.is_deleted = 0")
    List<ReviewVO> getAllReviewBySoftwareId(@Param("softwareId")Long softwareId);
}
