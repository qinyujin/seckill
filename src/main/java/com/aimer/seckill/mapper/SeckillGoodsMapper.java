package com.aimer.seckill.mapper;

import com.aimer.seckill.pojo.SeckillGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 秒杀商品表 Mapper 接口
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

    @Select("select * from t_seckill_goods where goods_id = #{goodsId}")
    SeckillGoods getGoodsById(Long goodsId);

    @Update("update t_seckill_goods set stock_count = stock_count - 1 where id = #{id} and stock_count > 0")
    int deductStock(Long id);
}
