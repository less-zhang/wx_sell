package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.ProductInfoDto;
import com.xmcc.entity.ProductInfo;

import java.util.List;


public interface ProductInfoService {

    ResultResponse<List<ProductInfoDto>> queryList();

    ResultResponse<ProductInfo> queryById(String infoId);

    void updateProduct(ProductInfo productInfo);
}
