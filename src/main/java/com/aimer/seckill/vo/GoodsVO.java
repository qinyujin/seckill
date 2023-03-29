package com.aimer.seckill.vo;

import com.aimer.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author :覃玉锦
 * @create :2023-03-23 19:55:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVO extends Goods {
    /**
     * 秒杀家
     */
    private BigDecimal seckillPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private LocalDateTime startDate;

    /**
     * 秒杀结束时间
     */
    private LocalDateTime endDate;
}
