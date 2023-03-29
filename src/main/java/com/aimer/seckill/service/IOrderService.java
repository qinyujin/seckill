package com.aimer.seckill.service;

import com.aimer.seckill.pojo.Order;
import com.aimer.seckill.pojo.User;
import com.aimer.seckill.vo.GoodsVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
public interface IOrderService extends IService<Order> {

    Order secKill(User user, GoodsVO goods);

    String createPath(User user, Long goodsId);

    boolean checkPath(User user, Long goodsId, String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
