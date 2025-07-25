package com.qg.controller;


import cn.hutool.core.bean.BeanUtil;
import com.qg.domain.Ban;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.User;

import com.qg.dto.RegisterDTO;
import com.qg.dto.UserDto;
import com.qg.service.BanService;
import com.qg.service.UserService;
import com.qg.utils.EmailService;
import com.qg.utils.FileUploadHandler;
import com.qg.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static com.qg.domain.Code.*;


import static com.qg.domain.Code.SUCCESS;
import static com.qg.utils.FileUploadHandler.DOCUMENT_DIR;
import static com.qg.utils.FileUploadHandler.IMAGE_DIR;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BanService banService;


    @Autowired
    private EmailService emailService;

    @PostMapping("/upload/pdf")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println(file.getOriginalFilename());
            String filePath = FileUploadHandler.saveFile(file, DOCUMENT_DIR);
            return new Result(Code.SUCCESS, filePath); // 返回文件路径
        } catch (IOException e) {
            return new Result(Code.INTERNAL_ERROR, "上传失败");
        }
    }

    /**
     * 用户通过邮箱登录
     * @param email
     * @param password
     * @return
     */

    @GetMapping("/password")
    public Result loginByPassword(@RequestParam String email, @RequestParam String password) {
        Map<String, Object> map = userService.loginByPassword(email, password);
        System.out.println(email);
        System.out.println(password);
        if (map == null) {
            return new Result(NOT_FOUND, "用户未注册");
        }
        User user = (User) map.get("user");
        if (user == null) {
            return new Result(BAD_REQUEST, "未注册");
        }
        Long id = user.getId();
        if (banService.judgeBan(id)) {
            Ban ban = banService.selectByUserId(id);
            return new Result(FORBIDDEN, ban, "账号被封禁，无法登录");
        }
        map.put("user", BeanUtil.copyProperties(user, UserDto.class));
        return new Result(SUCCESS, map, "登录成功");
    }

    /**
     * 用户通过邮箱验证码登录
     * @param email
     * @param code
     * @return
     */
    @GetMapping("/code")
    public Result loginByCode(@RequestParam String email, @RequestParam String code) {
        return userService.loginByCode(email.trim(), code.trim());
    }

    /**
     * 用户注册
     * @param registerDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO.getUser(), registerDTO.getCode().trim());
    }

    /**
     * 用户修改信息
     * @param user
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody User user, @RequestParam String code) {
        return userService.update(user, code);
    }

    /**
     * 用户逻辑删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    /**
     * 获取用户余额
     * @param id
     * @return
     */
    @GetMapping("/getPrice/{id}")
    public Result getPriceById(@PathVariable Long id) {
        if (id <= 0) {
            return new Result(BAD_REQUEST, "获取失败！");
        }
        double price = userService.getPriceById(id);

        return new Result(SUCCESS, String.valueOf(price), "获取成功！");
    }

    /**
     * 用户查看个人信息
     * @param id
     * @return
     */
    @GetMapping("/getInformation/{id}")
    public Result getInformation(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return new Result(BAD_GATEWAY, "获取失败");
        }
        return new Result(SUCCESS, user, "获取成功");
    }

    /**
     * 发送验证码到邮箱
     * @param email
     * @return
     */
    @GetMapping("/sendCodeByEmail")
    public Result sendCodeByEmail(@RequestParam("email") String email) {
        System.out.println(email);
        // 发送验证码到邮箱
        return userService.sendCodeByEmail(email);
    }


    /**
     * 更新头像
     * @param file
     * @param userId
     * @return
     */
    @PostMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam("avatar") MultipartFile file,
                               @RequestParam("userId") Long userId) {
        try {
            // 验证文件是否为空
            if (file.isEmpty()) {
                return new Result(BAD_REQUEST, "请选择有效的头像文件");
            }

            // 判断是否为图片
            if (!FileUploadHandler.isValidImageFile(file)) {
                return new Result(Code.BAD_REQUEST, "上传的不是图片");
            }

            // 文件大小限制
            if (file.getSize() > 2 * 1024 * 1024) {
                return new Result(BAD_REQUEST, "图片大小不能超过2MB");
            }

            String avatarUrl = FileUploadHandler.saveFile(file, IMAGE_DIR);

            // 判断头像是否上传成功返回相应的结果
            if (userService.updateAvatar(userId, avatarUrl)) {
                return new Result(SUCCESS, avatarUrl, "头像上传成功");
            } else {
                return new Result(NOT_FOUND, "用户不存在");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Result(INTERNAL_ERROR, "头像上传失败");
        }
    }

    /**
     * @Author lrt
     * @Description //TODO 充值
     * @Date 17:00 2025/7/25
     * @Param
 * @param id
 * @param money
     * @return com.qg.domain.Result
     **/
    @PutMapping
    public Result updateMoney(@RequestParam Long id, @RequestParam Double money) {
        int i = userService.updateMoney(id,money);
        return i > 0 ? new Result(SUCCESS, "充值成功") : new Result(NOT_FOUND,"充值失败");
    }
}
