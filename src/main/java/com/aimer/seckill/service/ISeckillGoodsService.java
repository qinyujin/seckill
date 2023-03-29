package com.aimer.seckill.service;

import com.aimer.seckill.pojo.SeckillGoods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 秒杀商品表 服务类
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
public interface ISeckillGoodsService extends IService<SeckillGoods> {

    SeckillGoods getGoodsById(Long goodsId);

    boolean deductStock(Long id);
}
