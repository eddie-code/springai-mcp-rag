package org.dromara.mcp.tools;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.enums.ListSortEnum;
import org.dromara.enums.PriceCompareEnum;
import org.dromara.enums.ProductStatusEnum;
import org.dromara.mapper.ProductMapper;
import org.dromara.pojo.Product;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lee
 * @description
 */
@Slf4j
@Component
public class ProductTool {

    @Resource
    private ProductMapper productMapper;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateProductRequst {
        @ToolParam(description = "商品的名称")  // 作用：@ToolParam 用于描述传递给 @Tool 方法的参数。
        private String productName;
        @ToolParam(description = "商品的品牌")
        private String brand;
        @ToolParam(description = "商品的简介(可以为空)")
        private String description;
        @ToolParam(description = "商品的价格")
        private BigDecimal price;
        @ToolParam(description = "商品的库存数量")
        private Integer stock;
        @ToolParam(description = "商品的状态（上架状态的值为1/下架状态的值为0/预售状态的值为2）")
        private ProductStatusEnum status;
    }


    /**
     * 创建/新增商品信息记录
     *
     * @param createProductRequst 创建商品请求对象，包含商品的基本信息
     * @return 返回操作结果字符串，成功时返回"商品信息创建成功"
     */
    @Tool(description = "创建/新增商品信息记录")
    public String createNewProduct(CreateProductRequst createProductRequst) {

        log.info(" ========= 调用MCP工具：createNewProduct() ============");
        log.info(String.format(" | 参数 createProductRequest 为： | %s", createProductRequst.toString()));
        log.info(" ======== End =========");

        // 将请求对象转换为商品实体对象
        Product product = new Product();
        BeanUtils.copyProperties(createProductRequst, product);

        // 生成12位的随机数字
        // product.setProductId(RandomStringUtils.randomNumeric(12));
        // 使用 SecureRandom 生成12位随机数字（替代 deprecated 的 RandomStringUtils.randomNumeric）
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder randomId = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            randomId.append(secureRandom.nextInt(10));
        }
        product.setProductId(randomId.toString());

        // 设置创建时间和更新时间
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());

        // 插入商品信息到数据库
        productMapper.insert(product);
        return "商品信息创建成功";
    }

    @Transactional
    @Tool(description = "根据商品id删除商品记录")
    public String deleteProduct(String productId) {

        log.info("========== 调用MCP工具：deleteProduct() ==========");
        log.info(String.format("| 参数 productId 为： %s", productId));
        log.info("========== End ==========");

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);

        productMapper.delete(queryWrapper);

        return "商品信息删除成功";
    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryProductRequest {

        // required = true 默认谁自动填充数据，所以查询的时候建议使用 false

        @ToolParam(description = "商品的编号", required = false)
        private String productId;
        @ToolParam(description = "商品的名称", required = false)
        private String productName;
        @ToolParam(description = "商品的品牌", required = false)
        private String brand;
        @ToolParam(description = "具体商品价格大小", required = false)
        private Integer price;
        @ToolParam(description = "商品的状态（上架状态的值为1/下架状态的值为0/预售状态的值为2）", required = false)
        private ProductStatusEnum status;
//        @ToolParam(description = "查询列表的排序", required = false)
//        private ListSortEnum sortEnum;
//        @ToolParam(description = "比较价格的大小", required = false)
//        private PriceCompareEnum priceCompareEnum;
    }

    @Tool(description = "把商品价格的比较（大于/小于/大于等于/小于等于/高于/低于/不高于/不低于/等于）转换为对应的枚举")
    public PriceCompareEnum getPriceCompareEnum(String priceCompare) {

        log.info("========== 调用MCP工具：getPriceCompareEnum() ==========");
        log.info(String.format("| 参数 priceCompare 为： %s", priceCompare));
        log.info("========== End ==========");

        if (priceCompare.equalsIgnoreCase(PriceCompareEnum.GREATER_THAN.value)) {
            return PriceCompareEnum.GREATER_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.LESS_THAN.value)) {
            return PriceCompareEnum.LESS_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.GREATER_THAN_OR_EQUAL_TO.value)) {
            return PriceCompareEnum.GREATER_THAN_OR_EQUAL_TO;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.LESS_THAN_OR_EQUAL_TO.value)) {
            return PriceCompareEnum.LESS_THAN_OR_EQUAL_TO;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.HIGHER_THAN.value)) {
            return PriceCompareEnum.HIGHER_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.LOWER_THAN.value)) {
            return PriceCompareEnum.LOWER_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.NOT_HIGHER_THAN.value)) {
            return PriceCompareEnum.NOT_HIGHER_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.NOT_LOWER_THAN.value)) {
            return PriceCompareEnum.NOT_LOWER_THAN;
        } else {
            return PriceCompareEnum.EQUAL_TO;
        }
    }

    @Tool(description = "根据条件查询商品（product）信息")
    public List<Product> queryProductListByCondition(QueryProductRequest queryProductRequest) {

        log.info("========== 调用MCP工具：queryProductListByCondition() ==========");
        log.info(String.format("| 参数 queryProductRequest 为： %s", queryProductRequest.toString()));
        log.info("========== End ==========");

        return null;
    }



}
