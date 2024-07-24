package com.byx.pub.service;

import com.byx.pub.bean.xhs.OrderReceiverInfoVo;
import com.byx.pub.bean.xhs.XhsOrderVo;
import com.byx.pub.plus.entity.Admin;
import com.byx.pub.plus.entity.Product;
import com.byx.pub.plus.entity.XhsAuthToken;

import java.util.List;

/**
 * @author Jump
 * @date 2023/12/20 15:49:35
 */
public interface XiaoHongShuService {

    /**
     * 获取订单收件人信息
     * @param orderId
     * @param sellerId
     * @param openAddressId
     * @return
     */
    OrderReceiverInfoVo getReceiverInfo(String orderId, String sellerId, String openAddressId);

    /**
     * 获取物流方案列表
     * @param xhsToken
     * @return
     */
    String getLogisticsList(String xhsToken);
    /**
     * 每25分钟更新一次小红书订单状态
     */
    void synChangeOrder();
    /**
     * 获取过去半小时订单状态变换的订单
     * @param sellerId
     * @return
     */
    List<XhsOrderVo> getUpdateOrderList(String sellerId);
    /**
     * 获取订单列表(1小时5分钟前至今)
     * @return
     */
    List<XhsOrderVo> getAddOrderList(String sellerId);

    /**
     * 创建商品spu
     * @param product
     * @param admin
     * @return
     */
    String createItem(Product product, Admin admin);

    /**
     * 获取运费模板列表
     * @return
     */
    String getCarriageTemplate(String xhsToken);

    /**
     * 获取小红书类目信息
     * @param categoryId
     * @return
     */
    String getCategories(String categoryId,String sellerId);

    /**
     * 获取末级类目下的品牌
     * @param categoryId
     * @return
     */
    String getCategoryBrand(String categoryId,String sellerId);

    /**
     * 获取末级类目下的属性列表
     * @param categoryId
     * @return
     */
    String getCategoryAttributeList(String categoryId,String sellerId);

    /**
     * 获取属性值列表
     * @param attributeId
     * @return
     */
    String getAttributeValueList(String attributeId,String sellerId);

    /**
     * 通过code换token
     * @param code
     * @return
     */
    void code2AccessToken(String code);

    /**
     * 定时检查并刷新小红书token
     * 1)accessToken有效期为7天，refreshToken有效时间为14天
     * 2)accessToken未过期且剩余有效时间大于30分钟，使用refreshToken进行刷新后accessToken和refreshToken均不会刷新
     * 3)accessToken未过期且剩余有效时间小于30分钟，使用refreshToken进行刷新后会得到新的accessToken和refreshToken，且旧accessToken有效期为5分钟
     * 4)accessToken过期后使用refreshToken进行刷新后会得到新的accessToken和refreshToken
     * 5)refreshToken过期后需要通过用户重新授权
     * 6)获取accessToken和刷新accessToken的参数均通过body传输
     * 7)后续所有业务接口的url和获取token的url保持一致
     */
    void checkAndRefreshToken();

    /**
     * 获取店铺token
     * @param sellerId
     * @return
     */
    XhsAuthToken getXhsToken(String sellerId);
    /**
     * 刷新店铺token
     * @param authToken
     */
    void refreshToken(XhsAuthToken authToken);

    /**
     * 每小时拉取最近一小时的新增订单
     */
    void synAddOrder();

    /**
     * 上下架sku
     * @param skuId
     * @param xhsToken
     * @param status
     * @return

    String availableSku(String productId,String xhsToken,Boolean status);*/

    /**
     * 上传素材
     * @param img
     * @return

    String uploadMaterial(String img,String xhsToken);*/

    /**
     * 创建sku
     * @param product
     * @param xhsToken
     * @param itemId
     * @param planInfoId
     * @return

    String createSku(Product product, String xhsToken, String itemId, String planInfoId);*/
}
