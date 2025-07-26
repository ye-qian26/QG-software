package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Review;
import com.qg.mapper.ReviewMapper;
import com.qg.service.ReviewService;
import com.qg.vo.ReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewMapper reviewMapper;


    @Override
    public int addReview(Review review) {

        String content = review.getContent();

        String [] badWord =   {"垃圾","去你妈","屎","操","",""};

        for (String regex : badWord) {
            if (content.contains(regex)) {
                return 0;
            }
        }



        return reviewMapper.insert(review);
    }

    /**
     * 查看某个软件下的所有评论
     * @param softwareId
     * @return
     */
    @Override
    public List<ReviewVO> selectBySoftware(Long softwareId) {
        return reviewMapper.getAllReviewBySoftwareId(softwareId);
    }

    @Override
    public List<Review> selectAll() {
        return reviewMapper.selectList(null);
    }

    @Override
    public int deleteReview(Long id) {
        return reviewMapper.deleteById(id);
    }
}
