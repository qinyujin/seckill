package com.aimer.seckill.mapper;

import com.aimer.seckill.pojo.Goods;
import com.aimer.seckill.vo.GoodsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("SELECT \n" +
            "g.id,\n" +
            "g.goods_name,\n" +
            "g.goods_title,\n" +
            "g.goods_img,\n" +
            "g.goods_detail,\n" +
            "g.goods_price,\n" +
            "g.goods_stock,\n" +
            "sg.seckill_price,\n" +
            "sg.stock_count,\n" +
            "sg.start_date,\n" +
            "sg.end_date\n" +
            "from t_goods g LEFT JOIN t_seckill_goods sg on g.id = sg.goods_id\n")
    List<GoodsVO> findGoodsVo();

    @Select("SELECT \n" +
            "g.id,\n" +
            "g.goods_name,\n" +
            "g.goods_title,\n" +
            "g.goods_img,\n" +
            "g.goods_detail,\n" +
            "g.goods_price,\n" +
            "g.goods_stock,\n" +
            "sg.seckill_price,\n" +
            "sg.stock_count,\n" +
            "sg.start_date,\n" +
            "sg.end_date\n" +
            "from t_goods g LEFT JOIN t_seckill_goods sg on g.id = sg.goods_id\n" +
            "where g.id=#{goodsId}")
    GoodsVO findGoodsVoById(Long goodsId);
}
