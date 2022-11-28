package com.java.ymjr.core.vo;

import com.java.ymjr.core.pojo.BorrowInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel(description = "借款信息VO类")
public class BorrowInfoVO extends BorrowInfo {
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "手机")
    private String mobile;
    @ApiModelProperty(value = "其它参数")
    private Map<String, Object> params = new HashMap<>();
}
