package com.aimer.seckill.controller;

import com.aimer.seckill.pojo.User;
import com.aimer.seckill.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author :覃玉锦
 * @create :2023-03-22 19:45:00
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;

    /*@RequestMapping("/toList")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model, @CookieValue("userTicket") String ticket) {
        if (StringUtils.isEmpty(ticket)) {
            //跳转到登录
            return "login";
        }
//        User user = (User) session.getAttribute(ticket);
        //通过redis存储分布式session
        User user = userService.getUserByCookie(ticket, request, response);
        if (null == user) {
            return "login";
        }
        model.addAttribute("user", user);
        return "goodsList";
    }*/

    //通过mvc拦截器设定了必有User参数
    @RequestMapping("/toList")
    @ResponseBody
    public String toList(Model model, User user) {
//        if (StringUtils.isEmpty(ticket)) {
//            //跳转到登录
//            return "login";
//        }
//        User user = (User) session.getAttribute(ticket);
        //通过redis存储分布式session
//        User user = userService.getUserByCookie(ticket, request, response);
        if (null == user) {
            return "login";
        }
        model.addAttribute("user", user);
        return "goodsList";
    }
}
