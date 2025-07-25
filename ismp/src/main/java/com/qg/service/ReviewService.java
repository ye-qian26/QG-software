package com.qg.service;

import com.qg.domain.Review;
import com.qg.vo.ReviewVO;

import java.util.List;

public interface ReviewService {
    int addReview(Review review);

    List<ReviewVO> selectBySoftware(Long softwareId);

    List<Review> selectAll();

    int deleteReview(Long id);
}
