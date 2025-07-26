package com.qg.service;

import com.qg.domain.Result;
import com.qg.domain.User;
import com.qg.dto.UserDto;

import java.util.Map;


public interface UserService {
    Map<String,Object> loginByPassword(String email, String password);

    Result loginByCode(String email, String code);

    Result register(User user, String code);

    Result update(User user, String code,String password);

    Result delete(Long id);


    int transaction(Long userId, Long authorId, Double price);

    double getPriceById(Long id);

    User getUser(Long id);


    Result sendCodeByEmail(String email);

    boolean updateAvatar(Long userId, String avatarUrl);

    int updateMoney(Long id, Double money);

    boolean updatePhone(Long id, String phone);

    boolean updateName(Long id, String name);

    User getUserByEmail(String email);
}
