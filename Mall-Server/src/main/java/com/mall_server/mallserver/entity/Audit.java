package com.mall_server.mallserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 审核实体
 */
@TableName("t_audit")
@Data
public class Audit {

    @TableId(type = IdType.AUTO)
    private Integer id; //

    private Integer productId; //

    private Integer userId; //

    private String auditMsg;

    private Integer isAudit; //

    private String productName;

}

