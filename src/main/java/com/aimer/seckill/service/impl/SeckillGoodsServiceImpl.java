package com.aimer.seckill.service.impl;

import com.aimer.seckill.mapper.SeckillGoodsMapper;
import com.aimer.seckill.pojo.SeckillGoods;
import com.aimer.seckill.service.ISeckillGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒杀商品表 服务实现类
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Override
    public SeckillGoods getGoodsById(Long goodsId) {
        return seckillGoodsMapper.getGoodsById(goodsId);
    }

    @Override
    public boolean deductStock(Long id) {
        return seckillGoodsMapper.deductStock(id) > 0;
    }
}
