package com.aimer.seckill.service.impl;

import com.aimer.seckill.mapper.UserMapper;
import com.aimer.seckill.pojo.User;
import com.aimer.seckill.service.IUserService;
import com.aimer.seckill.utils.CookieUtil;
import com.aimer.seckill.utils.MD5Utils;
import com.aimer.seckill.utils.UUIDUtil;
import com.aimer.seckill.vo.LoginVO;
import com.aimer.seckill.vo.RespBean;
import com.aimer.seckill.vo.RespBeanEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author aimer
 * @since 2023-03-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        //通过注解进行校验
        /*if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        if (!ValidatorUtils.isMobile(mobile)) {
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }*/

        User user = userMapper.selectById(mobile);
        if (user == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            //全局异常处理
//            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        String dbPwd = MD5Utils.fromPassToDBPass(password, user.getSlat());
        if (!dbPwd.equals(user.getPassword())) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();
        //session存用户身份信息
//        request.getSession().setAttribute(ticket, user);
        //采用redis存放身份信息
        redisTemplate.opsForValue().set("user:" + ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success();
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (null != user) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}
