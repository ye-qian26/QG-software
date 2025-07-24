package com.qg.service;

import com.qg.domain.Subscribe;

import com.qg.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SubscribeService {
    boolean subscribe(Subscribe subscribe);

    boolean unsubscribe(Subscribe subscribe);

    List<User> getAllSubscribe(Long userId);

    boolean isSubscribe(Long userId, Long developerId);
}
