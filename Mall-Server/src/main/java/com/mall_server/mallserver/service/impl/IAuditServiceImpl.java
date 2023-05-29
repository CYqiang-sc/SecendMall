package com.mall_server.mallserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall_server.mallserver.entity.Audit;
import com.mall_server.mallserver.entity.Product;
import com.mall_server.mallserver.mapper.AuditMapper;
import com.mall_server.mallserver.service.IAuditService;
import org.springframework.stereotype.Service;

/**
 * 管理员Service实现类
 */
@Service("auditService")
public class IAuditServiceImpl extends ServiceImpl<AuditMapper, Audit>  implements IAuditService{
}
