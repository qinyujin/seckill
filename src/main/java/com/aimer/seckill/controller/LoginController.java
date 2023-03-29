package com.aimer.seckill.controller;

import com.aimer.seckill.service.IUserService;
import com.aimer.seckill.vo.LoginVO;
import com.aimer.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author :覃玉锦
 * @create :2023-03-14 15:40:00
 */
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

//    @PostMapping("/doLogin")
//    @ResponseBody
//    public RespBean doLogin(@Valid @RequestBody LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {
//        return userService.doLogin(loginVO, request, response);
//    }

    //用于生成压测的数据
    @PostMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {
        return userService.doLogin(loginVO, request, response);
    }
}
