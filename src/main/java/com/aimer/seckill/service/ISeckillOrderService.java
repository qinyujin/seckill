package com.aimer.seckill.service;

import com.aimer.seckill.pojo.SeckillOrder;
import com.aimer.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 秒杀订单表 服务类
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    SeckillOrder findByUidAndGoodsId(Long uid, Long goodsId);

    SeckillOrder getResult(User user, Long goodsId);
}
