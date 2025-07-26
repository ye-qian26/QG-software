package com.qg.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.*;
import com.qg.dto.UserDto;
import com.qg.mapper.*;
import com.qg.service.UserService;
import com.qg.utils.EmailService;
import com.qg.utils.HashSaltUtil;
import com.qg.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.qg.domain.Code.*;

import static com.qg.utils.RedisConstants.LOGIN_USER_KEY;
import static com.qg.utils.RedisConstants.LOGIN_USER_TTL;
import static com.qg.utils.HashSaltUtil.verifyHashPassword;

import static com.qg.utils.RedisConstants.LOGIN_CODE_KEY;
import static com.qg.utils.RedisConstants.LOGIN_CODE_TTL;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApplyDeveloperMapper applyDeveloperMapper;

    @Autowired
    private ApplySoftwareMapper applySoftwareMapper;

    @Autowired
    private BanMapper banMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private SoftwareMapper softwareMapper;

    @Autowired
    private SubscribeMapper subscribeMapper;

    @Override
    public Map<String, Object> loginByPassword(String email, String password) {

        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();

        lqw.eq(User::getEmail, email);
        System.out.println("登录邮箱：" + email);

        //List<User> list= userMapper.selectList(lqw);
        User loginUser = userMapper.selectOne(lqw);
        //System.out.println(list);
        //System.out.println(loginUser);
        System.out.println(loginUser);


        if (loginUser == null || !HashSaltUtil.verifyHashPassword(password, loginUser.getPassword())) {
            return null;
        }
        //token验证
        String token = UUID.randomUUID().toString();
        UserDto userDto = BeanUtil.copyProperties(loginUser, UserDto.class);
        System.out.println(userDto);
        Map<String, Object> usermap = BeanUtil.beanToMap(userDto, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fileName, fileValue) -> fileValue.toString()));
        //usermap.put("lastActiveTime", System.currentTimeMillis());


        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, usermap);
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", loginUser);
        return map;
