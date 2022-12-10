package com.github.redis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.redis.dto.LoginFormDto;
import com.github.redis.entity.User;
import com.github.redis.mapper.UserMapper;
import com.github.redis.rest.Result;
import com.github.redis.service.UserService;
import com.github.redis.utils.RedisConstants;
import com.github.redis.utils.RegexUtils;
import com.github.redis.utils.UserHolder;
import com.github.redis.vo.UserVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.Duration;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 13:46:02
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @NonNull
    private StringRedisTemplate stringRedisTemplate;

    @NonNull
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result sendCode(String phone, HttpSession session) {
        // ① 校验手机号码
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机格式错误！");
        }
        // ② 生成验证码
        String code = RandomUtil.randomNumbers(6);
        // ③ 保存验证码到 Redis 中，并设置验证码的有效期是 5 分钟
        stringRedisTemplate.opsForValue().set(StrUtil.addPrefixIfNot(phone, RedisConstants.LOGIN_CODE_PHONE_KEY_PREFIX), code, Duration.ofMinutes(RedisConstants.LOGIN_CODE_TTL));
        // ⑤ 发送验证码
        log.info("发送验证码成功，验证码是 {}", code);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDto loginFormDto, HttpSession session) {
        // ① 校验手机号
        String phone = loginFormDto.getPhone();
        if (StrUtil.isBlank(phone) || RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机格式错误！");
        }
        // 获取验证码
        String code = loginFormDto.getCode();
        // 根据手机号获取对应的真实的验证码
        String realCode = stringRedisTemplate.opsForValue().get(StrUtil.addPrefixIfNot(phone, RedisConstants.LOGIN_CODE_PHONE_KEY_PREFIX));
        // ② 校验验证码
        if (StrUtil.isBlank(realCode) || StrUtil.isBlank(code) || !code.equalsIgnoreCase(realCode)) {
            return Result.fail("验证码错误！");
        }
        // ③ 根据手机号去表中查询该用户是否存在：如果存在，则表示登录成功，写入 Session；如果不存在，则快速创建用户，写入 Session
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);

        User user = this.getOne(queryWrapper);
        // ④ 判断用户是否存在
        if (ObjectUtil.isNull(user)) {
            user = new User();
            user.setPhone(phone);
            user.setNickName("user_" + RandomUtil.randomString(10));
            this.save(user);
        }
        // ⑤ 保存用户信息到 Redis 中
        // 生成随机 token
        String token = UUID.randomUUID().toString(true);
        // 将 User 对象转换为 UserVo 对象
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        // 将 User 对象转换为 HashMap 存储到 Redis 中
        redisTemplate.opsForHash().putAll(StrUtil.addPrefixIfNot(token, RedisConstants.LOGIN_TOKEN_KEY_PREFIX), BeanUtil.beanToMap(userVo));
        // 设置 token 的有效期
        redisTemplate.expire(StrUtil.addPrefixIfNot(token, RedisConstants.LOGIN_TOKEN_KEY_PREFIX), Duration.ofMinutes(RedisConstants.LOGIN_TOKEN_TTL));
        return Result.ok(token);
    }

    @Override
    public Result me() {
        UserVo user = UserHolder.getUser();
        return Result.ok(user);
    }
}
