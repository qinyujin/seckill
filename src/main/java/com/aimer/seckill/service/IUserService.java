package com.aimer.seckill.service;

import com.aimer.seckill.pojo.User;
import com.aimer.seckill.vo.LoginVO;
import com.aimer.seckill.vo.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aimer
 * @since 2023-03-14
 */
public interface IUserService extends IService<User> {
    RespBean doLogin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response);

    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);
}
