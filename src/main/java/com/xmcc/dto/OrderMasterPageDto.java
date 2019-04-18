package com.xmcc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("订单列表,分页参数")
public class OrderMasterPageDto implements Serializable {

    @NotBlank(message = "必须输入用户的微信openid")
    @ApiModelProperty(value = "买家微信openid",dataType = "String")
    private String openid;

    @NotNull(message = "从第几页开始")
    @ApiModelProperty(value = "默认从第0页开始",dataType = "Integer")
    private Integer page = 0;

    @NotNull(message = "每页显示条数")
    @ApiModelProperty(value = "默认10条",dataType = "Integer",example = "10")
    private Integer size = 10;

}
