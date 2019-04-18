package com.xmcc.entity;

import com.xmcc.common.OrderEnums;
import com.xmcc.common.PayEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_master")
@Builder
public class OrderMaster {

    @Id
    private String orderId;

    private String buyerName;

    private String buyerAddress;

    private String buyerPhone;

    private String buyerOpenid;

    private BigDecimal orderAmount;

    private Integer orderStatus = OrderEnums.NEW.getCode();

    private Integer payStatus = PayEnums.WAIT.getCode();

    private Date createTime;

    private Date updateTime;

}
