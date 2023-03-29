package com.aimer.seckill.mapper;

import com.aimer.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 秒杀订单表 Mapper 接口
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
public interface SeckillOrderMapper extends BaseMapper<SeckillOrder> {

    @Select("select * from t_seckill_order where user_id = #{uid} and goods_id = #{goodsId}")
    SeckillOrder queryByUidAndGid(Long uid, Long goodsId);

    @Select("select * from t_seckill_order where user_id = #{userId} and goods_id = #{goodsId}")
    SeckillOrder getResult(Long userId, Long goodsId);
}
