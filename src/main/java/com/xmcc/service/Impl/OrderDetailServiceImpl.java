package com.xmcc.service.Impl;

import com.xmcc.dao.Impl.BatchDaoImpl;
import com.xmcc.entity.OrderDetail;
import com.xmcc.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends BatchDaoImpl<OrderDetail> implements OrderDetailService {
    @Override
    public void batchInsert(List<OrderDetail> list) {
        super.batchInsert(list);
    }
}
