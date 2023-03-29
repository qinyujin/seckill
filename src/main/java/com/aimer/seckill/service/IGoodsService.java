package com.aimer.seckill.service;

import com.aimer.seckill.pojo.Goods;
import com.aimer.seckill.vo.GoodsVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVO> findGoodsVo();

    GoodsVO findGoodsVoById(Long goodsId);
}
