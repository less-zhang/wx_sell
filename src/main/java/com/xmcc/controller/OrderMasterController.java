package com.xmcc.controller;

import com.google.common.collect.Maps;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDetailDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.OrderMasterPageDto;
import com.xmcc.service.OrderMasterService;
import com.xmcc.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("buyer/order")
@Api(value = "订单相关接口",description = "完成订单的增删改查")
public class OrderMasterController {

    @Autowired
    private OrderMasterService orderMasterService;

    @PostMapping("/create")
    @ApiOperation(value = "创建订单接口", httpMethod = "POST", response =ResultResponse.class)
    public ResultResponse create(@Valid OrderMasterDto orderMasterDto, BindingResult bindingResult){

        Map<String,String> map = Maps.newHashMap();
        //判断是否有参数校验问题
        if(bindingResult.hasErrors()) {
            List<String> errList = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(errList));
            //将参数校验的错误信息返回给前台
            return ResultResponse.fail(map);
        }
        return orderMasterService.insertOrder(orderMasterDto);
    }

    @PostMapping("/list")
    @ApiOperation(value = "创建订单列表接口", httpMethod = "POST", response =ResultResponse.class)
    public ResultResponse list(OrderMasterPageDto orderMasterPageDto){
        ResultResponse orderMasterList = orderMasterService.getOrderMasterList(orderMasterPageDto);
        return orderMasterList;
    }

    @PostMapping("/detail")
    @ApiOperation(value = "创建订单详情接口", httpMethod = "POST", response =ResultResponse.class)
    public ResultResponse detail(OrderMasterDetailDto orderMasterDetailDto){
        return orderMasterService.getOrderMasterDetail(orderMasterDetailDto);
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "创建取消订单接口", httpMethod = "POST", response =ResultResponse.class)
    public ResultResponse cancel(OrderMasterDetailDto orderMasterDetailDto){
        return orderMasterService.cancelOrderMater(orderMasterDetailDto);
    }

}
