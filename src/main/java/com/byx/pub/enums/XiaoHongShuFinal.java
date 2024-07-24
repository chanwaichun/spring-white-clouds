package com.byx.pub.enums;

/**
 * 小红书常量值
 * @author Jump
 * @date 2023/12/20 15:34:33
 */
public interface XiaoHongShuFinal {
    /**
     * appId?
     */
    String appKey = "ba4256a094274ebeba2f";
    /**
     * 密钥
     */
    String appSecret = "74399f133d1ac4c8abd9491e684a2337";
    /**
     * version
     */
    String version = "2.0";
    /**
     * 获取accessToken
     */
    String xhs_Url = "https://ark.xiaohongshu.com/ark/open_api/v3/common_controller";
    /**
     * 获取accessToken方法名
     */
    String code2AccessTokenMethod = "oauth.getAccessToken";
    /**
     * 获取accessToken方法名
     */
    String refreshTokenMethod = "oauth.refreshToken";
    /**
     * 获取商品类目列表
     */
    String categoryList = "common.getCategories";
    /**
     * 获取类目品牌列表
     */
    String categoryBrandList = "common.brandSearch";
    /**
     * 获取类目属性列表
     */
    String categoryAttributeList = "common.getAttributeLists";
    /**
     * 获取类目属性值列表
     */
    String attributeValueList = "common.getAttributeValues";
    /**
     * 获取运费模版列表
     */
    String carriageTemplateList = "common.getCarriageTemplateList";
    /**
     * 上传素材
     */
    String upMaterial = "material.uploadMaterial";
    /**
     * 创建item
     */
    String createItem = "product.createItemV2";
    /**
     * 创建sku
     */
    String createSku = "product.createSkuV2";
    /**
     * 上架sku
     */
    String skuAvailable = "product.updateSkuAvailable";
    /**
     * 获取物流方案列表
     */
    String logisticsList = "common.getLogisticsList";
    /**
     * 获取订单列表
     */
    String orderList = "order.getOrderList";
    /**
     * 获取订单详情
     */
    String orderDetail = "order.getOrderDetail";
    /**
     * 获取订单收货人信息
     */
    String orderReceiver = "order.getOrderReceiverInfo";
    /**
     * 解密订单收货人信息
     */
    String batchDecrypt = "data.batchDecrypt";
}
