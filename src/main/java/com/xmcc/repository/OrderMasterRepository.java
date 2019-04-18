package com.xmcc.repository;

import com.xmcc.dto.OrderMasterDetailDto;
import com.xmcc.dto.OrderMasterListDto;
import com.xmcc.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {

    List<OrderMaster> findByOrderId(String orderId);

    Page<OrderMaster> findAllByBuyerOpenid(String openid, Pageable pageable);

    OrderMaster findAllByOrderIdAndAndBuyerOpenid(String openid,String orderid);
}
