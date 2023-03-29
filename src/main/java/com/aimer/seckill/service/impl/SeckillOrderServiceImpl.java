package com.aimer.seckill.service.impl;

import com.aimer.seckill.mapper.SeckillOrderMapper;
import com.aimer.seckill.pojo.SeckillOrder;
import com.aimer.seckill.pojo.User;
import com.aimer.seckill.service.ISeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒杀订单表 服务实现类
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Override
    public SeckillOrder findByUidAndGoodsId(Long uid, Long goodsId) {
        return seckillOrderMapper.queryByUidAndGid(uid,goodsId);
    }

    @Override
    public SeckillOrder getResult(User user, Long goodsId) {
        return seckillOrderMapper.getResult(user.getId(),goodsId);
    }
}
