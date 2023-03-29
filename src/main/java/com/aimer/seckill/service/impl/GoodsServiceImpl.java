package com.aimer.seckill.service.impl;

import com.aimer.seckill.mapper.GoodsMapper;
import com.aimer.seckill.pojo.Goods;
import com.aimer.seckill.service.IGoodsService;
import com.aimer.seckill.vo.GoodsVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVO> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVO findGoodsVoById(Long goodsId) {
        return goodsMapper.findGoodsVoById(goodsId);
    }
}
