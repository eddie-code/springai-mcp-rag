package org.dromara.pojo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 *
 * @author lee
 * @description
 */
@Data
@ToString
public class Product {

    /**
     * 商品编号
     */
    private String productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 商品简介
     */
    private String description;

    /**
     * 销售价格(单位:分)
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 状态(0-下架 1-上架)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
