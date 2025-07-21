package com.qg.service;

import com.qg.domain.Result;
import com.qg.domain.User;


public interface UserService {
    Result loginByPassword(String email, String password);

    Result loginByCode(String email, String code);

    Result register(User user);

    Result update(User user);

    Result delete(Integer id);


    int transaction(long userId, long authorId, double price);
}
