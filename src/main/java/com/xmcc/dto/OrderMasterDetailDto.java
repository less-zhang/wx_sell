package com.xmcc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class OrderMasterDetailDto implements Serializable {

//    openid: 18eu2jwk2kse3r42e2e
//    orderId: 161899085773669363

    @NotBlank(message = "微信id不能为空")
    @ApiModelProperty(value = "微信id",dataType = "String")
    private String openid;

    @NotBlank(message = "订单id不能为空")
    @ApiModelProperty(value = "订单id",dataType = "String")
    private String orderId;

}
