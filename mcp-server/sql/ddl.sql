# 测试表
CREATE TABLE `product`
(
    `product_id`   varchar(64)  NOT NULL COMMENT '商品编号',
    `product_name` varchar(100) NOT NULL COMMENT '商品名称',
    `brand`        varchar(128) NOT NULL COMMENT '品牌',
    `price`        int          NOT NULL COMMENT '销售价格(单位:分)',
    `stock`        int          NOT NULL COMMENT '库存数量',
    `description`  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品简介',
    `STATUS`       int          NOT NULL COMMENT '状态(0-下架 1-上架)',
    `create_time`  datetime     NOT NULL COMMENT '创建时间',
    `update_time`  datetime     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`product_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='商品表';

# 手动录入测试
INSERT
INTO product
(product_id, product_name, brand, description, price, stock, status, create_time, update_time)
VALUES ('880278672935', '秋季新款预售商品', '优衣库', '', 199, 100, 2, 2025 - 11 - 07T20:28:42.228576200,
        2025 - 11 - 07T20:28:42.228576200);