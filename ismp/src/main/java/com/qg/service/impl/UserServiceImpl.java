package com.qg.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Result;
import com.qg.domain.User;
import com.qg.dto.UserDto;
import com.qg.mapper.UserMapper;
import com.qg.service.UserService;
import com.qg.utils.EmailService;
import com.qg.utils.HashSaltUtil;
import com.qg.utils.RegexUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.qg.domain.Code.*;
import static com.qg.utils.HashSaltUtil.verifyHashPassword;
import static com.qg.utils.RedisConstants.LOGIN_CODE_KEY;
import static com.qg.utils.RedisConstants.LOGIN_CODE_TTL;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmailService emailService;

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

    @Override
    public Result sendCodeByEmail(String email) {
        // 判断是否是无效邮箱地址
        if (RegexUtils.isEmailInvalid(email)) {
            return new Result(BAD_REQUEST, "邮箱格式错误");
        }
        // 符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
        System.out.println("验证码：" + code);
        // 保存验证码到 redis 中
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + email, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 发送验证码到邮箱
        emailService.sendSimpleEmail(email, "验证码", code);
        System.out.println("已发送验证码到邮箱到 " + email);
        return new Result(SUCCESS, "验证码发送成功~");
    }
}
