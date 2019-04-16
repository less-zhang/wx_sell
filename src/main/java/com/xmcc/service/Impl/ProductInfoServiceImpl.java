package com.xmcc.service.Impl;

import com.xmcc.common.ResultEnums;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.ProductCategoryDto;
import com.xmcc.dto.ProductInfoDto;
import com.xmcc.entity.ProductCategory;
import com.xmcc.entity.ProductInfo;
import com.xmcc.repository.ProductCategoryRepository;
import com.xmcc.repository.ProductInfoRepository;
import com.xmcc.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public ResultResponse<List<ProductInfoDto>> queryList() {
        //查询所有分类
        List<ProductCategory> all = productCategoryRepository.findAll();
        //判断是否为空
        if (CollectionUtils.isEmpty(all)){
            return ResultResponse.fail();
        }
        //转换成dto
        List<ProductCategoryDto> collect =
                all.stream().map(productCategory -> ProductCategoryDto.build(productCategory)).collect(Collectors.toList());
        //获取类目集合
        List<Integer> typeList =
                collect.stream().map(productCategoryDto -> productCategoryDto.getCategoryType()).collect(Collectors.toList());
        //跟据typeList查询商品列表
        List<ProductInfo> productInfoList =
                productInfoRepository.findAllByProductStatusAndCategoryTypeIn(ResultEnums.PRODUCT_UP.getCode(), typeList);
        //对分类dto遍历，取出每个商品的编号，设置到对应的目录中
        //将商品设置到foods中
        //过滤：不同的type 不同封装
        //将productInfo转成dto
        List<ProductCategoryDto> productCategoryDtos =
                collect.parallelStream().map(productCategoryDto -> {
            productCategoryDto.setProductInfoDtoList(productInfoList.stream()
                    .filter(productInfo -> productInfo.getCategoryType() == productCategoryDto.getCategoryType())
                    .map(productInfo -> ProductInfoDto.build(productInfo)).collect(Collectors.toList()));
            return productCategoryDto;
        }).collect(Collectors.toList());

        return ResultResponse.succes(productCategoryDtos);
    }
}
