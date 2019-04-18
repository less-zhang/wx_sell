package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderDetailDto;
import com.xmcc.dto.OrderMasterDetailDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.OrderMasterPageDto;

public interface OrderMasterService {

    ResultResponse insertOrder(OrderMasterDto orderMasterDto);

    ResultResponse getOrderMasterList(OrderMasterPageDto orderMasterPageDto);

    ResultResponse getOrderMasterDetail(OrderMasterDetailDto orderMasterDetailDto);

    ResultResponse cancelOrderMater(OrderMasterDetailDto orderMasterDetailDto);

}
