package com.qg.controller;

import com.qg.domain.Result;
import com.qg.domain.Review;
import com.qg.service.EquipmentService;
import com.qg.service.ReviewService;
import com.qg.vo.ReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.qg.domain.Code.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 用户添加评论
     * @param review
     * @return
     */
    @PostMapping("/addReview")
    public Result addReview(@RequestBody Review review) {
        System.out.println("发表评论" + review);
        Long userId = review.getUserId();
        Long softwareId = review.getSoftwareId();
        String content = review.getContent();

        if (!equipmentService.isPurchased(userId, softwareId)) {

            return new Result(FORBIDDEN, "你尚未购买此软件");
        }


        if (userId <= 0 || softwareId <= 0 || content == null || "".equals(content)) {
            System.out.println("评论失败！");
            return new Result(BAD_REQUEST, "评论失败");
        }

        int addReview = reviewService.addReview(review);

        return addReview > 0 ? new Result(SUCCESS,"评论成功") : new Result(INTERNAL_ERROR,"评论失败，请稍后重试");
    }

    /**
     * 获取软件的所有评论
     * @param softwareId
     * @return
     */
    @GetMapping ("/reviewOfSoftware/{softwareId}")
    public Result reviewOfSoftware(@PathVariable Long softwareId) {
        System.out.println("reviewOfSoftware==> " + softwareId);
        if (softwareId <= 0) {
            return new Result(BAD_REQUEST, "获取评论失败");
        }
        List<ReviewVO> reviews = reviewService.selectBySoftware(softwareId);
        System.out.println(reviews);
        if (reviews == null || reviews.size() == 0) {
            return new Result(NOT_FOUND, "评论无法加载");
        }

        return new Result(SUCCESS,reviews,"评论加载成功");

    }

    /**
     * 管理员查看所有评论
     * @return
     */
    @GetMapping("/getAllReviews")
    public Result getAllReviews() {
        List<Review> list = reviewService.selectAll();
        System.out.println(list);
        return list!= null ? new Result(SUCCESS,list,"查询成功") : new Result(NOT_FOUND,"查询失败");
    }

    /**
     * 管理员删除评论
     * @param id
     * @return
     */
    @DeleteMapping("/deleteReview/{id}")
    public Result deleteReview(@PathVariable Long id) {
        System.out.println("deleteReview==> " + id);
        if (id <= 0) {
            return new Result(BAD_REQUEST,"删除失败");
        }
        int delete = reviewService.deleteReview(id);

        return delete > 0 ? new Result(SUCCESS,"删除成功") : new Result(BAD_GATEWAY,"删除失败");
    }


}
