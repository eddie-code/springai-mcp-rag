package org.dromara.mcp.tools;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.dromara.mapper.ProductMapper;
import org.dromara.pojo.Product;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;

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
        private Integer status;
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


}
