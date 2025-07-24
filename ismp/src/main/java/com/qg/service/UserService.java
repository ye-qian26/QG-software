package com.qg.service;

import com.qg.domain.Result;
import com.qg.domain.User;
import com.qg.dto.UserDto;


public interface UserService {
    User loginByPassword(String email, String password);

    User loginByCode(String email, String code);

    Result register(User user);

    Result update(User user);

    Result delete(Long id);


    int transaction(Long userId, Long authorId, Double price);

    double getPriceById(Long id);

    User getUser(Long id);

}
