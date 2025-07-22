package com.qg.service;

import com.qg.domain.Review;

import java.util.List;

public interface ReviewService {
    int addReview(Review review);

    List<Review> selectBySoftware(Long softwareId);

    List<Review> selectAll();

    int deleteReview(Long id);
}
