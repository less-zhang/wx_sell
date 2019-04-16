package com.xmcc.repository;

import com.xmcc.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo,Integer> {

    //跟据状态和标号查询商品
    List<ProductInfo> findAllByProductStatusAndCategoryTypeIn(Integer status,List<Integer> typeList);

}
