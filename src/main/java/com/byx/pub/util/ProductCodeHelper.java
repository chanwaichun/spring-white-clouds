package com.byx.pub.util;

import com.byx.pub.plus.dao.ProductPlusDao;
import com.byx.pub.plus.entity.Product;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 生成商品编码
 * @author Jump
 * @date 2022/05/16
 */
@Component
public class ProductCodeHelper {
    @Resource
    private ProductPlusDao productPlusDao;

    /**
     * 生成编码
     *
     * @param key   编码前缀
     * @param total 需要生成编码的数量
     * @return 编码集合
     */
    public Queue<String> genCode(String key, int total) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < total; i++) {
            int hashCode = UUID.randomUUID().hashCode();
            String code = String.format("%s%010d", key, Math.abs(hashCode));
            set.add(code);
        }
        removeDuplicateCode(key, set);
        set = reGenCode(set, key, total);
        return new LinkedList<>(set);
    }

    /**
     * 过滤相同code，通过数据库过滤在set集合的code
     *
     * @param key
     * @param set
     */
    private void removeDuplicateCode(String key, Set<String> set) {
        if ("P".equalsIgnoreCase(key)) {
            Set<String> spuCodes = productPlusDao.lambdaQuery()
                    .in(Product::getProductCode, set)
                    .list()
                    .stream()
                    .map(Product::getProductCode)
                    .collect(Collectors.toSet());
            set.removeAll(spuCodes);
        }
    }

    private Set<String> reGenCode(Set<String> set, String key, int total) {
        if (set.size() >= total) {
            return set;
        }

        Set<String> newCodeSet = new HashSet<>();
        int size = set.size();
        for (int i = 0; i < total - size; i++) {
            int hashCode = UUID.randomUUID().hashCode();
            String code = String.format("%s%10d", key, hashCode);
            newCodeSet.add(code);
        }
        removeDuplicateCode(key, newCodeSet);
        set.addAll(newCodeSet);
        // 通过递归过滤重复code，直到set集合大于等于total返回set
        return reGenCode(set, key, total);
    }
}
