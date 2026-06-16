package cn.structured.tenant.service;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.tenant.dto.TenantDTO;
import cn.structured.tenant.query.TenantQuery;
import cn.structured.tenant.vo.TenantVO;

public interface ITenantService {

    Long create(TenantDTO dto);

    void update(Long id, TenantDTO dto);

    void delete(Long id);

    TenantVO findById(Long id);

    ResPage<TenantVO> page(TenantQuery query, ReqPage reqPage);

    void activate(Long id);

    void suspend(Long id);

    void freeze(Long id);

    void expire(Long id);

    void applyTemplate(Long tenantId, Long templateId);
}