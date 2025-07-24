package com.qg.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Result;
import com.qg.domain.User;
import com.qg.dto.UserDto;
import com.qg.mapper.UserMapper;
import com.qg.service.UserService;
import com.qg.utils.HashSaltUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.qg.domain.Code.*;
import static com.qg.utils.HashSaltUtil.verifyHashPassword;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User loginByPassword(String email, String password) {

        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, email);
        User loginUser = userMapper.selectOne(lqw);
        if(loginUser == null){
            return null;
        }

        String token = UUID.randomUUID().toString();

        return loginUser;
    }

    @Override
    public User loginByCode(String email, String code) {
        return null;
    }

    @Override
    public Result register(User user) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, user.getEmail());
        User one = userMapper.selectOne(lqw);

        if (one != null) {
            return new Result(CONFLICT,"该邮箱已被注册！");
        }

        // 对密码进行加密处理
        user.setPassword(HashSaltUtil.creatHashPassword(user.getPassword()));

        userMapper.insert(user);

        return new Result(CREATED,"恭喜你，注册成功！");
    }

    @Override
    public Result update(User user) {

        if (user == null) {
            return new Result(BAD_REQUEST,"表单为空");
        }

        // 如果密码不为空，则加密
        if (user.getPassword() != null) {
            user.setPassword(HashSaltUtil.creatHashPassword(user.getPassword()));
        }

        if (userMapper.updateById(user) > 0) {
            return new Result(SUCCESS,"修改成功！");
        }

        return new Result(NOT_FOUND,"网络错误！");
    }

    @Override
    public Result delete(Long id) {
        int i = userMapper.deleteById(id);
        return i > 0 ? new Result(SUCCESS, "删除成功！") : new Result(NOT_FOUND, "删除错误！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transaction(Long userId, Long authorId, Double price) {
        LambdaQueryWrapper<User> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(User::getId, userId);
        User customer = userMapper.selectOne(lqw1);

        LambdaQueryWrapper<User> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(User::getId, authorId);
        User author = userMapper.selectOne(lqw2);

        if (author == null || customer == null || price <= 0 || price > author.getMoney()) {
            return 0;
        }

        customer.setMoney(customer.getMoney() - price);
        int customerResult = userMapper.updateById(customer);

        author.setMoney(author.getMoney() + price);
        int authorResult = userMapper.updateById(author);

        if (customerResult > 0 && authorResult > 0) {
            return 1;
        }

        return 0;

    }

    @Override
    public double getPriceById(Long id) {
        return userMapper.selectById(id).getMoney();
    }

    @Override
    public User getUser(Long id) {
        return userMapper.selectById(id);
    }



}
