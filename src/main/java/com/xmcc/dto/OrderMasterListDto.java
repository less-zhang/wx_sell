package com.xmcc.dto;

import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMasterListDto extends OrderMaster implements Serializable {

    private OrderDetail orderDetailList;

    public static OrderMasterListDto bulid(OrderMaster orderMaster){
        OrderMasterListDto orderMasterListDto = new OrderMasterListDto();
        BeanUtils.copyProperties(orderMaster,orderMasterListDto );
        return orderMasterListDto;
    }

}
