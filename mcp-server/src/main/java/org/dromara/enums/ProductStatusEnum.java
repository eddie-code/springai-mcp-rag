package org.dromara.enums;

/**
 * @author lee
 * @description 商品状态枚举
 */
public enum ProductStatusEnum {

    ON_SHELF(1, "上架"),
    OFF_SHELF(0, "下架"),
    PRE_SALE(2, "预售");

    public final Integer status;
    public final String description;

    ProductStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

}