//        return null;
    }

    @Override
    public Result loginByCode(String email, String code) {
        // 判断 邮箱 格式
        if (RegexUtils.isEmailInvalid(email)) {
            return new Result(BAD_REQUEST, "邮箱格式错误");
        }
        // 符合 格式
        // 判断用户是否存在
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, email);
        User user = userMapper.selectOne(lqw);
        if (user == null) {
            return new Result(NOT_FOUND, "该用户尚未注册");
        }

        // 从redis中取出验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + email);
        if (cacheCode == null || !cacheCode.equals(code)) {
            return new Result(NOT_FOUND, "验证码错误");
        }

        // 随机生成token，作为的登录令牌
        String token = cn.hutool.core.lang.UUID.randomUUID().toString(true);
        // 7.2.将User对象转换为HashMap存储
        UserDto userDTO = BeanUtil.copyProperties(user, UserDto.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fileName, fileValue) -> fileValue.toString()));
        // 7.3.存储
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        // 7.4.设置token有效期
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>();
        map.put("user", userDTO);
        map.put("token", token);
        return new Result(SUCCESS, map, "");
    }

    @Override
    public Result register(User user, String code) {

        // 判断参数非空
        if (user == null || code == null) {
            System.out.println("存在空参");
            return new Result(BAD_REQUEST, "存在空参");

        }
        // 获取用户邮箱，并做正则验证
        String email = user.getEmail().trim();
        if (RegexUtils.isEmailInvalid(email)) {
            System.out.println("邮箱格式错误");
            return new Result(BAD_REQUEST, "邮箱格式错误");
        }

        // 再查看验证码是否正确
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + email);
        if (cacheCode == null || !cacheCode.equals(code)) {
            System.out.println(cacheCode);
            System.out.println("用户输入的验证码：" + code);
            System.out.println("验证码错误");
            return new Result(NOT_FOUND, "验证码错误");
        }

        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, email);

        // 判断邮箱是否已经被注册
        User one = userMapper.selectOne(lqw);
        if (one != null) {
            System.out.println("该邮箱已被注册");
            return new Result(CONFLICT, "该邮箱已被注册！");
        }

        // 对密码进行加密处理
        user.setPassword(HashSaltUtil.creatHashPassword(user.getPassword()));

        // 向数据库中添加数据
        if (userMapper.insert(user) != 1) {
            System.out.println("注册失败，请稍后重试");
            return new Result(INTERNAL_ERROR, "注册失败，请稍后重试");
        }
        // 注册成功后删除验证码
        stringRedisTemplate.delete(LOGIN_CODE_KEY + user.getEmail());
        System.out.println("注册成功，恭喜你！");

        return new Result(CREATED, user,"恭喜你，注册成功！");
    }

    @Override
    public Result update(User user, String code) {
        if (user == null) {
            return new Result(BAD_REQUEST, "表单为空");
        }
        String email = user.getEmail();
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + email);
        System.out.println("缓存中的验证码：" + cacheCode);
        System.out.println("用户输入的验证码：" + code);
        System.out.println("用户邮箱：" + email);

        if (cacheCode == null || !cacheCode.equals(code)) {
            return new Result(NOT_FOUND, "验证码错误");
        }
        // 如果密码不为空，则加密
        if (user.getPassword() != null) {
            user.setPassword(HashSaltUtil.creatHashPassword(user.getPassword()));
        }

        if (userMapper.updateById(user) > 0) {
            return new Result(SUCCESS, "修改成功！");
        }

        return new Result(NOT_FOUND, "网络错误！");
    }

    @Override
    public Result delete(Long id) {
        int i = userMapper.deleteById(id);
        LambdaQueryWrapper<ApplyDeveloper> applyDeveloperWrapper = new LambdaQueryWrapper<>();
        applyDeveloperWrapper.eq(ApplyDeveloper::getUserId, id);
        applyDeveloperMapper.delete(applyDeveloperWrapper);

        LambdaQueryWrapper<ApplySoftware> applySoftwareWrapper = new LambdaQueryWrapper<>();
        applySoftwareWrapper.eq(ApplySoftware::getUserId, id);
        applySoftwareMapper.delete(applySoftwareWrapper);

        LambdaQueryWrapper<Ban> banWrapper = new LambdaQueryWrapper<>();
        banWrapper.eq(Ban::getUserId, id);
        banMapper.delete(banWrapper);

        LambdaQueryWrapper<Equipment> equipmentWrapper = new LambdaQueryWrapper<>();
        equipmentWrapper.eq(Equipment::getUserId, id);
        equipmentMapper.delete(equipmentWrapper);

        LambdaQueryWrapper<Message> messageWrapper = new LambdaQueryWrapper<>();
        messageWrapper.eq(Message::getReceiverId, id).or().eq(Message::getPosterId, id);
        messageMapper.delete(messageWrapper);

        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getUserId, id).or().eq(Order::getDeveloperId, id);
        orderMapper.delete(orderWrapper);

        LambdaQueryWrapper<Review> reviewWrapper = new LambdaQueryWrapper<>();
        reviewWrapper.eq(Review::getUserId, id);
        reviewMapper.delete(reviewWrapper);

        LambdaQueryWrapper<Software> softwareWrapper = new LambdaQueryWrapper<>();
        softwareWrapper.eq(Software::getAuthorId, id);
        softwareMapper.delete(softwareWrapper);

        LambdaQueryWrapper<Subscribe> subscribeWrapper = new LambdaQueryWrapper<>();
        subscribeWrapper.eq(Subscribe::getUserId, id).or().eq(Subscribe::getDeveloperId, id);
        subscribeMapper.delete(subscribeWrapper);
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

        if (author == null || customer == null || price <= 0 || price > customer.getMoney()) {
            System.out.println("用户不存在或余额不足");
            return 0;
        }

        System.out.println("==>用户准备扣钱" + customer.getMoney());
        customer.setMoney(customer.getMoney() - price);
        int customerResult = userMapper.updateById(customer);
        System.out.println("==>用户已经扣钱" + customer.getMoney());

        System.out.println("==>开发商准备收款" + author.getMoney());
        author.setMoney(author.getMoney() + price);
        int authorResult = userMapper.updateById(author);
        System.out.println("==>开发商已经收款" + author.getMoney());

        if (customerResult > 0 && authorResult > 0) {
            return 1;
        }

        throw new RuntimeException("交易失败，可能是余额不足或其他错误");

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

        // 发送验证码到邮箱
        // 3. 调用邮件工具类发送验证码
        boolean sendSuccess = emailService.sendSimpleEmail(
                email,
                "你的验证码",
                "尊敬的用户，你的验证码是：" + code + "，有效期2分钟。"
        );
        if (sendSuccess) {
            System.out.println("已发送验证码到邮箱到 " + email);
            // 保存验证码到 redis 中
            stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + email, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
            return new Result(SUCCESS, "验证码已发送至邮箱，请注意查收");
        } else {
            // 发送失败（可能是邮箱不存在或其他原因）
            return new Result(BAD_REQUEST, "验证码发送失败，请检查邮箱地址是否正确");
        }
    }


    /**
     * 更新用户头像
     *
     * @param userId
     * @param avatarUrl
     * @return
     */
    @Override
    public boolean updateAvatar(Long userId, String avatarUrl) {
        return userMapper.updateAvatar(userId, avatarUrl) > 0;
    }


    /**
     * @param id
     * @param money
     * @return int
     * @Author lrt
     * @Description //TODO 充值
     * @Date 16:59 2025/7/25
     * @Param
     **/

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateMoney(Long id, Double money) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getId, id);
        User user = userMapper.selectOne(lqw);
        if (user == null) {
            return 0;
        }
        user.setMoney(user.getMoney() + money);

        return userMapper.updateById(user);
    }

    @Override
    public boolean updatePhone(Long id, String phone) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getId, id);
        User user = userMapper.selectOne(lqw);
        if (user == null) {
            return false;
        }
        System.out.println("更新手机号为：" + phone);

        user.setPhone(phone);
        return userMapper.updateById(user) > 0;

    }

    @Override
    public boolean updateName(Long id, String name) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getId, id);
        User user = userMapper.selectOne(lqw);
        if (user == null) {
            System.out.println("用户不存在，无法更新用户名");
            return false;
        }
        System.out.println("更新用户名为：" + name);

        user.setName(name);
        return userMapper.updateById(user) > 0;
    }


}
