package com.qg.service;

import com.qg.domain.Subscribe;

import com.qg.domain.User;
import com.qg.vo.SubscribeVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SubscribeService {
    boolean subscribe(Subscribe subscribe);

    boolean unsubscribe(Subscribe subscribe);

<<<<<<< HEAD
    List<User> getAllSubscribe(Long userId);

    boolean isSubscribe(Long userId, Long developerId);
=======
    List<SubscribeVO> getMySubscribe(Long userId);
>>>>>>> 30d6dca0f0a4f023df7c662cc063e2bfeee98a54
}
