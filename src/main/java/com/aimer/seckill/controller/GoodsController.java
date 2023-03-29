package com.aimer.seckill.controller;

import com.aimer.seckill.pojo.User;
import com.aimer.seckill.service.IGoodsService;
import com.aimer.seckill.service.IUserService;
import com.aimer.seckill.utils.JsonUtil;
import com.aimer.seckill.vo.GoodsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author :覃玉锦
 * @create :2023-03-22 19:45:00
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

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

    /**
     * 优化前 qps680 每秒1000个请求，循环3次
     *
     * 加了缓存后 qps1063
     *
     * @param model
     * @param user
     * @return
     */
    //通过mvc拦截器设定了必有User参数
    @RequestMapping("/toList")
    @ResponseBody
    public String toList(Model model, User user) {
        if (null == user) {
            return "login";
        }

        String cacheKey = "goodsList";

        String goodsList = (String) redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isEmpty(goodsList)) {
            List<GoodsVO> goodsVo = goodsService.findGoodsVo();
            goodsList = JsonUtil.serialize(goodsVo);
            redisTemplate.opsForValue().set(cacheKey, goodsList);
        }

        return goodsList;
    }

    @RequestMapping("/toDetail/{goodsId}")
    @ResponseBody
    public String toDetail(Model model, User user, @PathVariable Long goodsId) {
        if (null == user) {
            return "login";
        }
        GoodsVO goodsVoById = goodsService.findGoodsVoById(goodsId);
        return JsonUtil.serialize(goodsVoById);
    }
}
