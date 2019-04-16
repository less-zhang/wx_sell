package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.ProductInfoDto;

import java.util.List;


public interface ProductInfoService {

    ResultResponse<List<ProductInfoDto>> queryList();
}
