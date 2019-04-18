package com.xmcc.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.OrderEnums;
import com.xmcc.common.PayEnums;
import com.xmcc.common.ResultEnums;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.*;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.exception.CustomException;
import com.xmcc.repository.OrderDetailRepository;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.util.BigDecimalUtil;
import com.xmcc.util.IDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductInfoService productInfoService;

    @Override
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {

        //取出订单项
        List<OrderDetailDto> items = orderMasterDto.getItems();
        //创建detail集合，存储
        List<OrderDetail> detailList = Lists.newArrayList();
        //初始化订单总额
        BigDecimal totalPrice = new BigDecimal("0");

        //遍历items
        for (OrderDetailDto detailDto:items) {
            //查询订单项
            ResultResponse<ProductInfo> resultResponse = productInfoService.queryById(detailDto.getProductId());
            if (resultResponse.getCode() == ResultEnums.FAIL.getCode()) {
                throw new CustomException(ResultEnums.FAIL.getMsg());
            }
            //获取商品信息
            ProductInfo productInfo = resultResponse.getData();
            //库存
            if (productInfo.getProductStock() < detailDto.getProductQuantity()) {
                throw new CustomException(ResultEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //生成detail，放入集合
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtils.createIdbyUUID()).productIcon(productInfo.getProductIcon())
                    .productId(detailDto.getProductId()).productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice()).productQuantity(detailDto.getProductQuantity())
                    .build();
            detailList.add(orderDetail);
            //减少库存
            productInfo.setProductStock(productInfo.getProductStock() - detailDto.getProductQuantity());
            productInfoService.updateProduct(productInfo);
            //计算价格
            totalPrice = BigDecimalUtil.add(totalPrice, BigDecimalUtil.multi(productInfo.getProductPrice(), detailDto.getProductQuantity()));
            //生成订单id
        }

        String orderId = IDUtils.createIdbyUUID();
        //构建订单信息
        OrderMaster orderMaster = OrderMaster.builder().buyerAddress(orderMasterDto.getAddress()).buyerName(orderMasterDto.getName())
                .buyerOpenid(orderMasterDto.getOpenid()).orderStatus(OrderEnums.NEW.getCode())
                .payStatus(PayEnums.WAIT.getCode()).buyerPhone(orderMasterDto.getPhone())
                .orderId(orderId).orderAmount(totalPrice).build();
        //将订单id设置到订单项中
        detailList.stream().map(orderDetail -> {
            orderDetail.setOrderId(orderId);
            return orderDetail;
        }).collect(Collectors.toList());
        //插入订单项
        orderDetailService.batchInsert(detailList);
        //插入订单
        orderMasterRepository.save(orderMaster);
        HashMap<String, String> map = Maps.newHashMap();
        //按照前台要求的数据结构传入
        map.put("orderId",orderId);
        return ResultResponse.succes(map);
    }

    @Override
    public ResultResponse getOrderMasterList(OrderMasterPageDto orderMasterPageDto) {
        if (StringUtils.isBlank(orderMasterPageDto.getOpenid())){
            throw new CustomException("购买微信id不能为空");
        }
        Pageable pageable = PageRequest.of(orderMasterPageDto.getPage(),orderMasterPageDto.getSize());

        Page<OrderMaster> masterPage = orderMasterRepository.findAllByBuyerOpenid(orderMasterPageDto.getOpenid(),pageable );
        List<OrderMasterListDto> collect =
                masterPage.getContent().stream().map(orderMaster -> OrderMasterListDto.bulid(orderMaster)).collect(Collectors.toList());

//        List<OrderMaster> masterList = orderMasterRepository.findByOrderId(orderMasterPageDto.getOpenid());
//        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderIdIn(masterList.stream().map(
//                orderMaster -> orderMaster.getOrderId()).collect(Collectors.toList()));
        OrderDetail orderDetail = new OrderDetail();
        collect.stream().map(orderMasterListDto -> {
            orderMasterListDto.setOrderDetailList(orderDetail);
            return orderMasterListDto;
        }).collect(Collectors.toList());
        //        masterPage.getContent().stream().map(orderMaster -> OrderMasterListDto.bulid(orderMaster)).collect(Collectors.toList()).add((OrderMasterListDto) orderDetailList);
        return ResultResponse.succes(collect);
    }

    @Override
    public ResultResponse getOrderMasterDetail(OrderMasterDetailDto orderMasterDetailDto) {

        OrderMaster orderMaster =
                orderMasterRepository.findAllByOrderIdAndAndBuyerOpenid(orderMasterDetailDto.getOrderId(),orderMasterDetailDto.getOpenid());
        OrderMasterListDto masterListDto = OrderMasterListDto.bulid(orderMaster);
        List<OrderDetail> allByOrderId = orderDetailRepository.findAllByOrderId(orderMasterDetailDto.getOrderId());
        masterListDto.setOrderDetailList(allByOrderId.get(0));
        return ResultResponse.succes(masterListDto);
    }

    @Override
    public ResultResponse cancelOrderMater(OrderMasterDetailDto orderMasterDetailDto) {
        OrderMaster orderMaster =
                orderMasterRepository.findAllByOrderIdAndAndBuyerOpenid(orderMasterDetailDto.getOrderId(),orderMasterDetailDto.getOpenid());
        if (orderMaster == null){
            throw new CustomException(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        if (orderMaster.getOrderStatus() == OrderEnums.FINSH_CANCEL.getCode()){
            throw new CustomException(OrderEnums.FINSH_CANCEL.getMsg());
        }
        orderMaster.setOrderStatus(OrderEnums.CANCEL.getCode());
        orderMaster.setUpdateTime(new Date());
        orderMasterRepository.save(orderMaster);
        return ResultResponse.succes();

    }
}
